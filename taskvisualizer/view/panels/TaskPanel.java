package org.ucb.bio134.taskvisualizer.view.panels;

import org.ucb.c5.semiprotocol.model.Task;

import javax.swing.*;
import java.awt.*;

/**
 * Similar to Notification Panel. Displays the current task below the workspace for debugging purposes.
 *
 * @author J. Christopher Anderson
 * @author Lucas M. Waldburger
 */
public class TaskPanel extends JPanel {

    private static final Font secondary_font = new Font("Helvetica", 1, 22);
    private static final Font body_font = new Font("Helvetica", 1, 18);
    private static final Font emphasis_font = new Font("Helvetica", 1, 40);

    /**
     * Constructs the Task Panel
     */
    public TaskPanel() {
        setBackground(Color.lightGray);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(0,10,10,10));

        add(Box.createVerticalGlue());

        //Create a welcome message
        JLabel lbl1 = new JLabel("Task Panel");
        lbl1.setFont(body_font);
        lbl1.setHorizontalAlignment(JLabel.CENTER);
        add(lbl1);

        add(Box.createVerticalGlue());

        JLabel lbl2 = new JLabel(" ");
        lbl1.setFont(body_font);
        add(lbl2);

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

                //Create a welcome message
                JLabel lbl1 = new JLabel("Task Panel");
                lbl1.setFont(body_font);
                lbl1.setHorizontalAlignment(JLabel.CENTER);
                add(lbl1);

                add(Box.createVerticalGlue());

                JLabel lbl2 = new JLabel("Done");
                lbl1.setFont(body_font);
                add(lbl2);

                add(Box.createVerticalGlue());
                
                revalidate();
                repaint();
            }
        });
    }

    /**
     * Displays the current task in the Semiprotocol
     *
     * @param step the current task
     */
    public void notify(Task step) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                removeAll();

                add(Box.createVerticalGlue());

                //Create a welcome message
                JLabel lbl1 = new JLabel("Task Panel");
                lbl1.setFont(body_font);
                lbl1.setHorizontalAlignment(JLabel.CENTER);
                add(lbl1);

                add(Box.createVerticalGlue());

                JLabel lbl2 = new JLabel(step.toString());
                lbl1.setFont(body_font);
                add(lbl2);

                add(Box.createVerticalGlue());

                revalidate();
                repaint();
            }
        });
    }
}
