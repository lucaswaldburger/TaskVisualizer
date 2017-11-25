package org.ucb.bio134.taskvisualizer.view.panels;

import org.ucb.bio134.taskvisualizer.model.*;
import org.ucb.bio134.taskvisualizer.view.View;
import org.ucb.c5.semiprotocol.model.Container;

import java.awt.*;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

/**
 *
 * @author J. Christopher Anderson
 * @author Lucas M. Waldburger
 */
public class PlatePanel extends JPanel {
    private JPanel currentDisplay;
    private Config config;
    private WellPanel[][] wells;
    private Color emptyColor;
    private Color green;

    /**
     *
     * @param type
     */
    public PlatePanel(ContainerType type) {
        setLayout(new GridBagLayout());
        calcConfig(type);
        currentDisplay = createEmptyPosition();
        emptyColor = new Color(160,160,160);
        green = new Color(76,153,0);

        wells = new WellPanel[config.getNumRows()][config.getNumCols()];
        removePlate();
    }

    /**
     *
     * @param type
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

    /**
     *
     * @param platename
     * @return
     */
    private JPanel createAddPlatePanel(String platename) {
        JPanel out = new JPanel();
        out.setBackground(new Color(0,0,50));
        JLabel label = new JLabel(platename);
        label.setForeground(new Color(0,0,50));
        out.add(label, BorderLayout.CENTER);
        out.setBounds(0, 0, config.getWidth()/config.getNumCols(), config.getHeight()/config.getNumRows());
        return out;
    }

    /**
     *
     * @param rows
     * @param cols
     * @return
     */
    public WellPanel[][] addWells(int rows, int cols) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int xpos = View.windowOffset + col * config.getSubBlockWidth();
                int ypos = View.windowOffset + row * config.getSubBlockHeight();
                //Create and add the panel
                wells[row][col] = new WellPanel(emptyColor);
                wells[row][col].setBounds(xpos ,ypos , config.getSubBlockWidth(), config.getSubBlockHeight());


            }
        }
        return wells;
    }
    public WellPanel[][] getWells() {
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
