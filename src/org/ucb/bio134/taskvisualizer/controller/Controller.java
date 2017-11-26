package org.ucb.bio134.taskvisualizer.controller;

import javafx.util.Pair;
import org.ucb.bio134.taskmaster.model.Tip;
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

/**
 * Controller for TaskVisualizer with a complete workspace consisting of two components: racks and a deck. For now,
 * there is a default of one tube rack for Eppendorf tubes and one pcr rack for pcr tubes and pcr strip tubes.
 * The deck is composed of up to four pcr plates that are added to the workspace according to the tasks in the
 * semiprotocol
 *
 * In the future, I plan to add tabs to allow for multiple tube and/or pcr racks as well as allow for a greater degree
 * of freedom for the user to configure the deck to their liking. This version has additional panels for accessory
 * information that I hope the user will find to aid with debugging or further optimizing their program that designs
 * semiprotocol files or follow the logic of their output step by step using the TaskVisualizer.
 *
 *
 * @author J. Christopher Anderson
 * @author Lucas M. Waldburger
 */
public class Controller {
    private final Semiprotocol protocol;
    private final View view;
    private final Rack rack;
    private final Deck deck;
    private int currPos = 0;
    public SemiprotocolPriceSimulator sps;

    public Controller(Semiprotocol protocol, View view) throws Exception{
        // Instantiate and assign class variables
        this.protocol = protocol;
        this.view = view;
        this.rack = new Rack();
        this.deck = new Deck();

        // Instantiate SemiprotocolPriceSimulator for the 'Price Tracker' panel
        sps = new SemiprotocolPriceSimulator();
        sps.initiate();
        view.initiate();

        // Goes through the semiprotocol one step at a time
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

        // Goes through the semiprotocol automatically at a set speed (500 milliseconds)
        view.getPlayButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Timer(500, new ActionListener() {
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

        // For now, the same as 'Play' function (waiting on TZ's human burden portion)
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

        // Goes to the end of the semiprotocol to display the final state of the workspace
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
     * Adds a container to the appropriate position in the workspace based on the type of the container
     * described in the semiprotocol.
     *
     * @param addcon container to be added to the workspace
     * @throws Exception
     */
    private void populateView(AddContainer addcon) throws Exception {
        Pair<Integer, Integer> position = Well.parseWellLabel(addcon.getLocation());
        if ((addcon.getTubetype().equals(Container.eppendorf_1p5mL) ||
                addcon.getTubetype().equals(Container.eppendorf_2mL) )
                && !rack.containsTube(ContainerType.TUBE,addcon.getName())) {
            rack.addTube(addcon.getName(),ContainerType.TUBE,addcon.getTubetype(),position.getKey(),position.getValue());
            view.addTubeToRack(addcon.getName(),addcon.getTubetype(),ContainerType.TUBE,position.getKey(),position.getValue());
        } else if (addcon.getTubetype().equals(Container.pcr_tube) && !rack.containsTube(ContainerType.PCR,addcon.getName())) {
            rack.addTube(addcon.getName(),ContainerType.PCR,addcon.getTubetype(),position.getKey(),position.getValue());
            view.addTubeToRack(addcon.getName(),addcon.getTubetype(),ContainerType.PCR,position.getKey(),position.getValue());
        } else if (addcon.getTubetype().equals(Container.pcr_plate_96)) {
            deck.addPlate(addcon.getName(),position.getKey(),position.getValue());
            view.addPlate(addcon.getName(),ContainerType.PCR, position.getKey(), position.getValue());
        } else if (addcon.getTubetype().equals(Container.pcr_strip)) {
            //TODO: Implement pcr stip tube color
        } else if (rack.containsTube(ContainerType.TUBE, addcon.getName()) || rack.containsTube(ContainerType.PCR, addcon.getName())) {
            throw new Exception("Tube already exists in workspace");
        } else {
            throw new Exception("Error populating view");
        }
    }

    /**
     * Removes a container from the specified position in the workspace as specified in the semiprotocl.
     *
     * @param containerName name of container to be removed from the workspace
     * @throws Exception
     */
    private void unpopulateView(String containerName) throws Exception {
        Pair <Integer,Integer> position;
        if (rack.containsTube(ContainerType.TUBE,containerName)) {
            position = rack.getTubePos(ContainerType.TUBE, containerName);
            view.removeTubeFromRack(containerName, ContainerType.TUBE, position.getKey(), position.getValue());
            rack.removeTube(containerName,ContainerType.TUBE);
        } else if (rack.containsTube(ContainerType.PCR,containerName)) {
            position = rack.getTubePos(ContainerType.PCR, containerName);
            view.removeTubeFromRack(containerName, ContainerType.PCR, position.getKey(), position.getValue());
            rack.removeTube(containerName,ContainerType.PCR);
        } else if (deck.containsPlate(containerName)){
            position = deck.getPlate(containerName);
            view.removePlate(containerName,ContainerType.PCR,position.getKey(),position.getValue());
            deck.removePlate(containerName);
        } else if (rack.containsTube(ContainerType.PCR,containerName)) {
            //TODO: Implement pcr stip tube color
        } else {
            throw new Exception("Cannot find container to remove");
        }
    }

    /**
     * Goes through the semiprotocol one task at a time until the end is reached. Panels are updated accordingly to
     * simulate the action in the task in the workspace.
     *
     * @throws Exception
     */
    private void goNext() throws Exception {
        if(protocol.getSteps().size() - 1 < currPos) {
            view.endProtocol();
            return;
        }
        int burdenCount = 0;
        Tip tip;
        view.resetPipette();
        Task task = protocol.getSteps().get(currPos);
        switch(task.getOperation()) {
            case addContainer:
                AddContainer addcon = (AddContainer) task;
                populateView(addcon);
                break;
            case removeContainer:
                RemoveContainer remcon = (RemoveContainer) task;
                unpopulateView(remcon.getName());
                break;
            case transfer:
                //TODO: Implement transfer ability with highlighted wells
                Transfer tfer = (Transfer) task;
                tip = sps.getTip(tfer.getVolume());
                view.updatePipette(tip);
                Well srcwell = rack.getTube(tfer.getSource());
                srcwell.removeVolume(tfer.getVolume());
                Well dstwell = deck.getWell(tfer.getDest());
                dstwell.addVolume(tfer.getSource(),tfer.getVolume());

                //Update the view
//                view.resetAll();
                String srcLocation = tfer.getSource();
                System.out.println("srcLocation" + srcLocation);
                Pair<Integer,Integer> srcPlatePos = deck.getPlatePos(srcLocation);
                Pair<Integer,Integer> srcWell = Well.parseWellLabel(tfer.getSource());
                System.out.println("srcPlatePos" + srcPlatePos);
                System.out.println("srcWell" + srcWell);
                //Color color, int plateRow, int plateCol, int wellRow, int wellCol
                System.out.println(tfer.getSource());
                if (tfer.getSource().equals("deck")) {
                    view.highlightDeck(Color.pink, srcPlatePos.getKey(), srcPlatePos.getValue(), srcWell.getKey(), srcWell.getValue());
                } else if (tfer.getSource().equals("rack")) {
                    view.highlightRack(Color.pink, ContainerType.TUBE, srcWell.getKey(), srcWell.getValue());
                } else if (tfer.getSource().equals("rack")) {
                    view.highlightRack(Color.pink, ContainerType.PCR, srcWell.getKey(), srcWell.getValue());
                }
//                view.colorWell(Color.white, srcPlatePos.getKey(), srcPlatePos.getValue(), srcWell.getKey(), srcWell.getValue());

                Pair<Integer,Integer> dstPlatePos = deck.getPlatePos(tfer.getDest());
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
                //TODO: Implement multichannel capability
                break;
            case dispense:
                Dispense disp = (Dispense) task;
                String dstContainer = disp.getDstContainer();
                tip = sps.getTip(disp.getVolume());
                view.updatePipette(tip);
                Pair<Integer, Integer> position;
                if (rack.containsTube(ContainerType.TUBE,dstContainer)) {
                    position = rack.getTubePos(ContainerType.TUBE, dstContainer);
                    view.addVolToRack(disp.getReagent(),disp.getVolume(),ContainerType.TUBE,position.getKey(),position.getValue());
                } else if (rack.containsTube(ContainerType.PCR,dstContainer)) {
                    position = rack.getTubePos(ContainerType.PCR, dstContainer);
                    view.addVolToRack(disp.getReagent(), disp.getVolume(), ContainerType.PCR, position.getKey(), position.getValue());
                }
                break;
        }
        view.notify(task);
        view.currentTask(task);
        sps.run(task);
        view.updatePrice(sps.getReagentTotal(),sps.getTubeTotal(),sps.getTipTotal());
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
        View view = new View();

        //Create the Controller and initiate the GUI
        Controller controller = new Controller(protocol, view);
    }
}
