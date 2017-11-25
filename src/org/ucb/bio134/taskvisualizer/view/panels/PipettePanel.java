package org.ucb.bio134.taskvisualizer.view.panels;

import org.ucb.bio134.taskmaster.PriceCalculator;
import org.ucb.bio134.taskmaster.SemiprotocolPriceSimulator;
import org.ucb.bio134.taskmaster.model.Tip;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author J. Christopher Anderson
 * @author Lucas M. Waldburger
 */
public class PipettePanel extends JPanel {

    private static final Font secondary_font = new Font("Helvetica", 1, 22);
    private static final Font body_font = new Font("Helvetica", 1, 18);
    private static final Font emphasis_font = new Font("Helvetica", 1, 40);

    /**
     *
     */
    public PipettePanel() {
        setBackground(Color.lightGray);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
        add(Box.createVerticalGlue());

        JLabel lbl1 = new JLabel("Pipette");
        lbl1.setFont(secondary_font);
        add(lbl1);

        add(Box.createVerticalGlue());

        JLabel lbl2 = new JLabel("P20");
        lbl2.setFont(body_font);
        add(lbl2);

        add(Box.createVerticalGlue());

        JLabel lbl3 = new JLabel("P200");
        lbl3.setFont(body_font);
        add(lbl3);

        add(Box.createVerticalGlue());

        JLabel lbl4 = new JLabel("P1000");
        lbl4.setFont(body_font);
        add(lbl4);

        add(Box.createVerticalGlue());
    }

    /**
     *
     * @param selectedPipette
     */
    public void update(Tip selectedPipette) {
        removeAll();

        JLabel lbl1 = new JLabel("Pipette");
        lbl1.setFont(secondary_font);
        add(lbl1);

        add(Box.createVerticalGlue());

        JLabel lbl2 = new JLabel("P20");
        lbl2.setFont(body_font);
        add(lbl2);

        add(Box.createVerticalGlue());

        JLabel lbl3 = new JLabel("P200");
        lbl3.setFont(body_font);
        add(lbl3);

        add(Box.createVerticalGlue());

        JLabel lbl4 = new JLabel("P1000");
        lbl4.setFont(body_font);
        add(lbl4);

        if (selectedPipette.equals(Tip.P20)) {
            lbl1.setBorder(BorderFactory.createLineBorder(Color.pink));
        } else if (selectedPipette.equals(Tip.P200)) {
            lbl2.setBorder(BorderFactory.createLineBorder(Color.pink));
        } else if (selectedPipette.equals(Tip.P1000)) {
            lbl3.setBorder(BorderFactory.createLineBorder(Color.pink));
        } else {
            throw new IllegalArgumentException("Invalid pipette selection");
        }

        revalidate();
        repaint();

    }
}
