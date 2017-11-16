package org.ucb.bio134.taskvisualizer.model;

import javafx.util.Pair;
import org.ucb.c5.semiprotocol.model.Container;

import java.util.HashMap;
import java.util.Map;

//public class Plate {
//
//    int rows;
//    int cols;
//    String name;
//    ArrayList<String> wells;
//    HashMap<String,String> wellToName;
//    HashMap<String,String> nameToWell;
//
//
//    public Plate(String name) {
//        this.rows = 8;
//        this.cols = 12;
//        this.name = name;
//        wells = new ArrayList<>();
//        wellToName = new HashMap<>();
//        nameToWell = new HashMap<>();
//        String[] rowLetter = {"A","B","C","D","E","F","G","H"};
//        for (String row: rowLetter){
//            for (int i = 1; i <= cols; i++) {
//                wells.add(row+i);
//                wellToName.put(row+i,null);
//            }
//        }
//    }
//}
public class Plate {
    private final String name;
    private final Well[][] wells;
    private final Map<String, Pair<Integer,Integer>> nameToPos;
    private final PlateType type;

    BlockConfig config;

    public Plate(String name, BlockConfig config) {
        this.name = name;
        this.config = config;
        this.type = null;

        nameToPos = new HashMap<>();
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
    }

    public Well getWell(int row, int col) {
        return wells[row][col];
    }

    public String getName() {
        return name;
    }

    public BlockConfig getConfig() {
        return config;
    }
}
