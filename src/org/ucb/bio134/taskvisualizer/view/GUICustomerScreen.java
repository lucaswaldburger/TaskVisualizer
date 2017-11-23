package org.ucb.bio134.taskvisualizer.view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;

public class GUICustomerScreen extends JPanel {

/**
 * 
 */
private static final long serialVersionUID = 1L;
/**
 * Create the panel.
 */

String firstName = "Phil";

public GUICustomerScreen() {
    setLayout(null);

    JLabel lblHello = new JLabel("Hello " + firstName);
    lblHello.setFont(new Font("Segoe UI Black", Font.ITALIC, 14));
    lblHello.setBounds(184, 11, 107, 27);
    add(lblHello);

}

} 