package org.ucb.bio134.taskvisualizer.model;

//public class Well {
//    String name;
//    double MAX_VOLUME;
//    double currentVolume;
//
//    // CONSTRUCTORS
//    public Well() {
//        this.name = "None";;
//        this.MAX_VOLUME = 200.0;
//        this.currentVolume = 0.0;
//    }
//    public Well(String name) {
//        this.name = name;;
//        this.currentVolume = 0.0;
//
//    }
//    public Well(String name, double max_vol) {
//        this.name = name;
//        this.MAX_VOLUME = max_vol;
//        this.currentVolume = 0.0;
//    }
//    // WELL FUNCTIONS
//    public void fillWell(double volume) {
//        if (volume + currentVolume > MAX_VOLUME) {
//            throw new IllegalArgumentException("Volume exceeds maximum well capacity");
//        } else {
//            currentVolume += volume;
//        }
//    }
//    public void emptyWell(double volume) {
//        if (currentVolume - volume < 0.0) {
//            throw new IllegalArgumentException("Invalid final well volume");
//        } else {
//            currentVolume -= volume;
//        }
//    }
//    // GETTERS
//    public double getWellVol() {
//        return currentVolume;
//    }
//    public String getWellName() {
//        return name;
//    }
//    public double getMaxVol() {
//        return MAX_VOLUME;
//    }
//}

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
    private Container tube;
    private String tubeName;
    private double maxVol;
    private WellType type;

    public Well() {
        this.currentVolume = 0.0;
        this.maxVol = 0.0;
        this.tube = null;
        this.tubeName = null;
        this.type = null;
        this.contents = new HashMap<>();
    }
    public Well(String containerName, Container container) {
        this.tube = container;
        this.tubeName = containerName;
        calcMaxVolume(container);
        calcType();
        this.currentVolume = 0.0;
        this.contents = new HashMap<>();
    }
    public void calcMaxVolume(Container container) {
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
    private void calcType() {
        if (this.tube.toString().contains("eppendorf")) {
            this.type = WellType.TUBE;
        } else if (this.tube.toString().contains("pcr")) {
            this.type = WellType.PCR;
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
    public void addTube(Container tube, double maxVolume) {
        if (tube.toString().contains("eppendorf") && this.type != WellType.TUBE) {
            throw new IllegalArgumentException("Cannot add tube to non-tube plate");
        }
        if (tube.toString().contains("pcr") && this.type != WellType.PCR) {
            throw new IllegalArgumentException("Cannot add pcr to non-pcr plate");
        }
        this.tube = tube;
        this.maxVol = maxVolume;
    }
    public double getVolume() {
        return currentVolume;
    }
    public double getMaxVolume() {
        return maxVol;
    }
    public void addVolume(String reagent, Double amount) throws Exception {
        //Check if the well has enough space for the additional volume
        if (currentVolume + amount > maxVol) {
            throw new IllegalArgumentException("New volume exceeds maximum volume of container in well");
        } else {
            currentVolume += amount;
            if (contents.containsKey(reagent)) {
                contents.replace(reagent, contents.get(reagent) + amount);
            } else {
                contents.put(reagent,amount);
            }
        }
    }
    public HashMap getContents() {return contents; }
    public void removeVolume(String reagent, Double amount) throws Exception {
        // Check for sufficient volume
        if (currentVolume - amount < 0.0) {
            throw new IllegalArgumentException("Removal of volume results in negative value");
        } else {
            currentVolume -= amount;
            if (contents.containsKey(reagent)) {
                contents.replace(reagent, contents.get(reagent) + amount);
            } else {
                contents.put(reagent,amount);
            }
        }
        //TODO in future:  adjust composition of the well
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
