package org.ucb.bio134.taskvisualizer.controller;

import javafx.util.Pair;
import org.ucb.bio134.taskvisualizer.model.*;
import org.ucb.bio134.taskvisualizer.view.View;
import org.ucb.c5.semiprotocol.ParseSemiprotocol;
import org.ucb.c5.semiprotocol.model.*;
import org.ucb.c5.semiprotocol.model.Container;
import org.ucb.c5.utils.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 *
 *
 * @author J. Christopher Anderson
 * @author Lucas M. Waldburger
 */
public class Controller {
    private final Semiprotocol protocol;
    private final View view;
    private final Block rackBlock;
    private final Block deckBlock;
    private int currPos = 0;
    private Color green;
    private Color red;
    private Color empty;

    private HashMap<String,Pair<Integer,Integer>> tubeRack;
    private HashMap<String,Pair<Integer,Integer>> pcrRack;
    private HashMap<String,Pair<Integer,Integer>> deck;

    public SemiprotocolPriceSimulator sps;

    public Controller(Semiprotocol protocol, View view) throws Exception{
        this.protocol = protocol;
        this.view = view;
        this.rackBlock = new Block(BlockType.RACK);
        this.tubeRack = new HashMap<>();
        this.pcrRack = new HashMap<>();
        this.deckBlock = new Block(BlockType.DECK);

        sps = new SemiprotocolPriceSimulator();
        sps.initiate();
        sps.run(protocol);

        green = new Color(76,153,0);
        red = new Color(204,0,0);
        empty = new Color(160,160,160);

        view.initiate();
        view.getNextBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    goNext();
                    System.out.println("Mouse go next");
                } catch (Exception ex) {
                    System.err.println("Error going forward");
                    ex.printStackTrace();
                }
            }
        });
        view.getPlayButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                            try {
                                goNext();
                                System.out.println("Mouse go next");
                            } catch (Exception ex) {
                                System.err.println("Error going forward");
                                ex.printStackTrace();
                            }
                        }
                }).start();
            }
        });
        //For now, the same as 'Play' (waiting on TZ for her human burden portion)
        view.getSimulateButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            goNext();
                            System.out.println("Mouse go next");
                        } catch (Exception ex) {
                            System.err.println("Error going forward");
                            ex.printStackTrace();
                        }
                    }
                }).start();
            }
        });


        view.getEndButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < protocol.getSteps().size();i++) {
                    try {
                        goNext();
                        System.out.println("Mouse go next");
                    } catch (Exception ex) {
                        System.err.println("Error going forward");
                        ex.printStackTrace();
                    }
                }
                view.endProtocol();
            }
        });



    }

    /**
     *
     * @param addcon
     * @throws Exception
     */
    private void populateView(AddContainer addcon) throws Exception {
        Color wellColor = green;
        Pair<Integer, Integer> position = Well.parseWellLabel(addcon.getLocation());
        if ((addcon.getTubetype().equals(Container.eppendorf_1p5mL) ||
                addcon.getTubetype().equals(Container.eppendorf_2mL) )
                && !tubeRack.containsValue(position)) {
            tubeRack.put(addcon.getName(),position);
            view.colorWell(ContainerType.TUBE,BlockType.RACK,wellColor,position.getKey(),position.getValue());
        } else if (addcon.getTubetype().equals(Container.pcr_tube) && !pcrRack.containsValue(position)) {
            pcrRack.put(addcon.getName(),position);
            view.colorWell(ContainerType.PCR,BlockType.RACK,wellColor,position.getKey(),position.getValue());
        } else if (addcon.getTubetype().equals(Container.pcr_plate_96)) {
            view.addPlate(addcon.getName(),ContainerType.PCR, position.getKey(), position.getValue());
        } else if (addcon.getTubetype().equals(Container.pcr_strip)) {
            //TODO: Implement pcr stip tube color
        }
    }

    /**
     *
     * @param revcon
     * @throws Exception
     */
    private void unpopulateView(RemoveContainer revcon) throws Exception {
        Color wellColor = empty;
        String name = revcon.getContainer();
        Pair <Integer,Integer> position;

        if (tubeRack.containsKey(name)) {
            position = tubeRack.get(name);
            view.colorWell(ContainerType.TUBE,BlockType.RACK,wellColor,position.getKey(),position.getValue());
            tubeRack.remove(name);
        } else if (pcrRack.containsKey(name)) {
            position = pcrRack.get(name);
            view.colorWell(ContainerType.PCR,BlockType.RACK,wellColor,position.getKey(),position.getValue());
            pcrRack.remove(name);
        } else if (deck.containsKey(name)){
            position = deck.get(name);
//            view.colorWell(ContainerType.PCR,BlockType.RACK,wellColor,position.getKey(),position.getValue());
            //TODO: Test remove plate from deck functionality
            deckBlock.removePlate(revcon.getContainer());
            deck.remove(name);
        } else if (pcrRack.containsKey(name)) {
            //TODO: Implement pcr stip tube color
        } else {
            throw new Exception("Cannot find container to remove");
        }
    }

    /**
     *
     * @throws Exception
     */
    private void goNext() throws Exception {
        if(protocol.getSteps().size() - 1 < currPos) {
            view.endProtocol();
            return;
        }
        int burdenCount = 0;
        Task task = protocol.getSteps().get(currPos);
        switch(task.getOperation()) {
            case addContainer:
                AddContainer addcon = (AddContainer) task;
//                deckBlock.addPlate(addcon.getName(), addcon.getTubetype(), position.getKey(), position.getValue());

                populateView(addcon);

                //Update the view
//                view.resetAll();
//                view.addTube(addcon.getName(), position.getKey(), position.getValue());
                break;
            case removeContainer:
                //TODO: TEST REMOVE CONTAINER FUNCTIONALITY
                System.out.println("Remove container");
                RemoveContainer revcon = (RemoveContainer) task;
                unpopulateView(revcon);
//                deckBlock.removePlate(revcon.getContainer());
                break;
            case transfer:
                //TODO: TEST TRANSFER HIGHLIGHT WELL FUNCTIONALITY
                Transfer tfer = (Transfer) task;
                Well srcwell = deckBlock.getWell(tfer.getSource());
                srcwell.removeVolume(tfer.getSource(),tfer.getVolume());
                Well dstwell = deckBlock.getWell(tfer.getDest());
                dstwell.addVolume(tfer.getSource(),tfer.getVolume());

                //Update the view
//                view.resetAll();
                String srcLocation = tfer.getSource();
                System.out.println("srcLocation" + srcLocation);
                Pair<Integer,Integer> srcPlatePos = deckBlock.getPlatePos(srcLocation);
                Pair<Integer,Integer> srcWell = Well.parseWellLabel(tfer.getSource());
                System.out.println("srcPlatePos" + srcPlatePos);
                System.out.println("srcWell" + srcWell);
                //Color color, int plateRow, int plateCol, int wellRow, int wellCol

                if (tfer.getSource().equals("deck")) {
                    view.highlightDeck(Color.pink, srcPlatePos.getKey(), srcPlatePos.getValue(), srcWell.getKey(), srcWell.getValue());
                } else if (tfer.getSource().equals("tube_rack")) {
                    view.highlightRack(Color.pink, ContainerType.TUBE, srcWell.getKey(), srcWell.getValue());
                } else if (tfer.getSource().equals("tube_rack")) {
                    view.highlightRack(Color.pink, ContainerType.PCR, srcWell.getKey(), srcWell.getValue());
                }
//                view.colorWell(Color.white, srcPlatePos.getKey(), srcPlatePos.getValue(), srcWell.getKey(), srcWell.getValue());

                Pair<Integer,Integer> dstPlatePos = deckBlock.getPlatePos(tfer.getDest());
                Pair<Integer,Integer> dstWell = Well.parseWellLabel(tfer.getDest());

                if (tfer.getDest().equals("deck")) {
                    view.highlightDeck(Color.pink, dstPlatePos.getKey(), dstPlatePos.getValue(), dstWell.getKey(), dstWell.getValue());
                } else if (tfer.getDest().equals("tube_rack")) {
                    view.highlightRack(Color.pink, ContainerType.TUBE, dstWell.getKey(), dstWell.getValue());
                } else if (tfer.getDest().equals("tube_rack")) {
                    view.highlightRack(Color.pink, ContainerType.PCR, dstWell.getKey(), dstWell.getValue());
                }
//                view.colorWell(Color.green, dstPlatePos.getKey(), dstPlatePos.getValue(), dstWell.getKey(), dstWell.getValue());
                break;
            case multichannel:
                Multichannel multi = (Multichannel) task;

                break;
            case dispense:
                Dispense disp = (Dispense) task;
                break;
        }
        view.notify(task);
        view.currentTask(task);
        view.updateCounter(sps.getReagentTotal(),sps.getTubeTotal(),sps.getTipTotal());
        view.updateBurden(burdenCount);
        currPos++;

    }


    public static void main(String[] args) throws Exception {
        //Load the protocol
        String text = FileUtils.readResourceFile("semiprotocol/data/full_workspace_test.txt");
        ParseSemiprotocol parser = new ParseSemiprotocol();
        parser.initiate();
        Semiprotocol protocol = parser.run(text);

        //Create the View
//        View view = new View(protocol);
        View view = new View();

        //Create the Controller and initiate the GUI
        Controller controller = new Controller(protocol, view);
    }
}
