package org.ucb.bio134.taskvisualizer.model;

import javafx.util.Pair;
import org.ucb.c5.semiprotocol.model.Container;
import java.util.HashMap;
import java.util.Map;

public class Plate {
    private final String name;
    private final Well[][] wells;
    private final Map<String, Pair<Integer,Integer>> nameToPos;
    private final Map<Pair<Integer,Integer>, String> posToName;
    private final ContainerType type;
    private final BlockType block;

    Config config;

    public Plate(String plateName, Config config, BlockType block) {
        this.name = plateName;
        this.config = config;
        this.block = block;
        this.type = null;
        nameToPos = new HashMap<>();
        posToName = new HashMap<>();
        wells = new Well[config.getNumRows()][config.getNumCols()];
        for(int i=0; i<wells.length; i++) {
            for(int j=0; j<wells[i].length; j++) {
                wells[i][j] = new Well();
            }
        }
    }

    public void addTube(String containerName, Container container, int row, int col) throws Exception {
        if(wells[row][col] != null) {
            throw new Exception();
        }
        if (block.equals(BlockType.RACK)) {
            wells[row][col] = new Well(containerName, container, type);
        } else if (block.equals(BlockType.DECK)) {
            throw new IllegalArgumentException("Cannot add tube to deck");
        } else {
            throw new IllegalArgumentException("Invalid block type");
        }
        nameToPos.put(containerName, new Pair(row, col));
        posToName.put(new Pair(row, col), containerName);
    }

    public Well getWell(int row, int col) {
        return wells[row][col];
    }

    public String getName() {
        return name;
    }

    public Config getConfig() {
        return config;
    }

    public String getContainerName(int row, int col) {
        Pair<Integer,Integer> position = new Pair(row,col);
        if (!posToName.containsKey(position)) {
            throw new IllegalArgumentException("Invalid position provided by user");
        }
        return posToName.get(position);
    }

    public Pair<Integer,Integer> getPosition(String containerName) {
        if (!nameToPos.containsKey(containerName)) {
            throw new IllegalArgumentException("Invalid container name provided by user");
        }
        return nameToPos.get(containerName);
    }
}
