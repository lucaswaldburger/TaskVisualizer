package org.ucb.bio134.taskvisualizer.view;

import javafx.util.Pair;
import org.ucb.bio134.taskmaster.model.Tip;
import org.ucb.bio134.taskvisualizer.model.visualizer.*;
import org.ucb.bio134.taskvisualizer.view.panels.*;
import org.ucb.c5.semiprotocol.model.AddContainer;
import org.ucb.c5.semiprotocol.model.Container;
import org.ucb.c5.semiprotocol.model.Task;


import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Describes the View for the TaskVisualizer.
 *
 * @author J. Christopher Anderson
 * @author Lucas M. Waldburger
 */
public class View extends JFrame {
    private WellPanel[][] tubeRackWells;
    private WellPanel[][] pcrRackWells;
    private PlatePanel[][] platePanels;
    private WellPanel[][][][] deckWells;

    private JButton nextButton;
    private JButton playButton;
    private JButton endButton;
    private JButton openButton;

    private NotificationPanel notifPanel;
    private PricePanel pricePanel;
    private BurdenPanel burdenPanel;
    private TaskPanel taskPanel;
    public static ContentPanel contentPanel;
    private PipettePanel pipettePanel;

    private HashSet<Pair<Integer,Integer>> highlightedTube;
    private HashSet<Pair<Integer,Integer>> highlightedPCR;
    private HashSet<Pair<Pair<Integer,Integer>,Pair<Integer,Integer>>> highlightedDeck;
    private HashSet<Pair<Integer,Integer>> coloredTube;
    private HashSet<Pair<Integer,Integer>> coloredPCR;

    public static final int plateHeight = 200;
    public static final int plateWidth = 400;
    public static final int plateRows = 8;
    public static final int plateCols = 12;
    public static final int wellHeight = plateHeight/plateRows;
    public static final int wellWidth = plateWidth/plateCols;

    public static final int windowOffset = 5;

    public static final DeckConfig deckConfig;
    public static final RackConfig rackConfig;
    public static final PCRPlateConfig pcrPlateConfig;
    public static final TubePlateConfig tubePlateConfig;

    static {
        deckConfig = DeckConfig.getInstance();
        rackConfig = RackConfig.getInstance();
        pcrPlateConfig = PCRPlateConfig.getInstance();
        tubePlateConfig = TubePlateConfig.getInstance();
    }

    public View() {
    }

