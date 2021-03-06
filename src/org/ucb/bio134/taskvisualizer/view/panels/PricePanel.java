package org.ucb.bio134.taskvisualizer.view.panels;

import org.ucb.bio134.taskmaster.PriceCalculator;
import org.ucb.bio134.taskmaster.SemiprotocolPriceSimulator;
import org.ucb.c5.semiprotocol.model.AddContainer;
import org.ucb.c5.semiprotocol.model.Task;
import org.ucb.c5.semiprotocol.model.Transfer;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

/**
 *
 * @author J. Christopher Anderson
 * @author Lucas M. Waldburger
 */
public class PricePanel extends JPanel {

    private static final Font secondary_font = new Font("Helvetica", 1, 22);
    private static final Font body_font = new Font("Helvetica", 1, 18);
    private static final Font emphasis_font = new Font("Helvetica", 1, 40);
    private SemiprotocolPriceSimulator sps = new SemiprotocolPriceSimulator();
    private PriceCalculator pc = new PriceCalculator();
    DecimalFormat dc = new DecimalFormat("0.00");

    /**
     *
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
     *
     * @param reagentTotal
     * @param containerTotal
     * @param tipTotal
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
