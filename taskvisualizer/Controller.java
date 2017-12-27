package org.ucb.bio134.taskvisualizer;

import javafx.util.Pair;
import org.ucb.bio134.taskmaster.model.Tip;
import org.ucb.bio134.taskvisualizer.model.*;
import org.ucb.bio134.taskvisualizer.view.View;
import org.ucb.c5.semiprotocol.ParseSemiprotocol;
import org.ucb.c5.semiprotocol.model.*;
import org.ucb.c5.semiprotocol.model.Container;
import org.ucb.c5.utils.FileUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
 * @author J. Christopher Anderson
 * @author Lucas M. Waldburger
 */
public class Controller implements MouseListener{
    private Semiprotocol protocol;
    private final View view;
    private final Rack rack;
    private final Deck deck;
    private int currPos = 0;
    private SemiprotocolPriceSimulator sps;
    private SemiprotocolBurdenSimulator sbs;
    private JFileChooser fc;

    /** Initates the Controller
     *
     * @param view the display for the user to visualize the workspace
     * @throws Exception cannot locate protocol, or error associated with the View
     */
    public Controller(View view) throws Exception{
        // Instantiate and assign class variables
        this.view = view;
        this.rack = new Rack();
        this.deck = new Deck();
        this.protocol = null;

        ParseSemiprotocol parser = new ParseSemiprotocol();
        parser.initiate();

        // Instantiate SemiprotocolPriceSimulator for the 'Price Tracker' panel
        sps = new SemiprotocolPriceSimulator();
        sps.initiate();

        // Instantiate SemiprotocolBurdenSimulator for the 'Burden Tracker' panel
        sbs = new SemiprotocolBurdenSimulator();
        sbs.initiate();

        view.initiate();

        fc = new JFileChooser();

        // Goes through the semiprotocol one step at a time
        view.getNextButton().addActionListener(new ActionListener() {
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

        // Allows user to select a Semiprotocol file from their directory
        view.getOpenButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                //Handle open button action.
                    int returnVal = fc.showOpenDialog(view);

                    if (returnVal == JFileChooser.APPROVE_OPTION) {

                        BufferedReader reader = null;

                        File file = null;
                        try {
                            file = fc.getSelectedFile();
                            reader = new BufferedReader(new FileReader(file));

                            String line;

                            StringBuilder sb = new StringBuilder();

                            while ((line = reader.readLine()) != null) {
                                sb.append(line).append("\n");
                            }

                            protocol = parser.run(sb.toString());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        } finally {
                            try {
                                reader.close();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                        view.getTaskPanel().append("Selected: " + file.getName());
                    } else {
                        view.getTaskPanel().append("Open command cancelled by user");
                    }




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
     * @throws Exception error with AddContainer step
     */
    private void populate(AddContainer addcon) throws Exception {
        Pair<Integer, Integer> position = Well.parseWellLabel(addcon.getLocation());
        BlockType destinationType = calcDestinationType(addcon.getTubetype());

        if (destinationType.equals(BlockType.RACK) && !rack.containsTube(addcon.getName())) {
            rack.addTube(addcon,position.getKey(),position.getValue());
            view.visualizeTube(addcon,position.getKey(),position.getValue());
        } else if (destinationType.equals(BlockType.DECK) && !deck.containsPlate(addcon.getName())) {
            deck.addPlate(addcon.getName(),addcon.isNew(),position.getKey(),position.getValue());
            view.visualizePlate(addcon.isNew(),position.getKey(), position.getValue());
        } else if (rack.containsTube(addcon.getName())) {
            throw new Exception("Tube already exists in workspace");
        } else {
            throw new Exception("Error populating view");
        }
    }

    /**
     * Removes a container from the specified position in the workspace as specified in the semiprotocl.
     *
     * @param remcon name of container to be removed from the workspace
     * @throws Exception error with container Name
     */
    private void unpopulate(RemoveContainer remcon) throws Exception {
        String containerName = remcon.getName();
        ContainerType containerType = calcContainerType(remcon.getContainer());
        Pair <Integer,Integer> position;

        if (rack.containsTube(containerName)) {
            position = rack.getTubePos(containerType, containerName);
            rack.removeTube(containerName,containerType);
            view.removeTubeFromRack(containerName, containerType, position.getKey(), position.getValue());
        } else if (deck.containsPlate(containerName)){
            position = deck.getPlate(containerName);
            deck.removePlate(containerName);
            view.removePlate(containerName,ContainerType.PCR,position.getKey(),position.getValue());
        } else {
            throw new Exception("Cannot find container to remove from workspace");
        }
    }

    private void transfer(String source, String destination, double volume) throws Exception {
        // Within the same rack (tube to tube, pcr to pcr) or across racks (tube to pcr, pcr to tube)
        if (rack.containsTube(source) && rack.containsTube(destination)) {
            Pair<Integer, Integer> sourcePos = rack.getTubeLocation(source);
            Pair<Integer, Integer> destinationPos = rack.getTubeLocation(destination);
            // Remove volume from source
            rack.getTube(source).removeVolume(volume);
            view.removeVolume(volume, rack.getRackType(source), sourcePos.getKey(), sourcePos.getValue());
            // Add volume to destination
            rack.getTube(destination).addVolume(source,volume);
            view.addVolume(source, volume, rack.getRackType(destination), destinationPos.getKey(), destinationPos.getValue());

            // Update the View
            view.highlightRackTransfer(rack.getRackType(source), sourcePos.getKey(), sourcePos.getValue(),
                    rack.getRackType(destination), destinationPos.getKey(), destinationPos.getValue());
        }
        // Within the deck (one pcr plate to another pcr plate) -- volume is not predetermined
        else if (deck.containsPlate(Deck.calcPlateName(source)) && deck.containsPlate(Deck.calcPlateName(destination))) {
            Pair<Integer,Integer> sourcePos = deck.getPlatePos(source);
            Pair<Integer,Integer> sourceWellPos = Well.parseWellLabel(source);
            Pair<Integer,Integer> destinationPos = deck.getPlatePos(destination);
            Pair<Integer,Integer> destinationWellPos = Well.parseWellLabel(destination);

            // Update the View
            view.addBlackBorderToWells(sourcePos.getKey(), sourcePos.getValue(), sourceWellPos.getKey(),sourceWellPos.getValue());
            view.addBlackBorderToWells(destinationPos.getKey(), destinationPos.getValue(),destinationWellPos.getKey(),
                    destinationWellPos.getValue());
        }
        // Rack to deck (tube to deck, pcr to deck)
        else if (rack.containsTube(source) && deck.containsPlate(Deck.calcPlateName(destination))){
            Pair<Integer, Integer> sourcePos = rack.getTubeLocation(source);
            Pair<Integer,Integer> destinationPos = deck.getPlatePos(destination);
            Pair<Integer,Integer> destinationWellPos = Well.parseWellLabel(destination);

            // Remove volume from source
            rack.getTube(source).removeVolume(volume);

            // Update the View
            view.addBlackBorderToWells(destinationPos.getKey(), destinationPos.getValue(),destinationWellPos.getKey(),
                    destinationWellPos.getValue());
            view.highlightRackPos(rack.getRackType(source),sourcePos.getKey(),sourcePos.getValue());
        }
        // Deck to rack
        else if (deck.containsPlate(Deck.calcPlateName(source)) && rack.containsTube(destination)) {
            Pair<Integer,Integer> sourcePos = deck.getPlatePos(source);
            Pair<Integer,Integer> sourceWellPos = Well.parseWellLabel(source);
            Pair<Integer, Integer> destinationPos = rack.getTubeLocation(destination);

            // Add volume to destination
            rack.getTube(destination).addVolume(source,volume);

            // Update the View
            view.addBlackBorderToWells(sourcePos.getKey(), sourcePos.getValue(),sourceWellPos.getKey(),
                    sourceWellPos.getValue());
            view.highlightRackPos(rack.getRackType(destination),destinationPos.getKey(),destinationPos.getValue());
        }
        else {
            throw new Exception("Error determining transfer type");
        }

        //Update the pipette panel
        view.updatePipette(sps.getTip(volume));
    }

    /**
     * Dispense for rack only at the moment since a more complete specification of the well within the plate is
     * required to consider the deck.
     *
     * @param disp
     * @throws Exception
     */
    private void dispense(Dispense disp) throws Exception {
        String destination = disp.getDstContainer();
        ContainerType containerType = rack.getRackType(destination);
        Pair<Integer,Integer> destinationPos = rack.getTubePos(containerType, destination);
        String reagentName = disp.getReagent().toString();
        double volume = disp.getVolume();

        if (rack.containsTube(destination)) {
            rack.getTube(destination).addVolume(reagentName, volume);
            view.addCyanBorderToWell(containerType,destinationPos.getKey(),destinationPos.getValue());
            view.addVolume(reagentName, volume, rack.getRackType(destination), destinationPos.getKey(), destinationPos.getValue());
        }  else {
            throw new Exception("Error determining dispense type");
        }

        //Update the pipette panel
        view.updatePipette(sps.getTip(volume));
    }

    /**
     * Goes through the semiprotocol one task at a time until the end is reached. Panels are updated accordingly to
     * simulate the action in the task in the workspace.
     *
     * @throws Exception
     */
    private void goNext() throws Exception {
        view.resetPipette();
        view.removeBlackBorderFromWells();
        view.removeCyanBorderFromWell();

        if(protocol.getSteps().size() - 1 < currPos) {
            view.endProtocol();
            return;
        }
        int burdenCount = 0;
        Tip tip;
        Pair<Integer,Integer> position;

        Task task = protocol.getSteps().get(currPos);
        switch(task.getOperation()) {
            case addContainer:
                AddContainer addcon = (AddContainer) task;
                populate(addcon);
                break;
            case removeContainer:
                RemoveContainer remcon = (RemoveContainer) task;
                unpopulate(remcon);
                break;
            case transfer:
                Transfer tfer = (Transfer) task;
                transfer(tfer.getSource(),tfer.getDest(),tfer.getVolume());
                break;
            case multichannel:
                Multichannel multi = (Multichannel) task;//TODO: Implement multichannel capability
                break;
            case dispense:
                Dispense disp = (Dispense) task;
                dispense(disp);
                break;
        }
        view.notifyNotifPanel(task);
        view.notifyTaskPanel(task);
        sps.run(task);
        sbs.run(task);
        view.notifyPricePanel(sps.getReagentTotal(),sps.getTubeTotal(),sps.getTipTotal());
        view.updateBurden(sbs.getCurrentBurden());
        currPos++;
    }

    public static void main(String[] args) throws Exception {
        //Load the protocol
//        String text = FileUtils.readResourceFile("semiprotocol/data/test_tube_overflow.txt");
//        String text = FileUtils.readResourceFile("semiprotocol/data/full_workspace_test.txt");




//        String text = FileUtils.readResourceFile("semiprotocol/data/alibaba_semiprotocol.txt");
//        String text = FileUtils.readResourceFile("semiprotocol/data/test_removeContainer.txt");


        //Create the View
        View view = new View();

        //Create the Controller and initiate the GUI
        Controller controller = new Controller(view);
    }

    public void mousePressed(MouseEvent e) {
        System.out.println("Mouse pressed (# of clicks: "
                + e.getClickCount() + ")");
    }

    public void mouseReleased(MouseEvent e) {
        System.out.println("Mouse released (# of clicks: "
                + e.getClickCount() + ")");
    }

    public void mouseEntered(MouseEvent e) {
        System.out.println("Mouse entered");
    }

    public void mouseExited(MouseEvent e) {
        System.out.println("Mouse exited");
    }

    public void mouseClicked(MouseEvent e) {
        System.out.println("Mouse clicked (# of clicks: "
                + e.getClickCount() + ")");
    }
//    public static ContainerType calcContainerType(Container con) throws Exception {
//        if ((con.equals(Container.eppendorf_1p5mL) ||
//                con.equals(Container.eppendorf_2mL) )) {
//            return ContainerType.TUBE;
//        } else if (con.equals(Container.pcr_tube)) {
//            return ContainerType.PCR;
//        } else if (con.equals(Container.pcr_plate_96)) {
//            return ContainerType.PLATE;
//        } else if (con.equals(Container.pcr_strip)) {
//            //TODO: Implement pcr stip tube color
//            return ContainerType.PCR;
//        } else {
//            throw new Exception("Error calculating container type");
//        }
//    }

    public static BlockType calcDestinationType(Container con) throws Exception {
        if (con.equals(Container.eppendorf_1p5mL) ||
                con.equals(Container.eppendorf_2mL) || con.equals(Container.pcr_tube)
                || con.equals(Container.pcr_strip)) {
            return BlockType.RACK;
        } else if (con.equals(Container.pcr_plate_96)) {
            return BlockType.DECK;
        } else {
            throw new Exception("Error calculating destination type");
        }
    }
    public static ContainerType calcContainerType(Container con) throws Exception {
        if ((con.equals(Container.eppendorf_1p5mL) ||
                con.equals(Container.eppendorf_2mL))) {
            return ContainerType.TUBE;
        } else if (con.equals(Container.pcr_tube) || con.equals(Container.pcr_strip)) {
            return ContainerType.PCR;
        } else {
            throw new Exception("Error calculating container type");
        }
    }
}
