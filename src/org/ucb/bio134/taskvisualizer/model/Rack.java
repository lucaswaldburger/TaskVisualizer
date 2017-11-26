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
public class Rack {

    private final Well[][] tubeRackWells;
    private final Well[][] pcrRackWells;
    private final Map<String, Pair<Integer,Integer>> tubeNameToPos;
    private final Map<String, Pair<Integer,Integer>> pcrNameToPos;

    /**
     *
     */
    public Rack() {
        TubePlateConfig tubePlateConfig = TubePlateConfig.getInstance();
        PCRPlateConfig pcrPlateConfig = PCRPlateConfig.getInstance();
        tubeNameToPos = new HashMap<>();
        pcrNameToPos = new HashMap<>();
        tubeRackWells = new Well[tubePlateConfig.getNumRows()][tubePlateConfig.getNumCols()];
        pcrRackWells = new Well[pcrPlateConfig.getNumRows()][pcrPlateConfig.getNumCols()];
    }

    /**
     *
     * @param tubeName
     * @param containerType
     * @param container
     * @param row
     * @param col
     * @throws Exception
     */
    public void addTube(String tubeName, ContainerType containerType, Container container, int row, int col) throws Exception {
        if(tubeRackWells[row][col] != null && pcrRackWells[row][col] != null) {
            throw new Exception("Well does not exist");
        } else if (containerType.equals(ContainerType.TUBE)) {
            tubeRackWells[row][col] = new Well(tubeName, container, ContainerType.TUBE);
            tubeNameToPos.put(tubeName, new Pair(row, col));
        } else if (containerType.equals(ContainerType.PCR)) {
            pcrRackWells[row][col] = new Well(tubeName, container, ContainerType.PCR);
            pcrNameToPos.put(tubeName, new Pair(row, col));
        } else {
            throw new Exception("Cannot locate tube to add");
        }
    }

    /**
     *
     * @param containerType
     * @param tubeName
     * @return
     * @throws Exception
     */
    public boolean containsTube(ContainerType containerType, String tubeName) throws Exception {
        if (containerType.equals(ContainerType.TUBE)) {
            return tubeNameToPos.containsKey(tubeName);
        } else if (containerType.equals(ContainerType.PCR)) {
            return pcrNameToPos.containsKey(tubeName);
        } else {
            throw new Exception("Cannot locate tube to add");
        }
    }

    /**
     *
     * @param tubeName
     * @param containerType
     * @throws Exception
     */
    public void removeTube(String tubeName, ContainerType containerType) throws Exception{
        Pair<Integer,Integer> position;
        if (containerType.equals(ContainerType.TUBE) && tubeNameToPos.containsKey(tubeName)) {
            position = tubeNameToPos.get(tubeName);
            tubeRackWells[position.getKey()][position.getValue()] = null;
        } else if (containerType.equals(ContainerType.PCR)) {
            position = pcrNameToPos.get(tubeName);
            pcrRackWells[position.getKey()][position.getValue()] = null;
        } else {
            throw new Exception("Cannot locate tube to remove");
        }
    }

    /**
     *
     * @param containerType
     * @param row
     * @param col
     * @return
     * @throws Exception
     */
    public Well getTube (ContainerType containerType, int row, int col) throws Exception{
        if (containerType.equals(ContainerType.TUBE)){
            return tubeRackWells[row][col];
        } else if (containerType.equals(ContainerType.PCR)) {
            return pcrRackWells[row][col];
        } else {
            throw new Exception("Cannot locate tube to get");
        }
    }

    /**
     *
     * @param tubeName
     * @return
     * @throws Exception
     */
    public Well getTube (String tubeName) throws Exception{
        Pair<Integer, Integer> position;
        if (tubeNameToPos.containsKey(tubeName)){
            position = tubeNameToPos.get(tubeName);
            return tubeRackWells[position.getKey()][position.getValue()];
        } else if (pcrNameToPos.containsKey(tubeName)) {
            position = pcrNameToPos.get(tubeName);
            return pcrRackWells[position.getKey()][position.getValue()];
        } else {
            throw new Exception("Cannot locate tube to get");
        }
    }

    /**
     *
     * @param containerType
     * @param location
     * @return
     * @throws Exception
     */
    public Pair<Integer, Integer> getTubePos(ContainerType containerType, String location) throws Exception {
        String name = location;
        if(name.contains("/")) {
            String[] splitted = name.split("/");
            name = splitted[0];
        }
        if (containerType.equals(ContainerType.TUBE)){
            return tubeNameToPos.get(name);
        } else if (containerType.equals(ContainerType.PCR)) {
            return pcrNameToPos.get(name);
        } else {
            throw new Exception("Cannot locate tube to get");
        }
    }


    /**
     * Helper method for location in the form of plate_name/A2
     * 
     * @param location plate_name/A2
     * @return the Well object in its Plate
     */
    public static Pair<Integer, Integer> calcRackPos(String location) throws Exception {
        String A1 = location;
        if(A1.contains("/")) {
            String[] splitted = A1.split("/");
            A1 = splitted[1];
        }
        return Well.parseWellLabel(A1);
    }
}
