package org.ucb.bio134.taskvisualizer.view;

import org.ucb.bio134.taskvisualizer.model.*;
import org.ucb.bio134.taskvisualizer.view.panels.BlockPanel;
import org.ucb.bio134.taskvisualizer.view.panels.PlatePanel;
import org.ucb.bio134.taskvisualizer.view.panels.WellPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View extends JFrame {
    private WellPanel[][] tubeRackWells;
    private WellPanel[][] pcrRackWells;
    private PlatePanel[][] platePanels;
    private JButton nextButton;
    private Color green;

    public static final int plateHeight = 200;
    public static final int plateWidth = 400;
    public static final int plateRows = 8;
    public static final int plateCols = 12;
    public static final int plateOffset = 1;
    public static final int wellHeight = plateHeight/plateRows;
    public static final int wellWidth = plateWidth/plateCols;
    public static final int windowOffset = 5;

    public View() {

    }
    public void initiate(){
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.GRAY);

        TubePlateConfig tubePlateConfig = new TubePlateConfig();
        PlatePanel tubeRack = new PlatePanel(ContainerType.TUBE);

        tubeRackWells = tubeRack.addWells(tubePlateConfig.getNumRows(),tubePlateConfig.getNumCols());
        for (int row = 0; row < tubePlateConfig.getNumRows(); row++) {
            for (int col = 0; col < tubePlateConfig.getNumCols(); col++) {
                getContentPane().add(tubeRackWells[row][col]);
            }
        }
        getContentPane().add(tubeRack);

        PCRPlateConfig pcrPlateConfig = new PCRPlateConfig();
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




        RackConfig rc = new RackConfig();
//        BlockPanel rackPanel = new BlockPanel("Rack Block", BlockType.RACK);
//        rackPanel.setBounds(windowOffset,windowOffset,rc.getWidth(),rc.getHeight());
//        getContentPane().add(rackPanel);
//        rackPanel.addPlate("Tube Rack", ContainerType.TUBE);

















        DeckConfig deckConfig = new DeckConfig();
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











//        BlockPanel deckPanel = new BlockPanel("DECK",BlockType.DECK);
//        deckPanel.setBounds(windowOffset * 2 + rc.getWidth(),windowOffset,dc.getWidth(),dc.getHeight());
//        getContentPane().add(deckPanel);


//        BlockPanel[][] rackPanel = new BlockPanel[2][1];
////        for (int row = 0; row < 2; row++) {
////            for (int col = 0; col < 1; col++) {
////                //Calculate the dimensions and positioning of the panel
////
////                int xpos = windowOffset + col * plateWidth;
////                int ypos = windowOffset + row * plateHeight;
////                //Create and add the panel
////                rackPanel[row][col] = new BlockPanel();
////
////                rackPanel[row][col].setBounds(xpos,ypos , plateWidth, wellHeight);
////                getContentPane().add(rackPanel[row][col]);
////            }
////        }



        nextButton = new JButton("Next");
        nextButton.setBounds(windowOffset,windowOffset * 3 + rc.getHeight(), wellWidth * 2, wellHeight);
        getContentPane().add(nextButton);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        pack();
        setVisible(true);





    }
    public JButton getNextBtn() {
        return nextButton;
    }

    public void colorWell(String containerName, Color color, int wellRow, int wellCol) throws Exception {
        if (containerName.equals("tubeRack")) {
            tubeRackWells[wellRow][wellCol].colorWell(color,wellRow,wellCol);
            return;
        }
        if (containerName.equals("pcrRack")) {
            pcrRackWells[wellRow][wellCol].colorWell(color,wellRow,wellCol);
        }
    }
    public void addPlate(String plateName, int row, int col) throws Exception {
        platePanels[row][col].addPlate(plateName);
    }
    public void colorWell(Color color, int plateRow, int plateCol, int wellRow, int wellCol) throws Exception {
//        platePanels[plateRow][plateCol].colorWell(color, wellRow, wellCol);
    }
}

