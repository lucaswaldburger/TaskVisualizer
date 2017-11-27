package org.ucb.bio134.taskvisualizer.view;

import javafx.util.Pair;
import org.ucb.bio134.taskmaster.model.Tip;
import org.ucb.bio134.taskvisualizer.model.*;
import org.ucb.bio134.taskvisualizer.view.panels.*;
import org.ucb.c5.semiprotocol.model.Container;
import org.ucb.c5.semiprotocol.model.Reagent;
import org.ucb.c5.semiprotocol.model.Task;


import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 */
public class View extends JFrame {
    private WellPanel[][] tubeRackWells;
    private WellPanel[][] pcrRackWells;
    private PlatePanel[][] platePanels;
    private WellPanel[][][][] deckWells;
    private JButton nextButton;
    private JButton playButton;
    private JButton endButton;
    private JButton simulateButton;
    private NotificationPanel notifPanel;
    private PricePanel pricePanel;
    private BurdenPanel burdenPanel;
    private TaskPanel taskPanel;
    public static ContentPanel contentPanel;
    private PipettePanel pipettePanel;

    private HashSet<Pair<Integer,Integer>> highlightedTube;
    private HashSet<Pair<Integer,Integer>> highlightedPCR;


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
     * Initiates the view module by adding all components to the displayed window.
     */
    public void initiate(){
        highlightedTube = new HashSet<>();
        highlightedPCR = new HashSet<>();

        getContentPane().setLayout(null);
        getContentPane().setBackground(new Color(96,96,96));
        setTitle("TaskVisualizer");
        // Add tube rack to the display showing all tube wells determined by tubePlateConfig
        PlatePanel tubeRack = new PlatePanel(ContainerType.TUBE);
        tubeRackWells = tubeRack.addWells(tubePlateConfig.getNumRows(),tubePlateConfig.getNumCols(),ContainerType.TUBE);
        for (int row = 0; row < tubePlateConfig.getNumRows(); row++) {
            for (int col = 0; col < tubePlateConfig.getNumCols(); col++) {
                getContentPane().add(tubeRackWells[row][col]);
            }
        }
        getContentPane().add(tubeRack);
        // Add pcr rack to the display showing all pcr wells determined by pcrPlateConfig
        PlatePanel pcrRack = new PlatePanel(ContainerType.PCR);
        pcrRackWells = pcrRack.addWells(pcrPlateConfig.getNumRows(),pcrPlateConfig.getNumCols(), ContainerType.PCR);
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
                platePanels[row][col] = new PlatePanel(ContainerType.PCR);
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

        // Add simulate button to go through entire semiprotocol steps while considering the human burden for each step
        simulateButton = new JButton("Simulate");
        simulateButton.setBounds(windowOffset + wellWidth * 6,windowOffset * 3 + plateHeight * 2, wellWidth * 3, wellHeight);
        getContentPane().add(simulateButton);

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
        int cpH = plateHeight + 150 -wellHeight-windowOffset;
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
    public JButton getNextBtn() {
        return nextButton;
    }
    public JButton getPlayButton() {
        return playButton;
    }
    public JButton getEndButton() {
        return endButton;
    }
    public JButton getSimulateButton() {
        return simulateButton;
    }

    /**
     *
     * @param tubeName
     * @param tube
     * @param containerType
     * @param wellRow
     * @param wellCol
     * @throws Exception
     */
    public void addTubeToRack(String tubeName, Container tube, ContainerType containerType, int wellRow, int wellCol) throws Exception {
        if (containerType.equals(ContainerType.TUBE)) {
            tubeRackWells[wellRow][wellCol].addTubeColor(tubeName, tube);
        } else if (containerType.equals(ContainerType.PCR)) {
            pcrRackWells[wellRow][wellCol].addTubeColor(tubeName, tube);
        } else {
            throw new Exception("Cannot identify tube to be added to rack");
        }
    }

    /**
     *
     * @param tubeName
     * @param containerType
     * @param wellRow
     * @param wellCol
     * @throws Exception
     */
    public void removeTubeFromRack(String tubeName, ContainerType containerType, int wellRow, int wellCol) throws Exception {
        if (containerType.equals(ContainerType.TUBE)) {
            tubeRackWells[wellRow][wellCol].removeTubeColor(tubeName);
        } else if (containerType.equals(ContainerType.PCR)) {
            pcrRackWells[wellRow][wellCol].removeTubeColor(tubeName);
        } else {
            throw new Exception("Cannot identify tube to be removed to rack");
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

    public void highlightTransfer(ContainerType srcType, int srcRow, int srcCol, ContainerType dstType, int dstRow, int dstCol) throws Exception {
        if (srcType.equals(ContainerType.TUBE)) {
            highlightedTube.add(new Pair(srcRow,srcCol));
            tubeRackWells[srcRow][srcCol].highlightWell();
        } else if (srcType.equals(ContainerType.PCR)) {
            highlightedPCR.add(new Pair(srcRow,srcCol));
            pcrRackWells[srcRow][srcCol].highlightWell();
        } else {
            throw new Exception("Invalid rack element to add volume");
        }
        if (dstType.equals(ContainerType.TUBE)) {
            highlightedTube.add(new Pair(dstRow,dstCol));
            tubeRackWells[dstRow][dstCol].highlightWell();
        } else if (dstType.equals(ContainerType.PCR)) {
            highlightedPCR.add(new Pair(dstRow,dstCol));
            pcrRackWells[dstRow][dstCol].highlightWell();
        } else {
            throw new Exception("Invalid rack element to add volume");
        }
    }

    public void unhighlightTransfer() throws Exception {
        for (Pair<Integer,Integer> position : highlightedTube) {
            tubeRackWells[position.getKey()][position.getValue()].unhighlightWell();
        }
        for (Pair<Integer,Integer> position : highlightedPCR) {
            pcrRackWells[position.getKey()][position.getValue()].unhighlightWell();
        }
    }

    /**
     *
     * @param plateName
     * @param plateType
     * @param deckRow
     * @param deckCol
     * @throws Exception
     */
    public void addPlate(String plateName, ContainerType plateType, int deckRow, int deckCol) throws Exception {
//        platePanels[deckRow][deckCol].addPlate(plateName);
        deckWells[deckRow][deckCol] = platePanels[deckRow][deckCol].addWells(pcrPlateConfig.getNumRows(),pcrPlateConfig.getNumCols(), ContainerType.PCR);
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
     *
     * @param plateName
     * @param plateType
     * @param deckRow
     * @param deckCol
     * @throws Exception
     */
    public void removePlate(String plateName, ContainerType plateType, int deckRow, int deckCol) throws Exception {
        deckWells[deckRow][deckCol] = platePanels[deckRow][deckCol].removeWells(pcrPlateConfig.getNumRows(),pcrPlateConfig.getNumCols());
    }

    /**
     *
     * @param color
     * @param plateRow
     * @param plateCols
     * @param wellRow
     * @param wellCol
     */
//    public void highlightDeck(Color color, int plateRow, int plateCols, int wellRow, int wellCol) {
//        //TODO: TEST HIGHLIGHT DECK WELL FUNCTIONALITY
//        platePanels[plateRow][plateCols].getWells()[wellRow][wellCol].highlightWell(color, wellRow, wellCol);
//    }

    /**
     *
     * @param color
     * @param containerType
     * @param wellRow
     * @param wellCol
     */
    public void highlightRack(Color color, ContainerType containerType, int wellRow, int wellCol) {
        //TODO: TEST HIGHLIGHT RACK WELL FUNCTIONALITY
        if (containerType.equals(ContainerType.TUBE)) {
            tubeRackWells[wellRow][wellCol].highlightWell();
            return;
        }
        if (containerType.equals(ContainerType.TUBE)) {
            pcrRackWells[wellRow][wellCol].highlightWell();
        }
    }
//    public void colorWell(Color color, int plateRow, int plateCol, int wellRow, int wellCol) throws Exception {
////        platePanels[plateRow][plateCol].colorWell(color, wellRow, wellCol);
//    }

    /**
     *
     */
    public void endProtocol() {
//        reset();
        notifPanel.showComplete();
        taskPanel.showComplete();
    }
    public void notify(Task step) {
        notifPanel.notify(step);
    }
    public void currentTask(Task step) {taskPanel.notify(step); }
    public void updatePrice(double reagentTotal, double containerTotal, double tipTotal) {
        pricePanel.update(reagentTotal,containerTotal, tipTotal);
    }
    public void updateBurden(int burdenCount) {
        burdenPanel.update(burdenCount);
    }
    public void updatePipette(Tip tip) {
        pipettePanel.update(tip);
    }
    public void resetPipette() {
        pipettePanel.reset();
    }
}