    /**
     * Initiates the View by adding all components to the displayed window.
     */
    public void initiate(){
        highlightedTube = new HashSet<>();
        highlightedPCR = new HashSet<>();
        highlightedDeck = new HashSet<>();
        coloredTube = new HashSet<>();
        coloredPCR = new HashSet<>();


        getContentPane().setLayout(null);
        getContentPane().setBackground(new Color(96,96,96));
        setTitle("TaskVisualizer");
        // Add tube rack to the display showing all tube wells determined by tubePlateConfig
        PlatePanel tubeRack = new PlatePanel(ContainerType.TUBE, BlockType.RACK);
        tubeRackWells = tubeRack.addWells(true);
        for (int row = 0; row < tubePlateConfig.getNumRows(); row++) {
            for (int col = 0; col < tubePlateConfig.getNumCols(); col++) {
                getContentPane().add(tubeRackWells[row][col]);
            }
        }
        getContentPane().add(tubeRack);
        // Add pcr rack to the display showing all pcr wells determined by pcrPlateConfig
        PlatePanel pcrRack = new PlatePanel(ContainerType.PCR, BlockType.RACK);
        pcrRackWells = pcrRack.addWells(true);
        for (int row = 0; row < pcrPlateConfig.getNumRows(); row++) {
            for (int col = 0; col < pcrPlateConfig.getNumCols(); col++) {
                int xpos = windowOffset + wellWidth*col;
                int ypos = windowOffset * 2 + wellHeight*row + plateHeight;
                pcrRackWells[row][col].setBounds(xpos,ypos,wellWidth,wellHeight);
                getContentPane().add(pcrRackWells[row][col]);
            }
        }
        getContentPane().add(pcrRack);

        /* Add deck plates to the display. Default plate type is PCR (to handle TZ's implementation,
           but could be extended to handle TUBE plate type.
        */
        platePanels = new PlatePanel[deckConfig.getNumRows()][deckConfig.getNumCols()];
        for(int row=0; row<deckConfig.getNumRows(); row++) {
            for(int col=0; col<deckConfig.getNumCols(); col++) {
                //Calculate the dimensions and positioning of the panel
                int xpos = windowOffset * (col + 2) + (col+ 1) * plateWidth;
                int ypos = windowOffset * (row + 1) + row * plateHeight;
                //Create and add the panel
                platePanels[row][col] = new PlatePanel(ContainerType.PCR, BlockType.DECK);
                platePanels[row][col].setBounds(xpos, ypos, plateWidth, plateHeight);
                getContentPane().add(platePanels[row][col]);
            }
        }

        // Add wells to the deck plates, only visible when a corresponding plate is added
        deckWells = new WellPanel[deckConfig.getNumRows()][deckConfig.getNumCols()][pcrPlateConfig.getNumRows()][pcrPlateConfig.getNumCols()];

        // Add next button to go through semiprotocol tasks
        nextButton = new JButton("Next");
        nextButton.setBounds(windowOffset,windowOffset * 3 + plateHeight * 2, wellWidth * 3, wellHeight);
        getContentPane().add(nextButton);

        // Add play button to go through entire semiprotocol steps automatically
        playButton = new JButton("Play");
        playButton.setBounds(windowOffset + wellWidth * 3,windowOffset * 3 + plateHeight * 2, wellWidth * 3, wellHeight);
        getContentPane().add(playButton);

//        // Add simulate button to go through entire semiprotocol steps while considering the human burden for each step
//        simulateButton = new JButton("Simulate");
//        simulateButton.setBounds(windowOffset + wellWidth * 6,windowOffset * 3 + plateHeight * 2, wellWidth * 3, wellHeight);
//        getContentPane().add(simulateButton);

        //Add upload button to allow the user to select the semiprotocol they would like to visualize
        openButton = new JButton("Upload");
        openButton.setBounds(windowOffset + wellWidth * 6,windowOffset * 3 + plateHeight * 2, wellWidth * 3, wellHeight);
        getContentPane().add(openButton);

        // Add end button to go through entire semiprotocol steps and view the final state
        endButton = new JButton("End");
        endButton.setBounds(windowOffset + wellWidth * 9,windowOffset * 3 + plateHeight * 2, wellWidth * 3, wellHeight);
        getContentPane().add(endButton);

        // Add the notification panel to keep track of current task in semiprotocl
        notifPanel = new NotificationPanel();
        notifPanel.setBounds(windowOffset * 4 + plateWidth * 3, windowOffset, plateHeight, windowOffset + plateHeight * 2);
        getContentPane().add(notifPanel);

        // Add the price panel to keep track of semiprotrocol price
        pricePanel = new PricePanel();
        pricePanel.setBounds(windowOffset * 2 + plateWidth,windowOffset * 3 + plateHeight * 2,plateWidth,plateHeight);
        getContentPane().add(pricePanel);

        // Add the burden panel to keep track of human burden (waiting on TZ's implementation)
        burdenPanel = new BurdenPanel();
        burdenPanel.setBounds(windowOffset * 3 + plateWidth * 2,windowOffset * 3 + plateHeight * 2 ,plateWidth,plateHeight);
        getContentPane().add(burdenPanel);

        // Add the content panel to view the contents in a well when the user's mouse goes over it
        contentPanel = new ContentPanel();
        int cpH = plateHeight + 150 - wellHeight-windowOffset;
        contentPanel.setBounds(windowOffset,windowOffset * 4 + plateHeight * 2 + wellHeight ,plateWidth,plateHeight + 150 -wellHeight-windowOffset);
        getContentPane().add(contentPanel);

        // Add the pipette panel to display the pipette currently being used
        pipettePanel = new PipettePanel();
        pipettePanel.setBounds(windowOffset * 4 + plateWidth * 3 ,windowOffset * 3 + plateHeight * 2 ,plateHeight,plateHeight);
        getContentPane().add(pipettePanel);

        // Add the task panel to display the current task in the semiprotocol (useful for debugging)
        taskPanel = new TaskPanel();
        taskPanel.setBounds(windowOffset * 2 + plateWidth,windowOffset * 4 + plateHeight * 3 ,plateWidth * 2 + windowOffset * 2 + plateHeight,plateHeight + 150 - windowOffset - plateHeight);
        getContentPane().add(taskPanel);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);
        pack();
        setVisible(true);
    }
    public JButton getNextButton() {
        return nextButton;
    }
    public JButton getPlayButton() {
        return playButton;
    }
    public JButton getEndButton() {
        return endButton;
    }
