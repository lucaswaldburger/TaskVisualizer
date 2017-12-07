package org.ucb.bio134.taskvisualizer.view.panels;

import org.ucb.c5.semiprotocol.model.AddContainer;
import org.ucb.c5.semiprotocol.model.Container;
import org.ucb.c5.semiprotocol.model.Task;
import org.ucb.c5.semiprotocol.model.Transfer;

import javax.swing.*;
import java.awt.*;

/**
 * Extends Professor Anderson's Notification Panel for use across the workspace for both
 * Eppendorf tubes and PCR tubes in the Rack and PCR plates in the Deck.
 *
 * @author J. Christopher Anderson
 * @author Lucas M. Waldburger
 */
public class NotificationPanel extends JPanel {
       
    private static final Font secondary_font = new Font("Helvetica", Font.BOLD, 22);
    private static final Font body_font = new Font("Helvetica", Font.PLAIN, 18);
    private static final Font emphasis_font = new Font("Helvetica", Font.BOLD, 40);

    /**
     * Constructs the Notification Panel
     */
    public NotificationPanel() {
        setBackground(Color.lightGray);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(0,10,10,10)); 
        
        //Create a welcome message
        add(Box.createVerticalGlue());
        
        JLabel lbl1 = new JLabel("Click");
        lbl1.setFont(secondary_font);
        add(lbl1);
        
        JLabel lbl2 = new JLabel("Next");
        lbl2.setFont(emphasis_font);
        add(lbl2);
        
        add(Box.createVerticalGlue());

        JLabel lbl3 = new JLabel("to begin protocol");
        lbl3.setFont(body_font);
        add(lbl3);
        
        add(Box.createVerticalGlue());
    }

    /**
     * Display when Semiprotocol is completed
     */
    public void showComplete() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                removeAll();
                add(Box.createVerticalGlue());

                JLabel lbl1 = new JLabel("Done");
                lbl1.setFont(emphasis_font);
                add(lbl1);
                
                revalidate();
                repaint();
            }
        });
    }

    /**
     * Displays notification of the current task in the Semiprotocol
     *
     * @param step the current task
     */
    public void notify(Task step) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                removeAll();
                
                switch (step.getOperation()) {
                    case addContainer:
                        AddContainer addcon = (AddContainer) step;
                        add(Box.createVerticalGlue());

                        String container = "";

                        //TODO: Implement pcr strip tube
                        if (addcon.getTubetype().equals(Container.eppendorf_1p5mL) ||
                                addcon.getTubetype().equals(Container.eppendorf_2mL)) {
                            container = "Eppendorf tube";
                        } else if (addcon.getTubetype().equals(Container.pcr_tube)) {
                            container = "PCR tube";
                        } else if (addcon.getTubetype().equals(Container.pcr_plate_96)) {
                            container = "PCR plate";
                        }

                        JLabel lop = new JLabel("Add " + container + ":");
                        lop.setFont(body_font);
                        add(lop);
                        
                        JLabel lname = new JLabel(addcon.getName());
                        lname.setFont(secondary_font);
                        add(lname);
                        
                        JLabel ltopos = new JLabel("to position:");
                        ltopos.setFont(body_font);
                        add(Box.createVerticalGlue());
                        add(ltopos);
                        
                        JLabel lpos = new JLabel(calcWellLabel(addcon.getLocation()));
                        lpos.setFont(emphasis_font);
                        add(lpos);
                        break;
                    case removeContainer:
                        break;
                    case transfer:
                        Transfer tfer = (Transfer) step;
                        JLabel ttop = new JLabel("transfer:");
                        ttop.setFont(body_font);
                        add(Box.createVerticalGlue());
                        add(ttop);
                        
                        JLabel tlname = new JLabel("" + tfer.getVolume() + " uL");
                        tlname.setFont(emphasis_font);
                        add(tlname);
                        
                        JLabel tltopos = new JLabel("from: ");
                        tltopos.setFont(secondary_font);
                        JLabel tltopos1 = new JLabel(calcWellLabel(tfer.getSource()));
                        tltopos1.setFont(secondary_font);
                        add(Box.createVerticalGlue());
                        add(tltopos);
                        add(tltopos1);
                        
                        JLabel tltopos2 = new JLabel("to: ");
                        tltopos2.setFont(secondary_font);
                        JLabel tltopos3 = new JLabel(calcWellLabel(tfer.getDest()));
                        tltopos3.setFont(secondary_font);
                        add(Box.createVerticalGlue());
                        add(tltopos2);
                        add(tltopos3);

                        add(Box.createVerticalGlue());
                        break;
                    case multichannel:
                        break;
                    case dispense:
                        break;
                }

                revalidate();
                repaint();
            }
        });
    }

    /**
     * Calculates the location of a Well within a Plate from a label
     *
     * @param platenameslashA1 well label
     * @return location of Well
     */
    private static String calcWellLabel(String platenameslashA1) {
        String well = platenameslashA1;
        if(well.contains("/")) {
            String[] splitted = well.split("/");
            well = splitted[1];
        }
        return well;
    }
}
