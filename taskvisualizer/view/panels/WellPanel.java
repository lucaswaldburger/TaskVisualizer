package org.ucb.bio134.taskvisualizer.view.panels;

import org.ucb.bio134.taskvisualizer.model.visualizer.BlockType;
import org.ucb.bio134.taskvisualizer.model.visualizer.ContainerType;
import org.ucb.bio134.taskvisualizer.model.visualizer.Well;
import org.ucb.bio134.taskvisualizer.view.View;
import org.ucb.c5.semiprotocol.model.AddContainer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

/**
 * Displays the Well as a filled circle with a color. For the Rack, a green circle represents that a tube
 * is present, cyan and black borders mean that it is selected in the current Task, and a red circle indicates that the
 * tube is overflowing (volume of contents exceeds the maximum volume of the container). For now, the
 * overflow throws an Exception rather than coloring the Well.
 *
 * @author Lucas M. Waldburger
 * @author J. Christopher Anderson
 */
public class WellPanel extends JPanel implements MouseListener {
    private JPanel currentDisplay;
    private Color currentColor;
    private Graphics2D g2d;
    private Color green;
    private Color emptyColor;
    private Color red;
    private Well well;


    /**
     * Constructs the Well Panel
     */
    public WellPanel(ContainerType con, BlockType block, boolean isNew) {
        setLayout(null);
        well = new Well(con,block,isNew);
        green = new Color(76,153,0);
        emptyColor = new Color(160,160,160);
        red = new Color(204,0,0);
        currentDisplay = createEmptyWell();
        currentColor = emptyColor;
        removeWell();

    }

    /**
     * Initial display of the well
     *
     * @return
     */
    private JPanel createEmptyWell() {
        JPanel out = new JPanel();
        out.setOpaque(false);
        out.addMouseListener(this);
        addMouseListener(this);
        out.setBounds(View.wellWidth/2,View.wellHeight/2 , View.wellWidth, View.wellHeight);
        return out;
    }

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
     * Removes cyan border from the Well
     */
    public void removeCyanBorder() {
        setBorder(BorderFactory.createLineBorder(Color.CYAN, 0));
    }

    /**
     * Adds cyan border to the Well
     */
    public void addCyanBorder() {
        setBorder(BorderFactory.createLineBorder(Color.CYAN, 4));
    }

    /**
     * Paints the Well with higher resolution graphics than standard Graphics2D
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
     * Adds green color when tube is added
     */
    public void addTubeColor(AddContainer addcon) throws Exception {
        well.addTube(addcon);
        currentColor = green;
        repaint();
    }

    /**
     * Removes green color when tube is removed
     */
    public void removeTubeColor() {
        currentColor = emptyColor;
    }

    /**
     * Adds black border to the Well
     */
    public void addBlackBorder() {
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
    }

    /**
     * Removes black border from the Well
     */
    public void removeBlackBorder() {
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 0));
    }

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

    public void mousePressed(MouseEvent e) {
        System.out.println("Mouse pressed (# of clicks: "
                + e.getClickCount() + ")");
    }

    public void mouseReleased(MouseEvent e) {
        System.out.println("Mouse released (# of clicks: "
                + e.getClickCount() + ")");
    }

    public void mouseEntered(MouseEvent e) {
        View.contentPanel.getContentsFromWell(well.getContents());
        View.contentPanel.getTubeName(well.getTubeName());
        View.contentPanel.displayContents();
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
        System.out.println("Mouse clicked (# of clicks: "
                + e.getClickCount() + ")");
    }

    public HashMap getContents() {
        return well.getContents();
    }
}