//    public JButton getSimulateButton() {
//        return simulateButton;
//    }
    public JButton getOpenButton() {return openButton;}

    public TaskPanel getTaskPanel() {
        return taskPanel;
    }

    /**
     *  Adds a tube to the Rack Well Panel
     *
     * @param addcon either tube or pcr type of the tube being added
     * @param wellRow row of the Rack Well Panel
     * @param wellCol column of the Rack Well panel
     * @throws Exception error identifying tube to be added to the Rack
     */
    public void visualizeTube(AddContainer addcon, int wellRow, int wellCol) throws Exception {
        ContainerType containerType = calcContainerType(addcon.getTubetype());

        if (containerType.equals(ContainerType.TUBE)) {
            tubeRackWells[wellRow][wellCol].addTubeColor(addcon);
        } else if (containerType.equals(ContainerType.PCR)) {
            pcrRackWells[wellRow][wellCol].addTubeColor(addcon);
        } else {
            throw new Exception("Cannot identify tube to be added to rack");
        }
    }

    /**
     * Removes a tube from the Rack Well Panel
     *
     * @param tubeName name of the tube to be removed
     * @param containerType container type of the tube to be removed
     * @param wellRow row of the Rack
     * @param wellCol column of the Rack
     * @throws Exception error locating the tube to be removed from the Rack
     */
    public void removeTubeFromRack(String tubeName, ContainerType containerType, int wellRow, int wellCol) throws Exception {
        if (containerType.equals(ContainerType.TUBE)) {
            tubeRackWells[wellRow][wellCol].removeTubeColor();
        } else if (containerType.equals(ContainerType.PCR)) {
            pcrRackWells[wellRow][wellCol].removeTubeColor();
        } else {
            throw new Exception("Cannot locate tube to be removed to rack");
        }
    }

    /**
     *
     * @param source
     * @param volume
     * @param containerType
     * @param row
     * @param col
     * @throws Exception
     */
    public void addVolume(String source, double volume, ContainerType containerType, int row, int col) throws Exception{
        if (containerType.equals(ContainerType.TUBE)) {
            tubeRackWells[row][col].addVolume(source,volume);
        } else if (containerType.equals(ContainerType.PCR)) {
            pcrRackWells[row][col].addVolume(source,volume);
        } else {
            throw new Exception("Invalid rack element to add volume");
        }
    }
    public void removeVolume(double volume, ContainerType containerType, int row, int col) throws Exception{
        if (containerType.equals(ContainerType.TUBE)) {
            tubeRackWells[row][col].removeVolume(volume);
        } else if (containerType.equals(ContainerType.PCR)) {
            pcrRackWells[row][col].removeVolume(volume);
        } else {
            throw new Exception("Invalid rack element to add volume");
        }
    }

    /**
     * Adds a border to Well Panels involved in the current Transfer step of the Semiprotocol
     *
     * @param srcType container type of the source well
     * @param srcRow row of the source well
     * @param srcCol column of the source well
     * @param dstType container type of the destination well
     * @param dstRow row of the destination well
     * @param dstCol column of the destination well
     * @throws Exception
     */
    public void highlightRackTransfer(ContainerType srcType, int srcRow, int srcCol, ContainerType dstType, int dstRow, int dstCol) throws Exception {
        if (srcType.equals(ContainerType.TUBE)) {
            highlightedTube.add(new Pair<>(srcRow,srcCol));
            tubeRackWells[srcRow][srcCol].addBlackBorder();
        } else if (srcType.equals(ContainerType.PCR)) {
            highlightedPCR.add(new Pair<>(srcRow,srcCol));
            pcrRackWells[srcRow][srcCol].addBlackBorder();
        } else {
            throw new Exception("Invalid rack element to add volume");
        }
        if (dstType.equals(ContainerType.TUBE)) {
            highlightedTube.add(new Pair<>(dstRow,dstCol));
            tubeRackWells[dstRow][dstCol].addBlackBorder();
        } else if (dstType.equals(ContainerType.PCR)) {
            highlightedPCR.add(new Pair<>(dstRow,dstCol));
            pcrRackWells[dstRow][dstCol].addBlackBorder();
        } else {
            throw new Exception("Invalid rack element to add volume");
        }
    }
    public void highlightRackPos(ContainerType conType, int row, int col) throws Exception {
        if (conType.equals(ContainerType.TUBE)) {
            highlightedTube.add(new Pair<>(row,col));
            tubeRackWells[row][col].addBlackBorder();
        } else if (conType.equals(ContainerType.PCR)) {
            highlightedPCR.add(new Pair<>(row,col));
            pcrRackWells[row][col].addBlackBorder();
        } else {
            throw new Exception("Invalid rack element to add volume");
        }
    }

    /**
     * Removes a black border from the Well Panel of a corresponding Plate Panel
     *
     * @throws Exception error locating
     */
    public void removeBlackBorderFromWells() throws Exception {
        for (Pair<Integer,Integer> position : highlightedTube) {
            tubeRackWells[position.getKey()][position.getValue()].removeBlackBorder();
        }
        for (Pair<Integer,Integer> position : highlightedPCR) {
            pcrRackWells[position.getKey()][position.getValue()].removeBlackBorder();
        }
        for (Pair<Pair<Integer,Integer>,Pair<Integer,Integer>> position : highlightedDeck) {
            platePanels[position.getKey().getKey()][position.getKey().getValue()].removeBlackBorderFromWell(position.getValue().getKey(),
                    position.getValue().getValue());
        }


    }
    /**
     * Adds a black border to the Well Panel of a corresponding Plate Panel
     *
     * @param plateRow row of the Plate Panel containing the Well Panel
     * @param plateCol column of the Plate Panel containing the Well Panel
     * @param wellRow row of the Well Panel to add black border
     * @param wellCol column of the Well Panel to add black border
     * @throws Exception error locating Well Panel
     */
    public void addBlackBorderToWells(int plateRow, int plateCol, int wellRow, int wellCol) throws Exception {
        if(platePanels[plateRow][plateCol] == null) {
            throw new Exception("Error adding black border to well");
        }
        platePanels[plateRow][plateCol].addBlackBorderToWell(wellRow, wellCol);
        highlightedDeck.add(new Pair<>(new Pair<>(plateRow, plateCol),new Pair<>(wellRow, wellCol)));
    }
    /**
     * Add Plate Panel to the workspace
     *

     * @param deckRow row of the Plate Panel within the Deck
     * @param deckCol column of the Plate Panel within the Deck
     * @throws Exception error adding plate to the Deck
     */
    public void visualizePlate(boolean isNew, int deckRow, int deckCol) throws Exception {
        if (deckWells[deckRow][deckCol][0][0] != null) {
            throw new Exception("Error adding plate to the deck");
        }
        deckWells[deckRow][deckCol] = platePanels[deckRow][deckCol].addWells(isNew);
        for (int row = 0; row < pcrPlateConfig.getNumRows(); row++) {
            for (int col = 0; col < pcrPlateConfig.getNumCols(); col++) {
                int xpos = plateWidth + windowOffset * (deckCol + 2) + wellWidth*col + plateWidth * deckCol;
                int ypos = windowOffset * (deckRow + 1) + wellHeight*row + plateHeight * deckRow;
                deckWells[deckRow][deckCol][row][col].setBounds(xpos,ypos,wellWidth,wellHeight);
                getContentPane().add(deckWells[deckRow][deckCol][row][col]);
            }
        }
        getContentPane().add(platePanels[deckRow][deckCol]);
    }
    /**
     * Remove Plate Panel from the workspace
     *
     * @param plateName name of the plate
     * @param plateType plate type
     * @param deckRow row of the Plate Panel within the Deck
     * @param deckCol column of the Plate Panel within the Deck
     * @throws Exception error locating plate in Deck
     */
    public void removePlate(String plateName, ContainerType plateType, int deckRow, int deckCol) throws Exception {
        if (deckWells[deckRow][deckCol] == null) {
            throw new Exception("Cannot locate plate to remove from the deck");
        }
        deckWells[deckRow][deckCol] = platePanels[deckRow][deckCol].removeWells();
    }

