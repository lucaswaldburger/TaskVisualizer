package org.ucb.bio134.taskvisualizer.model.visualizer;

import java.util.HashMap;
import java.util.Map;
import javafx.util.Pair;

/**
 * Describes the Deck that is used to hold a configuration of plates in the workspace. For clarification,
 * Position refers to a Pair description of a row and column. Location refers to String notation as being
 * in column 1 through 12 and rows A through H (for a 96-well plate).
 *
 * @author J. Christopher Anderson
 * @author Lucas M. Waldburger
 */
public class Deck {

    private final Plate[][] plates;
    private final Map<String, Pair<Integer,Integer>> nameToPos;

    /**
     * Constructs the Deck object
     */
    public Deck() {
        DeckConfig config = DeckConfig.getInstance();
        plates = new Plate[config.getNumRows()][config.getNumCols()];
        nameToPos = new HashMap<>();
    }

    /**
     * Adds a plate to the Deck
     *
     * @param plateName plate name
     * @param row of the Deck
     * @param col of the Deck
     * @throws Exception plate at specified position does not exist
     */
    public void addPlate(String plateName, boolean isNew, int row, int col) throws Exception {
        if(plates[row][col] != null) {
            throw new Exception();
        }
        //String plateName, ContainerType plateType, Config config, BlockType block, boolean isNew
        plates[row][col] = new Plate(plateName,ContainerType.PCR,PCRPlateConfig.getInstance(),BlockType.DECK,isNew);
        nameToPos.put(plateName, new Pair<>(row, col));
    }

    /**
     * Removes a plate from the Deck based on a specified position
     *
     * @param row of Deck
     * @param col of Deck
     */
    public void removePlate(int row, int col) {
        plates[row][col] = null;
    }

    /**
     * Removes a plate from the Deck based on a specified plate name
     *
     * @param plateName name of the plate
     */
    public void removePlate(String plateName) {
        Pair<Integer,Integer> pos = nameToPos.get(plateName);
        removePlate(pos.getKey(), pos.getValue());
    }

    /**
     * Plate within the Deck based on a specified position
     *
     * @param row of the Deck
     * @param col of the Deck
     * @return the plate at the specified position
     */
    public Plate getPlate(int row, int col) {
        return plates[row][col];
    }

    public boolean containsPlate(String plateName) {
        return nameToPos.containsKey(plateName);
    }

    /**
     * Plate position based on a specified plate name
     *
     * @param plateName name of the plate
     * @return the position of the plate within the Deck
     * @throws Exception plate is not an element of the HashMap
     */
    public Pair<Integer,Integer> getPlate(String plateName) throws Exception{
        if (nameToPos.containsKey(plateName)) {
            return nameToPos.get(plateName);
        } else {
            throw new Exception("Cannot locate plate within the deck");
        }
    }

    /**
     * Plate position based on a specified location
     *
     * @param location plate_name/A2
     * @return the location of the Plate within the Deck
     */
    public Pair<Integer, Integer> getPlatePos(String location) throws Exception{
        String name = location;
        if(name.contains("/")) {
            String[] splitted = name.split("/");
            name = splitted[0];
        }
        if (nameToPos.containsKey(name)) {
            return nameToPos.get(name);
        } else {
            throw new Exception("Plate cannot be located within the deck");
        }
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
        Pair<Integer,Integer> deckrowcol = nameToPos.get(plateName);
        Plate aplate = plates[deckrowcol.getKey()][deckrowcol.getValue()];
        
        Pair<Integer,Integer> A1 = Well.parseWellLabel(slitted[1]);
        return aplate.getWell(A1.getKey(), A1.getValue());
    }

    /**
     * Helper method for position of the plate in the deck
     *
     * @param location plate_name/A2
     * @return the position of the Plate within the Deck
     * @throws Exception improper syntax of label
     */
    public static Pair<Integer, Integer> calcDeckPosition(String location) throws Exception {
        String A1 = location;
        if(A1.contains("/")) {
            String[] splitted = A1.split("/");
            A1 = splitted[1];
        }
        return Well.parseWellLabel(A1);
    }

    /**
     * Helper method for name of the plate
     *
     * @param location plate_name/A2
     * @return the plate name
     */
    public static String calcPlateName(String location) {
        String temp = location;
        if(temp.contains("/")) {
            String[] splitted = temp.split("/");
            temp = splitted[0];
        }
        return temp;
    }


}
