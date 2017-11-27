package org.ucb.bio134.taskvisualizer.view.panels;

import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import org.ucb.bio134.taskvisualizer.model.Config;
import org.ucb.bio134.taskvisualizer.model.ContainerType;
import org.ucb.bio134.taskvisualizer.model.Well;
import org.ucb.bio134.taskvisualizer.view.View;
import org.ucb.c5.semiprotocol.model.Container;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author Lucas M. Waldburger
 * @author J. Christopher Anderson
 */
public class WellPanel extends JPanel implements MouseListener {
    private JPanel currentDisplay;
    private Color currentColor;
    private Config plateConfig;
    private Graphics2D g2d;
    private Well well;
    private Color green;
    private Color emptyColor;
    private Color red;

    /**
     *
     * @param containerType
     */
    public WellPanel(ContainerType containerType) {
        setLayout(null);
        green = new Color(76,153,0);
        emptyColor = new Color(160,160,160);
        red = new Color(204,0,0);
        currentDisplay = createEmptyWell();
        currentColor = emptyColor;
        removeWell();
        well = new Well(containerType);

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
//        String labelText = String.valueOf(well.getVolume());
//        JLabel label = new JLabel();
//        out.add(label, BorderLayout.CENTER);
        out.setBounds(View.wellWidth/2,View.wellHeight/2 , View.wellWidth, View.wellHeight);
        return out;
    }

    /**
     *
     * @param source
     * @param volume
     * @throws Exception
     */
    public void addVolume(String source, double volume) throws Exception{
        well.addVolume(source, volume);
        if (well.isFull()) {
            currentColor = red;
        }
    }

    /**
     *
     * @param volume
     * @throws Exception
     */
    public void removeVolume(double volume) throws Exception {
        well.removeVolume(volume);
    }

    /**
     *
     * @param e
     */
    public void mousePressed(MouseEvent e) {
        System.out.println("Mouse pressed (# of clicks: "
                + e.getClickCount() + ")");
    }

    /**
     *
     * @param e
     */
    public void mouseReleased(MouseEvent e) {
        System.out.println("Mouse released (# of clicks: "
                + e.getClickCount() + ")");
    }

    /**
     *
     * @param e
     */
    public void mouseEntered(MouseEvent e) {
        View.contentPanel.getContentsFromWell(well.getContents());
        View.contentPanel.displayContents();
    }

    /**
     *
     * @param e
     */
    public void mouseExited(MouseEvent e) {
        View.contentPanel.hideContents();
    }

    /**
     *
     * @param e
     */
    public void mouseClicked(MouseEvent e) {
        System.out.println("Mouse clicked (# of clicks: "
                + e.getClickCount() + ")");
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
     * @param tubeName
     * @param tube
     */
    public void addTubeColor(String tubeName, Container tube) {
        well.addTube(tubeName, tube);
        currentColor = green;
        repaint();
    }

    /**
     *
     * @param tubeName
     */
    public void removeTubeColor(String tubeName) {
        well.removeTube();
        currentColor = emptyColor;
    }

    /**
     *
     */
    public void highlightWell() {
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
//        Color borderColor = Color.PINK;
//        g2d.setColor(borderColor);
//        g2d.fillOval(0,0,getWidth()+100, getHeight()+100);
//        g2d.setColor(currentColor);
//        g2d.fillOval(0,0,getWidth(), getHeight());
//        revalidate();
//        repaint();
    }
    public void unhighlightWell() {
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 0));
    }

    /**
     *
     * @param plateName
     */
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