//    /**
//     *
//     * @param color
//     * @param plateRow
//     * @param plateCols
//     * @param wellRow
//     * @param wellCol
//     */
//    public void highlightDeck(Color color, int plateRow, int plateCols, int wellRow, int wellCol) {
//        //TODO: TEST HIGHLIGHT DECK WELL FUNCTIONALITY
//        platePanels[plateRow][plateCols].getWells()[wellRow][wellCol].addBlackBorder(color, wellRow, wellCol);
//    }

//    /**
//     *
//     * @param color
//     * @param containerType
//     * @param wellRow
//     * @param wellCol
//     */
//    public void highlightRack(Color color, ContainerType containerType, int wellRow, int wellCol) {
//        //TODO: TEST HIGHLIGHT RACK WELL FUNCTIONALITY
//        if (containerType.equals(ContainerType.TUBE)) {
//            tubeRackWells[wellRow][wellCol].addBlackBorder();
//            return;
//        }
//        if (containerType.equals(ContainerType.PCR)) {
//            pcrRackWells[wellRow][wellCol].addBlackBorder();
//        }
//    }
//    public void addCyanBorder(Color color, int plateRow, int plateCol, int wellRow, int wellCol) throws Exception {
////        platePanels[plateRow][plateCol].addCyanBorder(color, wellRow, wellCol);
//    }
    /**
     * Adds a cyan border to the Well Panel of a corresponding Plate Panel
     *
     * @param containerType whether well is tube or pcr type
     * @param wellRow row of the Well Panel to add black border
     * @param wellCol column of the Well Panel to add black border
     * @throws Exception error locating Well Panel
     */
    public void addCyanBorderToWell(ContainerType containerType, int wellRow, int wellCol) throws Exception {
        if (containerType.equals(ContainerType.TUBE)) {
            tubeRackWells[wellRow][wellCol].addCyanBorder();
            coloredTube.add(new Pair<>(wellRow,wellCol));
        } else if (containerType.equals(ContainerType.PCR)) {
            pcrRackWells[wellRow][wellCol].addCyanBorder();
            coloredPCR.add(new Pair<>(wellRow,wellCol));
        } else {
            throw new Exception("cannot identify well to be colored");
        }
    }

    public void removeCyanBorderFromWell() throws Exception {
        for (Pair<Integer, Integer> positon : coloredTube) {
            tubeRackWells[positon.getKey()][positon.getValue()].removeCyanBorder();
        }
        for (Pair<Integer, Integer> positon : coloredTube) {
            pcrRackWells[positon.getKey()][positon.getValue()].removeCyanBorder();
        }
    }
    /**
     * Displays completion message and final state of the workspace when the end of the
     * Semiprotocol is reached
     */
    public void endProtocol() {
//        reset();
        notifPanel.showComplete();
        taskPanel.showComplete();
    }

    /**
     * Updates the Notification Panel
     *
     * @param step current Task in the Semiprotocol
     */
    public void notifyNotifPanel(Task step) {
        notifPanel.notify(step);
    }

    /**
     * Updates the Task Panel with the current Task in the Semiprotocol
     *
     * @param step current Task in the Semiprotocol
     */
    public void notifyTaskPanel(Task step) {taskPanel.notify(step); }

    /**
     * Updates the Price Panel with the current total price of the
     * Semiprotocol
     *
     * @param reagentTotal current reagent cost
     * @param containerTotal current container cost
     * @param tipTotal current tip cost
     */
    public void notifyPricePanel(double reagentTotal, double containerTotal, double tipTotal) {
        pricePanel.update(reagentTotal,containerTotal, tipTotal);
    }

    /**
     * Updates the Burden Panel with the current total human burden
     * of the Semiprotocol
     *
     * @param burdenCount the current human burden
     */
    public void updateBurden(int burdenCount) {
        burdenPanel.update(burdenCount);
    }

    /**
     * Updates the Pipette Panel when the current Task is a Dispense
     * or Transfer step
     *
     * @param tip of corresponding pipette being used
     */
    public void updatePipette(Tip tip) {
        pipettePanel.update(tip);
    }

    public static void updateContent(HashMap<String,Double> wellContents) {
        contentPanel.getContentsFromWell(wellContents);
    }

    /**
     * Resets the Pipette Panel when the current Task is not a Dispense
     * or Transfer step
     */
    public void resetPipette() {
        pipettePanel.reset();
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


