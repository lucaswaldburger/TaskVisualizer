package org.ucb.bio134.taskvisualizer.model;

import java.util.HashMap;
import java.util.Map;
import javafx.util.Pair;
import org.ucb.c5.semiprotocol.model.AddContainer;
import org.ucb.c5.semiprotocol.model.Container;
import sun.jvm.hotspot.opto.Block;

/**
 * Describes the Deck that is used to hold a configuration of plates in the workspace. For clarification,
 * Position refers to a Pair description of a row and column. Location refers to String notation as being
 * in column 1 through 12 and rows A through H (for a 96-well plate).
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
     * Constructs the Rack
     */
    public Rack() {
        TubePlateConfig tubePlateConfig = TubePlateConfig.getInstance();
        PCRPlateConfig pcrPlateConfig = PCRPlateConfig.getInstance();
        tubeNameToPos = new HashMap<>();
        pcrNameToPos = new HashMap<>();
        tubeRackWells = new Well[tubePlateConfig.getNumRows()][tubePlateConfig.getNumCols()];
        pcrRackWells = new Well[pcrPlateConfig.getNumRows()][pcrPlateConfig.getNumCols()];
    }
    //TODO: GET CONTAINER TYPE FROM CONTAINER, DON'T NEED BOTH IN SIGNATURE
    /**
     * Adds a tube to the rack
     *
     * @param container to be added to tube or PCR rack
     * @param row position in the tube or PCR rack
     * @param col position in the tube or PCR rack
     * @throws Exception
     */
    public void addTube(AddContainer container, int row, int col) throws Exception {
        String containerName = container.getName();
        ContainerType containerType = calcContainerType(container.getTubetype());


        if(tubeRackWells[row][col] != null && pcrRackWells[row][col] != null) {
            throw new Exception("Well does not exist");
        } else if (containerType.equals(ContainerType.TUBE)) {
            tubeRackWells[row][col] = new Well(containerType,BlockType.RACK,container.isNew());
            tubeRackWells[row][col].addTube(container);
            tubeNameToPos.put(containerName, new Pair<>(row, col));
        } else if (containerType.equals(ContainerType.PCR)) {
            tubeRackWells[row][col] = new Well(containerType,BlockType.RACK,container.isNew());
            pcrRackWells[row][col].addTube(container);
            pcrNameToPos.put(containerName, new Pair<>(row, col));
        } else {
            throw new Exception("Cannot locate tube to add to the rack");
        }
    }

    /**
     * Whether a tube is in the rack
     * @param tubeName tube name to be located
     * @return whether tube is present
     */
    public boolean containsTube(String tubeName) {
        if (tubeNameToPos.containsKey(tubeName)) {
            return tubeNameToPos.containsKey(tubeName);
        } else {
            return pcrNameToPos.containsKey(tubeName);
        }
    }

