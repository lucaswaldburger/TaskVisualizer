package org.ucb.bio134.taskvisualizer.view.panels;

import org.ucb.bio134.taskvisualizer.model.*;
import org.ucb.bio134.taskvisualizer.view.View;

import javax.swing.*;
import java.awt.*;

public class BlockPanel extends JPanel {
    private JPanel currentDisplay;
    private Config config;
    private String blockName;

    /**
     *
     * @param blockName
     * @param blockType
     */
    public BlockPanel(String blockName, BlockType blockType) {
        this.blockName = blockName;
        setLayout(null);
        calcConfig(blockType);
        currentDisplay = createEmptyBlock();
        removePlate();
    }

    /**
     *
     * @param type
     */
    private void calcConfig(BlockType type) {
        if (type == BlockType.RACK) {
            this.config = RackConfig.getInstance();

        } else if (type == BlockType.DECK) {
            this.config = DeckConfig.getInstance();
        } else {
            throw new IllegalArgumentException("Invalid configuration type");
        }
    }

    /**
     *
     * @return
     */
    private JPanel createEmptyBlock() {
        JPanel out = new JPanel();
        out.setBackground(Color.WHITE);
        JLabel label = new JLabel(blockName);
        out.add(label, BorderLayout.CENTER);
        System.out.println(config);
        out.setBounds(0, 0, config.getWidth(), config.getHeight());
        return out;
    }

    /**
     *
     * @param plateName
     * @param type
     * @return
     */
    private JPanel createAddPlatePanel(String plateName, ContainerType type) {
        PlatePanel platePanel = new PlatePanel(type);
        platePanel.setBounds(0,0,View.plateWidth,View.plateHeight);
//        platePanel.setBackground(new Color(0,0,50));
//        JLabel label = new JLabel(plateName);
//        label.setForeground(Color.WHITE);
//        platePanel.add(label, BorderLayout.CENTER);
        platePanel.setBounds(0, 0, View.plateWidth, View.plateHeight);
        return platePanel;
    }

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

    /**
     *
     * @param plateName
     * @param type
     */
    public void addPlate(String plateName, ContainerType type) {
        currentDisplay = this.createAddPlatePanel(plateName, type);
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

    /**
     * Relay requested from View in response to an "removePlate" step
     */
    private void removePlate() {
        currentDisplay = this.createEmptyBlock();
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
