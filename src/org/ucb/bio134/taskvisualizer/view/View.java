package org.ucb.bio134.taskvisualizer.view;

import org.ucb.bio134.taskvisualizer.model.*;
import org.ucb.bio134.taskvisualizer.view.panels.NotificationPanel;
import org.ucb.bio134.taskvisualizer.view.panels.PlatePanel;
import org.ucb.bio134.taskvisualizer.view.panels.WellPanel;
import org.ucb.c5.semiprotocol.model.Task;
import org.ucb.c5.utils.FileUtils;
import com.apple.eawt.Application;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;


public class View extends JFrame {
    private WellPanel[][] tubeRackWells;
    private WellPanel[][] pcrRackWells;
    private PlatePanel[][] platePanels;
    private WellPanel[][][][] deckWells;
    private JButton nextButton;
    private JButton playButton;
    private NotificationPanel notifPanel;
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

    void addCompForTitledBorder(TitledBorder border,
                                String description,
                                int justification,
                                int position,
                                Container container) {
        border.setTitleJustification(justification);
        border.setTitlePosition(position);
        addCompForBorder(border, description,
                container);
    }

    void addCompForBorder(Border border,
                          String description,
                          Container container) {
        JPanel comp = new JPanel(new GridLayout(1, 1), false);
        JLabel label = new JLabel(description, JLabel.CENTER);
        comp.add(label);
        comp.setBorder(border);

        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(comp);
    }

    public void initiate(){
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.GRAY);
        setTitle("TaskVisualizer");










        PlatePanel tubeRack = new PlatePanel(ContainerType.TUBE);
        tubeRackWells = tubeRack.addWells(tubePlateConfig.getNumRows(),tubePlateConfig.getNumCols());
        for (int row = 0; row < tubePlateConfig.getNumRows(); row++) {
            for (int col = 0; col < tubePlateConfig.getNumCols(); col++) {
                getContentPane().add(tubeRackWells[row][col]);
            }
        }
        getContentPane().add(tubeRack);




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

        deckWells = new WellPanel[deckConfig.getNumRows()][deckConfig.getNumCols()][pcrPlateConfig.getNumRows()][pcrPlateConfig.getNumCols()];

        nextButton = new JButton("Next");
        nextButton.setBounds(windowOffset,windowOffset * 3 + plateHeight * 2, wellWidth * 2, wellHeight);
        getContentPane().add(nextButton);

        playButton = new JButton("Play");
        playButton.setBounds(windowOffset + wellWidth * 2,windowOffset * 3 + plateHeight * 2, wellWidth * 2, wellHeight);
        getContentPane().add(playButton);

        //Add the notification panel
        notifPanel = new NotificationPanel();
        notifPanel.setBounds(windowOffset * 4 + plateWidth * 3, windowOffset, 200, windowOffset + plateHeight * 2);
        getContentPane().add(notifPanel);

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
    public void colorWell(ContainerType containerType, BlockType blockType, Color color, int wellRow, int wellCol) throws Exception {
        if (containerType.equals(ContainerType.TUBE) && blockType.equals(BlockType.RACK)) {
            tubeRackWells[wellRow][wellCol].colorWell(color,wellRow,wellCol);
            return;
        }
        if (containerType.equals(ContainerType.PCR) && blockType.equals(BlockType.RACK)) {
            pcrRackWells[wellRow][wellCol].colorWell(color,wellRow,wellCol);
        }
    }
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
    public void highlightWell(ContainerType containerType, BlockType blockType, Color color, int wellRow, int wellCol) {
        if (containerType.equals(ContainerType.TUBE) && blockType.equals(BlockType.RACK)) {
            tubeRackWells[wellRow][wellCol].highlightWell(color,wellRow,wellCol);
            return;
        }
        if (containerType.equals(ContainerType.TUBE) && blockType.equals(BlockType.RACK)) {
            pcrRackWells[wellRow][wellCol].highlightWell(color,wellRow,wellCol);
        }
    }

    public void colorWell(Color color, int plateRow, int plateCol, int wellRow, int wellCol) throws Exception {
//        platePanels[plateRow][plateCol].colorWell(color, wellRow, wellCol);
    }

    public void endProtocol() {
//        resetAll();
        notifPanel.showComplete();
    }

    public void notify(Task step) {
        notifPanel.notify(step);
    }

}

