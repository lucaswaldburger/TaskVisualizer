package org.ucb.bio134.taskvisualizer.model;

import javafx.util.Pair;
import org.ucb.c5.semiprotocol.model.AddContainer;
import org.ucb.c5.semiprotocol.model.Container;
import java.util.HashMap;
import java.util.Map;

/**
 * Describes the plate that is operated on in the workspace in a sequence defined by a Semiprotocol
 *
 * @author Lucas M. Waldburger
 * @author J. Christopher Anderson
 */
public class Plate {
    private final String name;
    private final Well[][] wells;
    private final Map<String, Pair<Integer,Integer>> nameToPos;
    private final Map<Pair<Integer,Integer>, String> posToName;
    private final ContainerType type;
    private final BlockType block;
    private boolean isNew;

    Config config;

    /**
     * Constructs the Plate object
     *
     * @param plateName name of the plate
     * @param plateType specifies the type of plate
     * @param config specifies the plate configuration
     * @param block whether the plate belongs to the rack or deck
     */
    public Plate(String plateName, ContainerType plateType, Config config, BlockType block, boolean isNew) {
        this.name = plateName;
        this.config = config;
        this.block = block;
        this.type = plateType;
        this.isNew = isNew;
        nameToPos = new HashMap<>();
        posToName = new HashMap<>();
        wells = new Well[config.getNumRows()][config.getNumCols()];
        for(int i=0; i<wells.length; i++) {
            for(int j=0; j<wells[i].length; j++) {
                wells[i][j] = new Well(plateType, block, isNew);
            }
        }
    }

    /**
     * Adds a tube to the plate only if it is an element of the rack since the deck consists of PCR plates
     * in this version.
     *
     * @param container specifies the type of tube
     * @param row of tube in plate
     * @param col of tube in plate
     * @throws Exception invalid well, well does not exist, or adding a container to the deck
     */
    public void addTube(AddContainer container, int row, int col) throws Exception {
        if(wells[row][col] != null) {
            throw new Exception("Well does not exist, possibly out of bounds");
        } else if (block.equals(BlockType.RACK)) {
            wells[row][col].addTube(container);
        } else if (block.equals(BlockType.DECK)) {
            throw new IllegalArgumentException("Cannot add tube to deck");
        } else {
            throw new IllegalArgumentException("Invalid block type");
        }
        nameToPos.put(container.getName(), new Pair<>(row, col));
        posToName.put(new Pair<>(row, col), container.getName());
    }

    /**
     * Helper method for name of tube at a specified position
     *
     * @param row of tube in plate
     * @param col of tube in plate
     * @return
     */
    public String getContainerName(int row, int col) {
        Pair<Integer,Integer> position = new Pair<>(row,col);
        if (!posToName.containsKey(position)) {
            throw new IllegalArgumentException("Invalid position provided by user");
        }
        return posToName.get(position);
    }

    /**
     * Helper method for position of a tube within a Plate
     *
     * @param containerName name of the container
     * @return position of tube within the Plate
     */
    public Pair<Integer,Integer> getPosition(String containerName) {
        if (nameToPos.containsKey(containerName)) {
            return nameToPos.get(containerName);
        } else {
            throw new IllegalArgumentException("Invalid container name provided by user");
        }
    }


    public Well getWell(int row, int col) {
        return wells[row][col];
    }

    public boolean isNew() {
        return isNew;
    }

    public ContainerType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Config getConfig() {
        return config;
    }
}
