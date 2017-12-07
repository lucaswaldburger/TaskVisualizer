package org.ucb.bio134.taskvisualizer.view.panels;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

/**
 * Panel in View used to display the current price of the Semiprotocol

 *
 * @author J. Christopher Anderson
 * @author Lucas M. Waldburger
 */
public class PricePanel extends JPanel {

    private static final Font secondary_font = new Font("Helvetica", 1, 22);
    private static final Font body_font = new Font("Helvetica", 1, 18);
    private DecimalFormat dc = new DecimalFormat("0.00");

    /**
     * Constructs the Price Panel
     */
    public PricePanel() {
        setBackground(Color.lightGray);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
        add(Box.createVerticalGlue());

        JLabel lbl1 = new JLabel("Price Tracker");
        lbl1.setFont(secondary_font);
        add(lbl1);

        add(Box.createVerticalGlue());

        JLabel lbl2 = new JLabel("Reagent Total: ");
        lbl2.setFont(body_font);
        add(lbl2);

        add(Box.createVerticalGlue());

        JLabel lbl3 = new JLabel("Container Total: ");
        lbl3.setFont(body_font);
        add(lbl3);

        add(Box.createVerticalGlue());

        JLabel lbl4 = new JLabel("Tip Total: ");
        lbl4.setFont(body_font);
        add(lbl4);

        add(Box.createVerticalGlue());
    }

    /**
     * Updates the Price Panel for the current Task
     *
     * @param reagentTotal current cost of reagents
     * @param containerTotal current cost of containers
     * @param tipTotal current cost of containers
     */
    public void update(double reagentTotal, double containerTotal, double tipTotal) {
        removeAll();

        add(Box.createVerticalGlue());

        JLabel lbl1 = new JLabel("Price Tracker");
        lbl1.setFont(secondary_font);
        add(lbl1);

        add(Box.createVerticalGlue());

        String formattedReagentTotal = dc.format(reagentTotal);
        JLabel lbl2 = new JLabel("Reagent Total: $" + formattedReagentTotal);
        lbl2.setFont(body_font);
        add(lbl2);

        add(Box.createVerticalGlue());

        String formattedContainerTotal = dc.format(containerTotal);
        JLabel lbl3 = new JLabel("Container Total: $" + formattedContainerTotal);
        lbl3.setFont(body_font);
        add(lbl3);

        add(Box.createVerticalGlue());

        String formattedTipTotal = dc.format(tipTotal);
        JLabel lbl4 = new JLabel("Tip Total: $" + formattedTipTotal);
        lbl4.setFont(body_font);
        add(lbl4);

        add(Box.createVerticalGlue());

        revalidate();
        repaint();

    }

}
