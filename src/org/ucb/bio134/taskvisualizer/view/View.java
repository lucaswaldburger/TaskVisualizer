package org.ucb.bio134.taskvisualizer.view;

import javax.swing.*;
import java.awt.*;

public class View extends JFrame {
    private WellPanel[][] tubeRackPanel;
    private JButton nextButton;





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

//        // Add stuff


        nextButton = new JButton("Next");
        nextButton.setBounds(windowOffset,windowOffset + plateHeight, wellWidth * 2, wellHeight);
        getContentPane().add(nextButton);


        tubeRackPanel = new WellPanel[plateRows][plateCols];
        for (int row = 0; row < plateRows; row++) {
            for (int col = 0; col < plateCols; col++) {
                //Calculate the dimensions and positioning of the panel

                int xpos = windowOffset + col * wellWidth;
                int ypos = windowOffset + row * wellHeight;
                //Create and add the panel
                tubeRackPanel[row][col] = new WellPanel();

                tubeRackPanel[row][col].setBounds(xpos,ypos , wellWidth, wellHeight);
                getContentPane().add(tubeRackPanel[row][col]);
            }
        }
        // Positions the tube rack block
//        BlockPanel tubeRack = new BlockPanel();
//        tubeRack.setBounds(windowOffset, windowOffset, plateWidth, plateHeight);
//        getContentPane().add(tubeRack);







        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        pack();
        setVisible(true);




    }
    public JButton getNextBtn() {
        return nextButton;
    }

    public void colorWell(Color color, int wellRow, int wellCol) throws Exception {
        tubeRackPanel[wellRow][wellCol].colorWell(color,wellRow,wellCol);
    }
    public void colorWell(Color color, int plateRow, int plateCol, int wellRow, int wellCol) throws Exception {
//        platePanels[plateRow][plateCol].colorWell(color, wellRow, wellCol);
    }
}
