package org.ucb.bio134.taskvisualizer.view.panels;

import org.ucb.bio134.taskmaster.model.Tip;

import javax.swing.*;
import java.awt.*;

/**
 * Informs the user which pipette is currently being used for Transfer and Dispense tasks
 * in a Semiprotocol. This can be used for optimization in reducing the number of times
 * the Semiprotocol alternates between different pipettes.
 *
 * @author J. Christopher Anderson
 * @author Lucas M. Waldburger
 */
public class PipettePanel extends JPanel {

    private static final Font secondary_font = new Font("Helvetica", 1, 22);
    private static final Font body_font = new Font("Helvetica", 1, 18);
    private static Font body_bold = new Font("Helvetica", 1, 18);
    private static final Font emphasis_font = new Font("Helvetica", 1, 40);

    /**
     * Constructs the Pipette Panel
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
     * Updates the View with the pipette that is currently selected
     *
     * @param selectedPipette tip corresponding to selected pipette
     */
    public void update(Tip selectedPipette) {
        removeAll();

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

        if (selectedPipette.equals(Tip.P20)) {
            lbl2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 10));
        } else if (selectedPipette.equals(Tip.P200)) {
            lbl3.setBorder(BorderFactory.createLineBorder(Color.BLACK, 10));
        } else if (selectedPipette.equals(Tip.P1000)) {
            lbl4.setBorder(BorderFactory.createLineBorder(Color.BLACK, 10));
        } else {
            throw new IllegalArgumentException("Invalid pipette selection");
        }

        add(Box.createVerticalGlue());

        revalidate();
        repaint();
    }

    /**
     * Resets the view when the current task in a Semiprotocol is not a Dispense or Transfer
     */
    public void reset() {
        removeAll();
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

        revalidate();
        repaint();
    }
}
