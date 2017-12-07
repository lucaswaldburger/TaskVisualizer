package org.ucb.bio134.taskvisualizer.model;

import javafx.util.Pair;
import org.ucb.c5.semiprotocol.model.Container;
import org.ucb.c5.semiprotocol.model.Reagent;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * Describes a Well that is a part of a Plate and can have a tube if the plate is a part of the Rack.
 *
 * @author J. Christopher Anderson
 * @author Lucas M. Waldburger
 */
public class Well {
    private HashMap<String, Double> contents;
    private HashMap<String, Double> mixture;
    private Double currentVolume;
    private String tubeName;
    private Container tube;
    private double maxVol;
    private ContainerType plateType;
    private BlockType block;

//    /**
//     * Constructs an empty well object
//     */
//    public Well() {
//        this.tubeName = null;
//        this.currentVolume = 0.0;
//        this.maxVol = 0.0;
//        this.tube = null;
//        this.contents = new HashMap<>();
//        this.mixture = new HashMap<>();
//    }

    /**
     * Constructs an empty well object as part of a plate
     *
     * @param plateType plate type used to denote PCR or TUBE
     */
    public Well(ContainerType plateType, BlockType blockType, boolean isNew) {
        this.tubeName = null;
        this.tube = null;
        this.plateType = plateType;
        if (!isNew) {
            this.currentVolume = 200.0;
        } else {
            this.currentVolume = 0.0;
        }
        this.block = blockType;
        this.maxVol = 0.0;
        this.contents = new HashMap<>();
        this.mixture = new HashMap<>();
    }
//    /**
//     * Constructs an occupied
//     *
//     * @param tubeName
//     * @param tube
//     * @param plateType
//     */
//    public Well(String tubeName, Container tube, ContainerType plateType) {
//        this.tubeName = tubeName;
//        this.tube = tube;
//        this.plateType = plateType;
//        this.currentVolume = 0.0;
//        calcMaxVolume(tube);
//
//        this.contents = new HashMap<>();
//        this.mixture = new HashMap<>();
//    }

    /**
     * Adds a tube to the Well
     *
     * @param tubeName name of the tube
     * @param tube type of tube
     */
    public void addTube(String tubeName, Container tube) throws Exception {
        if (block.equals(BlockType.DECK)) {
            throw new Exception("Cannot add tube to deck");
        } else if (calcTubeType(tube).equals(ContainerType.TUBE) && !plateType.equals(ContainerType.TUBE)){
            throw new Exception("Cannot add Tube type to non-Tube type plate");
        } else if (calcTubeType(tube).equals(ContainerType.PCR) && !plateType.equals(ContainerType.PCR)) {
            throw new Exception("Cannot add PCR type to non-PCR type plate");
        }
        this.tubeName = tubeName;
        this.tube = tube;
        calcMaxVolume(tube);
    }

    /**
     * Remove the tube from the Well
     */
    public void removeTube() {
        this.tubeName = null;
        this.tube = null;
        this.tubeName = null;
        this.currentVolume = 0.0;
        this.maxVol = 0.0;
        this.contents = new HashMap<>();
        this.mixture = new HashMap<>();
    }

    /**
     * Calculates the maximum volume from a specified container before encountering overflow
     *
     * @param container to calculate maximum volume
     */
    private void calcMaxVolume(Container container) {
        if (tube.toString().contains("eppendorf") && plateType != ContainerType.TUBE) {
            throw new IllegalArgumentException("Cannot add tube to non-tube plate");
        }
        if (tube.toString().contains("pcr") && plateType != ContainerType.PCR) {
            throw new IllegalArgumentException("Cannot add pcr to non-pcr plate");
        }
        if (container == Container.eppendorf_1p5mL)
            this.maxVol = 1400.0;
        if (container == Container.eppendorf_2mL)
            this.maxVol = 1900.0;
        if (container == Container.pcr_tube)
            this.maxVol= 200.0;
        if (container == Container.pcr_strip)
            this.maxVol = 200.0 * 8;
        if (container == Container.pcr_plate_96)
            this.maxVol = 200.0 * 96; }
    /**
     * Calculates whether the tube will belong to tube or PCR type plates
     *
     * @param container whether container is tube or PCR type
     */
    private ContainerType calcTubeType(Container container) {
        if (container.toString().contains("eppendorf")) {
            return ContainerType.TUBE;
        } else if (container.toString().contains("pcr")) {
            return ContainerType.PCR;
        } else {
            throw new IllegalArgumentException("Cannot determine well type");
        }
    }

