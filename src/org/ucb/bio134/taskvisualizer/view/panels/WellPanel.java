package org.ucb.bio134.taskvisualizer.view.panels;

import org.ucb.bio134.taskvisualizer.model.Config;
import org.ucb.bio134.taskvisualizer.view.View;

import javax.swing.*;
import java.awt.*;

public class WellPanel extends JPanel {
    private JPanel currentDisplay;
    private Color currentColor;
    private Config plateConfig;

    public WellPanel(Color color) {
        setLayout(null);
        currentDisplay = createEmptyWell();
        currentColor = color;
        removeWell();
    }
    private JPanel createEmptyWell() {
        JPanel out = new JPanel();
//        out.setBackground(Color.WHITE);
//        JLabel label = new JLabel("WELL");
//        out.add(label, BorderLayout.CENTER);
//        out.setBounds(0, 0, View.wellWidth, View.wellHeight);
        return out;
    }
    private JPanel createAddPlatePanel(String platename) {
        JPanel out = new JPanel();
        out.setBackground(new Color(0,0,50));
        JLabel label = new JLabel(platename);
        label.setForeground(Color.WHITE);
        out.add(label, BorderLayout.CENTER);
        out.setBounds(0, 0, View.wellWidth, View.wellHeight);
        return out;
    }
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        super.paintComponent(g);
        g.setColor(currentColor);
        g.fillOval(0, 0, getWidth(), getHeight());
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
    private JPanel createBlackPanel() {
        JPanel out = new JPanel();
        out.setLayout(null);
        out.setBackground(Color.BLACK);
        out.setBounds(0, 0, View.plateWidth, View.plateHeight);
        return out;
    }
    private JPanel createWellPanel(Color color, int row, int col) {
        JPanel out = new JPanel();
        out.setBackground(color);

        double posX = plateConfig.getXoffset() + (col-0.5) * plateConfig.getSubBlockWidth();
        double posy = plateConfig.getYoffset() + (row-0.5) * plateConfig.getSubBlockHeight();

        int width = View.wellWidth;
        int height = View.wellHeight;


        out.setBounds(500, 500, width, height);
        return out;
    }
    public void colorWell(Color color, int wellRow, int wellCol) {
        currentColor = color;
        repaint();

    }
    public void addWell(String plateName) {
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
//    public void displayWell(Color color, int rows, int cols) {
//        JPanel blackPanel = createBlackPanel();
//        blackPanel.setBorder(BorderFactory.createLineBorder(color));
//        for (int i = 0; i < rows; i++) {
//            for (int j = 0; j < cols; j++) {
//                JPanel wellPanel = createWellPanel(color, i, j);
//                SwingUtilities.invokeLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        removeAll();
//                        add(blackPanel);
//
//                        blackPanel.add(wellPanel);
//                        revalidate();
//                        repaint();
//                    }
//                });
//            }
//        }

    //    }

    //Relay requested from View in response to an "removePlate" step
    private void removeWell() {
        currentDisplay = this.createEmptyWell();
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
