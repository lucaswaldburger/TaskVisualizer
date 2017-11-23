package org.ucb.bio134.taskvisualizer.view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextField;  
import javax.swing.UIManager;
import javax.swing.JPasswordField;
import javax.swing.JButton;

public class GUILoginScreen extends JPanel {
/**
 * 
 */
private static final long serialVersionUID = 1L;
JTextField tboUsername;
JPasswordField passwordField;
JButton btnOk;
JButton btnCancel;

/**
 * Create the panel.
 */
public GUILoginScreen() {
    setBackground(Color.DARK_GRAY);
    setLayout(null);
    setLookAndFeel();



    JLabel lblUsername = new JLabel("Username");
    lblUsername.setFont(new Font("Segoe UI Black", Font.ITALIC, 18));
    lblUsername.setBounds(77, 170, 109, 35);
    add(lblUsername);

    JLabel lblPassword = new JLabel("Password");
    lblPassword.setFont(new Font("Segoe UI Black", Font.ITALIC, 18));
    lblPassword.setBounds(77, 235, 109, 35);
    add(lblPassword);

    tboUsername = new JTextField();
    tboUsername.setFont(new Font("Segoe UI Black", Font.ITALIC, 18));
    tboUsername.setBounds(77, 203, 241, 31);
    add(tboUsername);
    tboUsername.setColumns(10);

    passwordField = new JPasswordField();
    passwordField.setFont(new Font("Segoe UI Black", Font.ITALIC, 18));
    passwordField.setBounds(77, 268, 241, 31);
    add(passwordField);

    btnOk = new JButton("OK");
    btnOk.setFont(new Font("Segoe UI Black", Font.ITALIC, 18));
    btnOk.setBounds(77, 349, 109, 35);
    add(btnOk);


    btnCancel = new JButton("Cancel");
    btnCancel.setFont(new Font("Segoe UI Black", Font.ITALIC, 18));
    btnCancel.setBounds(209, 349, 109, 35);
    add(btnCancel);


}

// method to set the look and feel of the GUI
    private void setLookAndFeel() {
        try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception exc) {
            // ignore error
        }
    }
}