    /**
     * Helper method to determine whether a tube is situated in a well
     *
     * @return whether the Well contains a tube
     */
    public boolean isEmpty() {
        return tube == null;
    }

    /**
     * Helper method to determine whether a tube is at capacity or overflowing
     *
     * @return whether the tube is full
     */
    public boolean isFull() {
        return currentVolume >= maxVol;
    }


    /**
     * Add volume from a source to the Well
     *
     * @param source name of content being added
     * @param amount of volume to be added to the tube
     * @throws Exception no tube present in rack well or results in overflow
     */
    public void addVolume(String source, Double amount) throws Exception {
        if (tube == null && block.equals(BlockType.RACK)) {
            throw new Exception ("Cannot add " + amount + " uL of " + source + " to well since there is " +
                    "no container");
        } else if (currentVolume + amount > maxVol) {
            throw new Exception("New volume of " + amount + " exceeds maximum volume of container in " + source
            + " since new volume is " + (currentVolume + amount) + " and maximum volume is " + maxVol);
        } else {
            currentVolume += amount;
            mixture.put(source, amount/currentVolume);
            if (contents.containsKey(source)) {
                contents.replace(source, contents.get(source) + amount);
            } else {
                contents.put(source,amount);
            }
        }
    }

    /**
     * Remove volume from the Well
     *
     * @param amount of volume to be removed from the tube
     * @throws Exception no tube present in rack well or results in negative volume
     */
    public void removeVolume(Double amount) throws Exception {
        // Check for sufficient volume
        if (currentVolume - amount < 0.0) {
            throw new Exception("Removal of volume from " + tubeName + " results in negative value since " +
                    "volume to add is " + amount + " current volume is " + currentVolume);
        } else {
            currentVolume -= amount;
            for (String content : contents.keySet()) {
                contents.replace(content, mixture.get(content) * currentVolume);
            }
        }
    }

    /**
     * Helper method to get the position of a Well from a label
     *
     * @param location label
     * @return the position of the Well in the plate
     * @throws Exception cannot parse well label
     */
    public static Pair<Integer, Integer> parseWellLabel(String location)  throws Exception {
        String A1 = location;
        if(A1.contains("/")) {
            String[] splitted = A1.split("/");
            A1 = splitted[1];
        }
        //Figure out the row
        Integer row = null;
        try {
            String letters = A1.replaceAll("[0-9]+", "");
            char crow = letters.charAt(0);
            row = ((int) crow) - 65;
        } catch(Exception err) {
            throw new Exception("Cannot parse the well label row");
        }

        //Figure out the column
        Integer col = null;
        try {
            String numbers = A1.replaceAll("[A-Z]+", "");
            col  = Integer.parseInt(numbers) - 1;
        } catch(Exception err) {
            throw new Exception("Cannot parse the well label column");
        }

        return new Pair<>(row, col);
    }

    /**
     * Helper method to get the label from a specifiedposition
     *
     * @param position of Well in a plate
     * @return the well label
     */
    public static String calcWellLabel(Pair<Integer, Integer> position) {
        int col = position.getValue() + 1;
        int irow = 65 + position.getKey();
        char row = (char) irow;
        String out = "" + row + col;
        return out;
    }

    public String getTubeName() {
        return tubeName;
    }

    public Container getTube() {
        return tube;
    }

    public ContainerType getType() {
        return plateType;
    }

    public BlockType getBlock() {
        return block;
    }

    public double getVolume() {
        return currentVolume;
    }

    public double getMaxVolume() {
        return maxVol;
    }

    public HashMap getContents() {return contents; }

}
