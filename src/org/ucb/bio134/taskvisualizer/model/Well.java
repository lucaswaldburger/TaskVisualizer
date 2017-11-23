package org.ucb.bio134.taskvisualizer.model;

import javafx.util.Pair;
import org.ucb.c5.semiprotocol.model.Container;
import java.util.HashMap;

/**
 *
 * @author J. Christopher Anderson
 * @author Lucas M. Waldburger
 */
public class Well {

    private final HashMap<String, Double> contents;
    private Double currentVolume;
    private String tubeName;
    private Container tube;
    private double maxVol;
    private ContainerType plateType;

    public Well() {
        this.tubeName = null;
        this.currentVolume = 0.0;
        this.maxVol = 0.0;
        this.tube = null;
        this.contents = new HashMap<>();
    }

    public Well(String tubeName, Container tube, ContainerType plateType) {
        this.tubeName = tubeName;
        this.tube = tube;
        calcMaxVolume(tube);
        calcType(plateType);
        this.currentVolume = 0.0;
        this.maxVol = 0.0;
        this.contents = new HashMap<>();
    }

    public void calcMaxVolume(Container container) {
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
            this.maxVol = 200.0 * 96;
    }

    private void calcType(ContainerType plateType) {
        if (plateType.toString().contains("eppendorf")) {
            this.plateType = ContainerType.TUBE;
        } else if (plateType.toString().contains("pcr")) {
            this.plateType = ContainerType.PCR;
        } else {
            throw new IllegalArgumentException("Cannot determine well type");
        }
    }

    public boolean isEmpty() {
        if (tube == null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isFull() {
        if (currentVolume == maxVol) {
            return true;
        } else {
            return false;
        }
    }

    public Container getTube() {
        return tube;
    }

    public double getVolume() {
        return currentVolume;
    }

    public double getMaxVolume() {
        return maxVol;
    }

    public void addVolume(String source, Double amount) throws Exception {
        //Check if the well has enough space for the additional volume
        if (currentVolume + amount > maxVol) {
            throw new IllegalArgumentException("New volume exceeds maximum volume of container in well");
        } else {
            currentVolume += amount;
            if (contents.containsKey(source)) {
                contents.replace(source, contents.get(source) + amount);
            } else {
                contents.put(source,amount);
            }
        }
    }

    public HashMap getContents() {return contents; }

    public void removeVolume(String source, Double amount) throws Exception {
        // Check for sufficient volume
        if (currentVolume - amount < 0.0) {
            throw new IllegalArgumentException("Removal of volume results in negative value");
        } else {
            currentVolume -= amount;
            if (contents.containsKey(source)) {
                contents.replace(source, contents.get(source) + amount);
            } else {
                contents.put(source,amount);
            }
        }
    }

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
        } catch(Exception err) {}

        //Figure out the column
        Integer col = null;
        try {
            String numbers = A1.replaceAll("[A-Z]+", "");
            col  = Integer.parseInt(numbers) - 1;
        } catch(Exception err) {}

        return new Pair(row, col);
    }

    public static String calcWellLabel(Pair<Integer, Integer> srcWell) {
        int col = srcWell.getValue() + 1;
        int irow = 65 + srcWell.getKey();
        char row = (char) irow;
        String out = "" + row + col;
        return out;
    }
}
