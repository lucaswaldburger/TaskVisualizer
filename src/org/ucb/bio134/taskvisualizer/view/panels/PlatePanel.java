package org.ucb.bio134.taskvisualizer.view.panels;

import org.ucb.bio134.taskvisualizer.model.*;
import org.ucb.bio134.taskvisualizer.view.View;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author J. Christopher Anderson
 */
public class PlatePanel extends JPanel {
    private JPanel currentDisplay;
    private Config config;
    private WellPanel[][] wells;
    private Color empty;
    private Color green;


    public PlatePanel(ContainerType type) {
        setLayout(null);
        calcConfig(type);
        currentDisplay = createEmptyPosition();
        empty = new Color(160,160,160);
        green = new Color(76,153,0);
        wells = new WellPanel[config.getNumRows()][config.getNumCols()];
        removePlate();
    }

    private void calcConfig(ContainerType type) {
        if (type == ContainerType.PCR) {
            this.config = PCRPlateConfig.getInstance();
        } else if (type == ContainerType.TUBE) {
            this.config = TubePlateConfig.getInstance();
        } else {
            throw new IllegalArgumentException("Invalid configuration type");
        }
    }

    private JPanel createEmptyPosition() {
        JPanel out = new JPanel();
        out.setBackground(Color.PINK);
        JLabel label = new JLabel("Empty");
        out.add(label, BorderLayout.CENTER);
        out.setBounds(0, 0, config.getWidth(), config.getHeight());
        return out;
    }

    private JPanel createAddPlatePanel(String platename) {
        JPanel out = new JPanel();
        out.setBackground(new Color(0,0,50));
        JLabel label = new JLabel(platename);
        label.setForeground(Color.WHITE);
        out.add(label, BorderLayout.CENTER);
        out.setBounds(0, 0, config.getWidth()/config.getNumCols(), config.getHeight()/config.getNumRows());
        return out;
    }

    public WellPanel[][] addWells(int rows, int cols) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int xpos = View.windowOffset + col * config.getSubBlockWidth();
                int ypos = View.windowOffset + row * config.getSubBlockHeight();
                //Create and add the panel
                wells[row][col] = new WellPanel(empty);
                wells[row][col].setBounds(xpos ,ypos , config.getSubBlockWidth(), config.getSubBlockHeight());
            }
        }
        return wells;
    }

//    private JPanel createBlackPanel() {
//        JPanel out = new JPanel();
//        out.setLayout(null);
//        out.setBackground(Color.BLACK);
//        out.setBounds(0, 0, config.getWidth(), config.getHeight());
//        return out;
//    }

//    private JPanel createWellPanel(Color color, int row, int col) {
//        JPanel out = new JPanel();
//        out.setBackground(color);
//
//        double posX = config.getXoffset() + (col-0.5) * config.getWellWidth();
//        double posy = config.getYoffset() + (row-0.5) * config.getWellHeight();
//
//        int width = DeckConfig.calcPixels(plateConfig.getWellWidth());
//        int height = DeckConfig.calcPixels(plateConfig.getWellHeight());
//
//        out.setBounds(DeckConfig.calcPixels(posX), DeckConfig.calcPixels(posy), width, height);
//        return out;
//    }

    //VIEW-RELAYED COMMANDS//

    public void reset() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                removeAll();
                setBorder(null);
                add(currentDisplay);
                revalidate();
                repaint();
            }
        });
    }

    public void addPlate(String plateName) {
        currentDisplay = this.createAddPlatePanel(plateName);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                removeAll();
                add(currentDisplay);
                revalidate();
                repaint();
            }
        });
    }

//    public void colorWell(Color color, int wellRow, int wellCol) {
//        JPanel blackPanel = createBlackPanel();
//        blackPanel.setBorder(BorderFactory.createLineBorder(color));
//        JPanel wellPanel = createWellPanel(color, wellRow, wellCol);
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                removeAll();
//                add(blackPanel);
//
//                blackPanel.add(wellPanel);
//                revalidate();
//                repaint();
//            }
//        });
//    }

    //Relay requested from View in response to an "removePlate" step
    private void removePlate() {
        currentDisplay = this.createEmptyPosition();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                removeAll();
                add(currentDisplay);
                revalidate();
                repaint();
            }
        });
    }


}
