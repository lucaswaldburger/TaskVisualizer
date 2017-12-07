package org.ucb.bio134.taskvisualizer.view.panels;

import javafx.util.Pair;
import org.ucb.bio134.taskvisualizer.model.*;
import org.ucb.bio134.taskvisualizer.view.View;

import java.awt.*;
import java.util.HashSet;
import javax.swing.*;

/**
 * Displays the Plate in the workspace.
 *
 * @author J. Christopher Anderson
 * @author Lucas M. Waldburger
 */
public class PlatePanel extends JPanel {
    private JPanel currentDisplay;
    private Config config;
    private WellPanel[][] wells;
    private HashSet<Pair<Integer,Integer>> highlightedWells;

    /**
     * Constructs the Plate Panel
     *
     * @param type of Plate used to determine appropriate configuration
     */
    public PlatePanel(ContainerType type) {
        setLayout(new GridBagLayout());
        calcConfig(type);
        currentDisplay = createEmptyPosition();
        wells = new WellPanel[config.getNumRows()][config.getNumCols()];
        removePlate();
    }

    /**
     * Determines the appropriate configuration for the Plate Panel
     *
     * @param type of Plate
     */
    private void calcConfig(ContainerType type) {
        if (type == ContainerType.PCR) {
            this.config = PCRPlateConfig.getInstance();
        } else if (type == ContainerType.TUBE) {
            this.config = TubePlateConfig.getInstance();
        } else {
            throw new IllegalArgumentException("Invalid configuration type");
        }
    }

    /**
     * Initial Display of the Plate
     *
     * @return
     */
    private JPanel createEmptyPosition() {
        JPanel out = new JPanel();
        out.setBackground(new Color(160,160,160));
        JLabel label = new JLabel("Empty");
        label.setOpaque(false);
        label.setVerticalTextPosition(JLabel.CENTER);
        label.setHorizontalTextPosition(JLabel.CENTER);
        out.add(label);
        out.setBounds(0, 0, config.getWidth(), config.getHeight());
        return out;
    }

//    /**
//     *
//     * @param platename
//     * @return
//     */
//    private JPanel createAddPlatePanel(String platename) {
//        JPanel out = new JPanel();
//        out.setBackground(new Color(0,0,50));
//        JLabel label = new JLabel(platename);
//        label.setForeground(new Color(0,0,50));
//        out.add(label, BorderLayout.CENTER);
//        out.setBounds(0, 0, config.getWidth()/config.getNumCols(), config.getHeight()/config.getNumRows());
//        return out;
//    }

    /**
     * Adds Well Panels to the Plate Panel
     *
     * @return WellPanels of the Plate Panel
     */
    public WellPanel[][] addWells() {
        for (int row = 0; row < config.getNumRows(); row++) {
            for (int col = 0; col < config.getNumCols(); col++) {
                int xpos = View.windowOffset + col * config.getWellWidth();
                int ypos = View.windowOffset + row * config.getWellHeight();
                //Create and add the panel
                wells[row][col] = new WellPanel();
                wells[row][col].setBounds(xpos ,ypos , config.getWellWidth(), config.getWellHeight());


            }
        }
        return wells;
    }

    /**
     * Adds a black border to the Well Panel
     *
     * @param row of Well Panel within Plate Panel to add border
     * @param col of Well Panel within Plate Panel to add border
     */
    public void addBlackBorderToWell(int row, int col) {
        wells[row][col].addBlackBorder();
    }

    /**
     * Removes a black border to the Well Panel
     *
     * @param row of Well Panel within Plate Panel to remove border
     * @param col of Well Panel within Plate Panel to remove border
     */
    public void removeBlackBorderFromWell(int row, int col) {
        wells[row][col].removeBlackBorder();
    }

    /**
     * Returns all the Well Panels of a Plate Panel
     *
     * @return the Well Panels within a Plate Panel
     */
    public WellPanel[][] getWells() {
        return wells;
    }

    //VIEW-RELAYED COMMANDS//

//    public void reset() {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                removeAll();
//                setBorder(null);
//                add(currentDisplay);
//                revalidate();
//                repaint();
//            }
//        });
//    }
//
    /**
     * Removes Well Panel when Plate is taken out of the Deck
     *
     * @return the removed plate
     */
    public WellPanel[][] removeWells() {
        for (int row = 0; row < config.getNumRows(); row++) {
            for (int col = 0; col < config.getNumCols(); col++) {
                int xpos = View.windowOffset + col * config.getWellWidth();
                int ypos = View.windowOffset + row * config.getWellHeight();
                //Create and add the panel
                wells[row][col] = null;
            }
        }
        currentDisplay = createEmptyPosition();
        revalidate();
        repaint();
        return wells;
    }

    //Relay requested from View in response to a "removePlate" step
    public void removePlate() {
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
