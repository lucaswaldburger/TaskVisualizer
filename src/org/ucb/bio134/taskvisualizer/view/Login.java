package org.ucb.bio134.taskvisualizer.view;

import javax.swing.*;
import java.awt.*;

public class Login extends JDialog {

    MainLoginFrame mainloginframe = new MainLoginFrame();

    //Constructor
    public Login() { ;
        setTitle("Login Sample");
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        getContentPane().add(mainloginframe);
    }
    public class MainLoginFrame extends JPanel {

        //Create Labels
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        //Create TextField
        JTextField usernameTextField = new JTextField(10);
        JPasswordField passwordPasswordField = new JPasswordField(10);
        //Create Button
        JButton loginButton = new JButton("Login");
        JButton clearButton = new JButton("Clear");
        JButton exitButton = new JButton("Exit");
        //Create ComboBox
        String[] selectUser = {"Administrator", "Registrar"};
        JComboBox listUser = new JComboBox(selectUser);
        //Constraints
        GridBagConstraints usernameLabelConstraints = new GridBagConstraints();
        GridBagConstraints passwordLabelConstraints = new GridBagConstraints();
        GridBagConstraints userTypeConstraints = new GridBagConstraints();
        GridBagConstraints usernameTfConstraints = new GridBagConstraints();
        GridBagConstraints passwordPfConstraints = new GridBagConstraints();
        GridBagConstraints loginConstraints = new GridBagConstraints();
        GridBagConstraints clearConstraints = new GridBagConstraints();
        GridBagConstraints exitConstraints = new GridBagConstraints();

        //Constructor
        public MainLoginFrame() {
            setLayout(new GridBagLayout());

            usernameLabelConstraints.anchor = GridBagConstraints.LINE_START;
            usernameLabelConstraints.weightx = 0.5;
            usernameLabelConstraints.weighty = 0.5;
            add(usernameLabel, usernameLabelConstraints);

            passwordLabelConstraints.anchor = GridBagConstraints.LINE_START;
            passwordLabelConstraints.weightx = 0.5;
            passwordLabelConstraints.weighty = 0.5;
            passwordLabelConstraints.gridx = 0;
            passwordLabelConstraints.gridy = 1;
            add(passwordLabel, passwordLabelConstraints);

            usernameTfConstraints.anchor = GridBagConstraints.LINE_START;
            usernameTfConstraints.weightx = 0.5;
            usernameTfConstraints.weighty = 0.5;
            usernameTfConstraints.gridx = 1;
            usernameTfConstraints.gridy = 0;
            add(usernameTextField, usernameTfConstraints);

            passwordPfConstraints.anchor = GridBagConstraints.LINE_START;
            passwordPfConstraints.weightx = 0.5;
            passwordPfConstraints.weighty = 0.5;
            passwordPfConstraints.gridx = 1;
            passwordPfConstraints.gridy = 1;
            add(passwordPasswordField, passwordPfConstraints);

            userTypeConstraints.anchor = GridBagConstraints.LINE_START;
            userTypeConstraints.weightx = 0.5;
            userTypeConstraints.weighty = 0.5;
            userTypeConstraints.gridx = 1;
            userTypeConstraints.gridy = 2;
            add(listUser, userTypeConstraints);

            loginConstraints.anchor = GridBagConstraints.LINE_START;
            loginConstraints.weightx = 0.5;
            loginConstraints.weighty = 0.5;
            loginConstraints.gridx = 1;
            loginConstraints.gridy = 3;
            add(loginButton, loginConstraints);

            clearConstraints.anchor = GridBagConstraints.LINE_START;
            clearConstraints.weightx = 0.5;
            clearConstraints.weighty = 0.5;
            clearConstraints.gridx = 2;
            clearConstraints.gridy = 3;
            add(clearButton, clearConstraints);

            exitConstraints.anchor = GridBagConstraints.LINE_START;
            exitConstraints.weightx = 0.5;
            exitConstraints.weighty = 0.5;
            exitConstraints.gridx = 3;
            exitConstraints.gridy = 3;
            add(exitButton, exitConstraints);
        }
    }
}