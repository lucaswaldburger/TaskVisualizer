package org.ucb.bio134.taskvisualizer.model;

import java.util.HashMap;
import java.util.Map;
import javafx.util.Pair;
import org.ucb.c5.semiprotocol.model.Container;

/**
 *
 * @author J. Christopher Anderson
 * @author Lucas M. Waldburger
 */
public class Block {

    private final Plate[][] plates;
    private final Map<String, Pair<Integer,Integer>> nameToPos;
    private BlockType type;
    private Config config;

    public Block(BlockType type) {
        this.type = type;
        configBlock();
        plates = new Plate[config.getNumRows()][config.getNumCols()];
        nameToPos = new HashMap<>();
    }
    
    public void addPlate(String plateName, Container plate, int row, int col) throws Exception {
        if(plates[row][col] != null) {
            throw new Exception();
        }
        // Check type of plate and configure accordingly
        if (plate.toString().contains("pcr")) {
            plates[row][col] = new Plate(plateName, PCRPlateConfig.getInstance(),type);
        } else if  (plate.toString().contains("eppendorf")) {
            plates[row][col] = new Plate(plateName, TubePlateConfig.getInstance(),type);
        } else {
            throw new IllegalArgumentException("Invalid plate type added");
        }
        nameToPos.put(plateName, new Pair(row, col));
    }

    private void configBlock() {
        if (type.equals(BlockType.DECK)) {
            config = DeckConfig.getInstance();
        } else if (type.equals(BlockType.RACK)) {
            config = RackConfig.getInstance();
        } else {
            throw new IllegalArgumentException("Invalid block type");
        }
    }

    public void removePlate(int row, int col) {
        plates[row][col] = null;
    }

    public void removePlate(String plateName) {
        Pair<Integer,Integer> pos = nameToPos.get(plateName);
        removePlate(pos.getKey(), pos.getValue());
    }
    
    public Plate getPlate(int row, int col) {
        return plates[row][col];
    }

    public Pair<Integer, Integer> getPlatePos(String location) {
        String name = location;
        if(name.contains("/")) {
            String[] splitted = name.split("/");
            name = splitted[0];
        }
        return nameToPos.get(name);
    }

    /**
     * Helper method for location in the form of plate_name/A2
     * 
     * @param location plate_name/A2
     * @return the Well object in its Plate
     */
    public Well getWell(String location) throws Exception {
        String[] slitted = location.split("/");
        String plateName = slitted[0];
        Pair<Integer,Integer> blockPosition = nameToPos.get(plateName);
        Plate aplate = plates[blockPosition.getKey()][blockPosition.getValue()];

        Pair<Integer,Integer> A1 = Well.parseWellLabel(slitted[1]);
        return aplate.getWell(A1.getKey(), A1.getValue());
    }

    public static Pair<Integer, Integer> getPosition(String location) throws Exception {
        String A1 = location;
        if(A1.contains("/")) {
            String[] splitted = A1.split("/");
            A1 = splitted[1];
        }
        return Well.parseWellLabel(A1);
    }

}
