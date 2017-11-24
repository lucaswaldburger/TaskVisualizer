package org.ucb.bio134.taskvisualizer.controller;

import javafx.util.Pair;
import org.ucb.bio134.taskvisualizer.model.Block;
import org.ucb.bio134.taskvisualizer.model.BlockType;
import org.ucb.bio134.taskvisualizer.model.ContainerType;
import org.ucb.bio134.taskvisualizer.model.Well;
import org.ucb.bio134.taskvisualizer.view.View;
import org.ucb.c5.semiprotocol.ParseSemiprotocol;
import org.ucb.c5.semiprotocol.model.*;
import org.ucb.c5.semiprotocol.model.Container;
import org.ucb.c5.utils.FileUtils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 *
 *
 * @author J. Christopher Anderson
 * @author Lucas M. Waldburger
 */
public class Controller {
    private final Semiprotocol protocol;
    private final View view;
    private final Block racks;
    private final Block deck;
    private int currPos = 0;
    private Color green;
    private Color red;

    private HashMap<String,Pair<Integer,Integer>> tubeRack;
    private HashMap<String,Pair<Integer,Integer>> pcrRack;

    public Controller(Semiprotocol protocol, View view) {
        this.protocol = protocol;
        this.view = view;
        this.racks = new Block(BlockType.RACK);
        tubeRack = new HashMap<>();
        pcrRack = new HashMap<>();
        this.deck = new Block(BlockType.DECK);
        green = new Color(76,153,0);
        red = new Color(204,0,0);


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
                try {
                    for (int i = 0; i < protocol.getSteps().size();i++) {
                        goNext();
                        System.out.println("Mouse go next");
                    }

                } catch (Exception ex) {
                    System.err.println("Error going forward");
                    ex.printStackTrace();
                }
            }
        });

    }
    private void goNext() throws Exception {
        if(protocol.getSteps().size() - 1 < currPos) {
//            view.endProtocol();
            return;
        }

        Task task = protocol.getSteps().get(currPos);
        switch(task.getOperation()) {
            case addContainer:
                AddContainer addcon = (AddContainer) task;
                Pair<Integer, Integer> position = Well.parseWellLabel(addcon.getLocation());
//                deck.addPlate(addcon.getName(), addcon.getTubetype(), position.getKey(), position.getValue());
                if ((addcon.getTubetype().equals(Container.eppendorf_1p5mL) ||
                        addcon.getTubetype().equals(Container.eppendorf_2mL) )
                        && !tubeRack.containsValue(position)) {
                    tubeRack.put(addcon.getName(),position);
                    view.colorWell(ContainerType.TUBE,BlockType.RACK,green,position.getKey(),position.getValue());
                } else if (addcon.getTubetype().equals(Container.pcr_tube) && !pcrRack.containsValue(position)) {
                    pcrRack.put(addcon.getName(),position);
                    view.colorWell(ContainerType.PCR,BlockType.RACK,green,position.getKey(),position.getValue());
                } else if (addcon.getTubetype().equals(Container.pcr_plate_96)) {
                    view.addPlate(addcon.getName(),ContainerType.PCR, position.getKey(), position.getValue());
                }
                System.out.println(addcon.getTubetype());
                System.out.println(Well.parseWellLabel(addcon.getLocation()));
                System.out.println(Well.calcWellLabel(Well.parseWellLabel(addcon.getLocation())));

                //Update the view
//                view.resetAll();
//                view.addTube(addcon.getName(), position.getKey(), position.getValue());
                break;
            case removeContainer:
                System.out.println("Remove container");
                RemoveContainer rc = (RemoveContainer) task;
                deck.removePlate(rc.getContainer());
                break;
            case transfer:
                Transfer tfer = (Transfer) task;
                Well srcwell = deck.getWell(tfer.getSource());
                srcwell.removeVolume(tfer.getSource(),tfer.getVolume());
                Well dstwell = deck.getWell(tfer.getDest());
                dstwell.addVolume(tfer.getSource(),tfer.getVolume());

//                //Update the view
//                view.resetAll();
//                String srcLocation = tfer.getSource();
//                System.out.println("srcLocation" + srcLocation);
//                Pair<Integer,Integer> srcPlatePos = deck.getPlatePos(srcLocation);
//                Pair<Integer,Integer> srcWell = Well.parseWellLabel(tfer.getSource());
//                System.out.println("srcPlatePos" + srcPlatePos);
//                System.out.println("srcWell" + srcWell);
//                //Color color, int plateRow, int plateCol, int wellRow, int wellCol
//                view.colorWell(Color.white, srcPlatePos.getKey(), srcPlatePos.getValue(), srcWell.getKey(), srcWell.getValue());

                Pair<Integer,Integer> dstPlatePos = deck.getPlatePos(tfer.getDest());
                Pair<Integer,Integer> dstWell = Well.parseWellLabel(tfer.getDest());

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
