package org.ucb.bio134.taskvisualizer.view;

import org.ucb.bio134.taskvisualizer.model.BlockConfig;

import javax.swing.*;
import java.awt.*;

public class BlockPanel extends JPanel {
    private JPanel currentDisplay;
    private BlockConfig plateConfig;

    public BlockPanel() {
        setLayout(null);
        currentDisplay = createEmptyBlock();
        removePlate();
    }

    private JPanel createEmptyBlock() {
        JPanel out = new JPanel();
        out.setBackground(Color.BLACK);
        JLabel label = new JLabel("BLOCK");
        out.add(label, BorderLayout.CENTER);
        out.setBounds(0, 0, View.plateWidth, View.plateHeight);
        return out;
    }
    private JPanel createAddBlockPanel(String platename) {
        JPanel out = new JPanel();
        out.setBackground(new Color(0,0,50));
        JLabel label = new JLabel(platename);
        label.setForeground(Color.WHITE);
        out.add(label, BorderLayout.CENTER);
        out.setBounds(0, 0, View.plateWidth, View.plateHeight);
        return out;
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

    public void addPlate(String plateName) {
        currentDisplay = this.createAddBlockPanel(plateName);
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
    //Relay requested from View in response to an "removePlate" step
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
