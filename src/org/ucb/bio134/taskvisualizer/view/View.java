package org.ucb.bio134.taskvisualizer.view;

import org.ucb.bio134.taskvisualizer.model.*;
import org.ucb.bio134.taskvisualizer.view.panels.*;
import org.ucb.c5.semiprotocol.model.Task;


import javax.swing.*;
import java.awt.*;


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
    private CountPanel countPanel;
    private BurdenPanel burdenPanel;
    private TaskPanel taskPanel;
    public static ContentPanel contentPanel;
    private PipettePanel pipettePanel;
    private Color green;

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
        getContentPane().setLayout(null);
        getContentPane().setBackground(new Color(96,96,96));
        setTitle("TaskVisualizer");
        // Add tube rack to the display showing all tube wells determined by tubePlateConfig
        PlatePanel tubeRack = new PlatePanel(ContainerType.TUBE);
        tubeRackWells = tubeRack.addWells(tubePlateConfig.getNumRows(),tubePlateConfig.getNumCols());
        for (int row = 0; row < tubePlateConfig.getNumRows(); row++) {
            for (int col = 0; col < tubePlateConfig.getNumCols(); col++) {
                getContentPane().add(tubeRackWells[row][col]);
            }
        }
        getContentPane().add(tubeRack);
        // Add pcr rack to the display showing all pcr wells determined by pcrPlateConfig
        PlatePanel pcrRack = new PlatePanel(ContainerType.PCR);
        pcrRackWells = pcrRack.addWells(pcrPlateConfig.getNumRows(),pcrPlateConfig.getNumCols());
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

        // Add the count panel to keep track of semiprotrocol price
        countPanel = new CountPanel();
        countPanel.setBounds(windowOffset * 2 + plateWidth,windowOffset * 3 + plateHeight * 2,plateWidth,plateHeight);
        getContentPane().add(countPanel);

        burdenPanel = new BurdenPanel();
        burdenPanel.setBounds(windowOffset * 3 + plateWidth * 2,windowOffset * 3 + plateHeight * 2 ,plateWidth,plateHeight);
        getContentPane().add(burdenPanel);

        contentPanel = new ContentPanel();
        int cpH = plateHeight + 150 -wellHeight-windowOffset;
        contentPanel.setBounds(windowOffset,windowOffset * 4 + plateHeight * 2 + wellHeight ,plateWidth,plateHeight + 150 -wellHeight-windowOffset);
        getContentPane().add(contentPanel);

        pipettePanel = new PipettePanel();
        pipettePanel.setBounds(windowOffset * 4 + plateWidth * 3 ,windowOffset * 3 + plateHeight * 2 ,plateHeight,plateHeight);
        getContentPane().add(pipettePanel);

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
     *
     * @param containerType
     * @param blockType
     * @param color
     * @param wellRow
     * @param wellCol
     * @throws Exception
     */
    public void colorWell(ContainerType containerType, BlockType blockType, Color color, int wellRow, int wellCol) throws Exception {
        if (containerType.equals(ContainerType.TUBE) && blockType.equals(BlockType.RACK)) {
            tubeRackWells[wellRow][wellCol].colorWell(color,wellRow,wellCol);
            return;
        }
        if (containerType.equals(ContainerType.PCR) && blockType.equals(BlockType.RACK)) {
            pcrRackWells[wellRow][wellCol].colorWell(color,wellRow,wellCol);
        }
    }
    public void updateWell(ContainerType containerType, BlockType blockType, Color color, int wellRow, int wellCol, int volume) throws Exception {
        if (containerType.equals(ContainerType.TUBE) && blockType.equals(BlockType.RACK)) {
            tubeRackWells[wellRow][wellCol].colorWell(color,wellRow,wellCol);
            return;
        }
        if (containerType.equals(ContainerType.PCR) && blockType.equals(BlockType.RACK)) {
            pcrRackWells[wellRow][wellCol].colorWell(color,wellRow,wellCol);
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
        deckWells[deckRow][deckCol] = platePanels[deckRow][deckCol].addWells(pcrPlateConfig.getNumRows(),pcrPlateConfig.getNumCols());
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
     * @param color
     * @param plateRow
     * @param plateCols
     * @param wellRow
     * @param wellCol
     */
    public void highlightDeck(Color color, int plateRow, int plateCols, int wellRow, int wellCol) {
        //TODO: TEST HIGHLIGHT DECK WELL FUNCTIONALITY
        platePanels[plateRow][plateCols].getWells()[wellRow][wellCol].highlightWell(color, wellRow, wellCol);
    }

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
            tubeRackWells[wellRow][wellCol].highlightWell(color, wellRow, wellCol);
            return;
        }
        if (containerType.equals(ContainerType.TUBE)) {
            pcrRackWells[wellRow][wellCol].highlightWell(color, wellRow, wellCol);
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

    /**
     *
     * @param step
     */
    public void notify(Task step) {
        notifPanel.notify(step);
    }
    public void currentTask(Task step) {taskPanel.notify(step); }

    /**
     *
     * @param reagentTotal
     * @param containerTotal
     * @param tipTotal
     */
    public void updateCounter(double reagentTotal, double containerTotal, double tipTotal) {
        countPanel.update(reagentTotal,containerTotal, tipTotal);
    }
    public void updateBurden(int burdenCount) {
        burdenPanel.update(burdenCount);
    }
}


