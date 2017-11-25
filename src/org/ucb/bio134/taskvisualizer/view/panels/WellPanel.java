package org.ucb.bio134.taskvisualizer.view.panels;

import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import org.ucb.bio134.taskvisualizer.model.Config;
import org.ucb.bio134.taskvisualizer.model.ContainerType;
import org.ucb.bio134.taskvisualizer.model.Well;
import org.ucb.bio134.taskvisualizer.view.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class WellPanel extends JPanel implements MouseListener {
    private JPanel currentDisplay;
    private Color currentColor;
    private Config plateConfig;
    private Graphics2D g2d;
    private Well well;
    private ContentPanel cp;

    /**
     *
     * @param color
     */
    public WellPanel(Color color) {
        setLayout(null);
        currentDisplay = createEmptyWell();
        currentColor = color;
        removeWell();
    }

    /**
     *
     * @return
     */
    private JPanel createEmptyWell() {
        JPanel out = new JPanel();
        out.setOpaque(false);
        out.addMouseListener(this);
        addMouseListener(this);
        well = new Well();
//        String labelText = String.valueOf(well.getVolume());
//        JLabel label = new JLabel();
//        out.add(label, BorderLayout.CENTER);
        out.setBounds(View.wellWidth/2,View.wellHeight/2 , View.wellWidth, View.wellHeight);
        return out;
    }

    private void addVolume(String source, double volume) throws Exception{
        well.addVolume(source, volume);
    }

    public void mousePressed(MouseEvent e) {
        System.out.println("Mouse pressed (# of clicks: "
                + e.getClickCount() + ")");
    }

    public void mouseReleased(MouseEvent e) {
        System.out.println("Mouse released (# of clicks: "
                + e.getClickCount() + ")");
    }

    public void mouseEntered(MouseEvent e) {
        View.contentPanel.displayContents();
        View.contentPanel.getContentsFromWell(well.getContents());
    }

    public void mouseExited(MouseEvent e) {
        View.contentPanel.hideContents();
    }

    public void mouseClicked(MouseEvent e) {
        System.out.println("Mouse clicked (# of clicks: "
                + e.getClickCount() + ")");
    }
    public void getContentPanel(ContentPanel contentPanel) {
        this.cp = contentPanel;
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
        label.setForeground(Color.WHITE);
        out.add(label, BorderLayout.CENTER);
        out.setBounds(0, 0, View.wellWidth, View.wellHeight);
        return out;
    }

    /**
     *
     * @param g
     */
    protected void paintComponent(Graphics g) {
        g2d = (Graphics2D) g;
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
        g.drawOval(0,0,g.getClipBounds().width,g.getClipBounds().height);
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

    /**
     *
     * @return
     */
    private JPanel createBlackPanel() {
        JPanel out = new JPanel();
        out.setLayout(null);
        out.setBackground(Color.BLACK);
        out.setBounds(0, 0, View.plateWidth, View.plateHeight);
        return out;
    }

    /**
     *
     * @param color
     * @param row
     * @param col
     * @return
     */
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

    /**
     *
     * @param color
     * @param wellRow
     * @param wellCol
     */
    public void colorWell(Color color, int wellRow, int wellCol) {
        currentColor = color;
        repaint();
    }

    /**
     *
     * @param borderColor
     * @param wellRow
     * @param wellCol
     */
    public void highlightWell(Color borderColor, int wellRow, int wellCol) {
        borderColor = Color.PINK;
        g2d.setColor(borderColor);
        g2d.fillOval(0,0,getWidth()+1, getHeight()+1);
        g2d.setColor(currentColor);
        g2d.fillOval(0,0,getWidth(), getHeight());
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
