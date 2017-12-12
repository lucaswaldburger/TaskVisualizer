package org.ucb.bio134.taskvisualizer.view.panels;

import org.ucb.bio134.taskmaster.PriceCalculator;
import org.ucb.bio134.taskmaster.SemiprotocolPriceSimulator;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

/**
 *
 * @author J. Christopher Anderson
 * @author Lucas M. Waldburger
 */
public class BurdenPanel extends JPanel {

    private static final Font secondary_font = new Font("Helvetica", 1, 22);
    private static final Font body_font = new Font("Helvetica", 1, 18);
    private static final Font emphasis_font = new Font("Helvetica", 1, 40);
    private SemiprotocolPriceSimulator sps = new SemiprotocolPriceSimulator();
    private PriceCalculator pc = new PriceCalculator();
    DecimalFormat dc = new DecimalFormat("0");


    /**
     *
     */
    public BurdenPanel() {
        setBackground(Color.lightGray);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
        add(Box.createVerticalGlue());

        JLabel lbl1 = new JLabel("Burden Tracker");
        lbl1.setFont(secondary_font);
        add(lbl1);

        add(Box.createVerticalGlue());

        JLabel lbl2 = new JLabel("Total: ");
        lbl2.setFont(body_font);
        add(lbl2);

        add(Box.createVerticalGlue());
        add(Box.createVerticalGlue());
        add(Box.createVerticalGlue());
        add(Box.createVerticalGlue());
        add(Box.createVerticalGlue());
    }

    /**
     *
     * @param burdenTotal
     */
    public void update(double burdenTotal) {
        removeAll();

        add(Box.createVerticalGlue());

        JLabel lbl1 = new JLabel("Burden Tracker");
        lbl1.setFont(secondary_font);
        add(lbl1);

        add(Box.createVerticalGlue());

        String formattedBurden = dc.format(burdenTotal);
        JLabel lbl2 = new JLabel("Total: " + formattedBurden);
        lbl2.setFont(body_font);
        add(lbl2);

        add(Box.createVerticalGlue());
        add(Box.createVerticalGlue());
        add(Box.createVerticalGlue());
        add(Box.createVerticalGlue());
        add(Box.createVerticalGlue());

        revalidate();
        repaint();

    }
}