//    /**
//     *
//     * @param tubeName
//     * @param containerType
//     * @return
//     */
//    public boolean containsTube(String tubeName,ContainerType containerType) {
//        if (containerType.equals(ContainerType.TUBE)) {
//            return tubeNameToPos.containsKey(tubeName);
//        } else if (containerType.equals(ContainerType.PCR)) {
//            return pcrNameToPos.containsKey(tubeName);
//        } else {
//            return false;
//        }
//    }


    /**
     * Removes a tube from the associated rack
     *
     * @param tubeName name of the tube
     * @param containerType type of the tube
     * @throws Exception cannot locate the
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
            throw new Exception("Cannot locate tube to remove from the rack");
        }
    }

    /**
     * Removes a tube from the associated rack
     *
     * @param tubeName name of the tube
     * @throws Exception cannot locate tube
     */
    public void removeTube(Well tubeName) throws Exception{
        Pair<Integer,Integer> position;
        if (tubeNameToPos.containsKey(tubeName.getTubeName()) && tubeName.getType().equals(ContainerType.TUBE)) {
            position = tubeNameToPos.get(tubeName.getTubeName());
            tubeRackWells[position.getKey()][position.getValue()] = null;
        } else if (pcrNameToPos.containsKey(tubeName.getTubeName()) && tubeName.getType().equals(ContainerType.PCR)) {
            position = pcrNameToPos.get(tubeName.getTubeName());
            pcrRackWells[position.getKey()][position.getValue()] = null;
        } else {
            throw new Exception("Cannot locate tube to remove from rack");
        }
    }

    /**
     * Remove volume from a tube
     *
     * @param tubeName name of the tube
     * @param volume volume to be removed from the tube
     * @throws Exception
     */
    public void removeVolume(Well tubeName, double volume) throws Exception {
        Pair<Integer,Integer> position;
        if (tubeNameToPos.containsKey(tubeName.getTubeName()) && tubeName.getType().equals(ContainerType.TUBE)) {
            position = tubeNameToPos.get(tubeName.getTubeName());
            tubeRackWells[position.getKey()][position.getValue()].removeVolume(volume);
        } else if (pcrNameToPos.containsKey(tubeName.getTubeName()) && tubeName.getType().equals(ContainerType.PCR)) {
            position = pcrNameToPos.get(tubeName.getTubeName());
            pcrRackWells[position.getKey()][position.getValue()].removeVolume(volume);
        } else {
            throw new Exception("Cannot locate tube in rack to remove volume");
        }
    }

    /**
     * Locates the tube at a specified position in the tube or PCR rack
     *
     * @param containerType type of the container to be located
     * @param row in the tube or PCR rack
     * @param col in the tube or PCR rack
     * @return tube as a Well object
     * @throws Exception cannot locate the tube
     */
    public Well getTube (ContainerType containerType, int row, int col) throws Exception{
        if (containerType.equals(ContainerType.TUBE)){
            return tubeRackWells[row][col];
        } else if (containerType.equals(ContainerType.PCR)) {
            return pcrRackWells[row][col];
        } else {
            throw new Exception("Cannot locate tube to get from the rack");
        }
    }

    /**
     * Locates the tube with a specified name in the tube or PCR rack
     *
     * @param tubeName name of the tube to be located
     * @return tube as a Well object
     * @throws Exception cannot locate the tube
     */
    public Well getTube(String tubeName) throws Exception{
        Pair<Integer, Integer> position;
        if (tubeNameToPos.containsKey(tubeName)){
            position = tubeNameToPos.get(tubeName);
            return tubeRackWells[position.getKey()][position.getValue()];
        } else if (pcrNameToPos.containsKey(tubeName)) {
            position = pcrNameToPos.get(tubeName);
            return pcrRackWells[position.getKey()][position.getValue()];
        } else {
            throw new Exception("Cannot locate tube to get from the rack");
        }
    }

    /**
     * Gets the ContainerType of a specified tube name
     *
     * @param tubeName name of the tube
     * @return tube or PCR rack
     * @throws Exception cannot locate the tube
     */
    public ContainerType getRackType(String tubeName) throws Exception {
        if (tubeNameToPos.containsKey(tubeName)){
            return ContainerType.TUBE;
        } else if (pcrNameToPos.containsKey(tubeName)) {
            return ContainerType.PCR;
        } else {
            throw new Exception("Cannot locate tube to get from the rack");
        }
    }

    /**
     * Get the position of a specified tube name
     *
     * @param tubeName name of the tube
     * @return position in tube or PCR rack
     * @throws Exception cannot locate the tube
     */
    public Pair<Integer, Integer> getTubeLocation(String tubeName) throws Exception {
        if (tubeNameToPos.containsKey(tubeName)){
            return tubeNameToPos.get(tubeName);
        } else if (pcrNameToPos.containsKey(tubeName)) {
            return pcrNameToPos.get(tubeName);
        } else {
            throw new Exception("Cannot locate " + tubeName + " to get from the rack");
        }
    }

    /**
     * Get the position of a ContainerType and location label
     *
     * @param containerType of the tube
     * @param location of the tube
     * @return position in the tube or PCR rack
     * @throws Exception cannot locate the tube
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
            throw new Exception("Cannot locate tube at " + location + " to get from rack");
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
