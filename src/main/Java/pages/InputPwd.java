package pages;

import assemblers.Messenger;
import assemblers.Setup;
import utils.Checks;
import utils.Visual;
import javax.swing.*;
import java.awt.*;

public class InputPwd {

    public static JFrame inputFrame;

    public InputPwd(JFrame origFrame, String userName) {
        inputPassword(origFrame, userName);
    }

    public static void inputPassword(JFrame origFrame, String username) {
        // Create a JFrame for the landing page
        inputFrame = new JFrame("Confirm password");
        inputFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inputFrame.setSize(400, 200);
        inputFrame.setLocationRelativeTo(null);

        // Create components for the landing page
        JLabel confirmLabel = new JLabel("Please confirm with your password.");
        confirmLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        JPasswordField passwordField = new JPasswordField();
        JLabel passwordLabel = new JLabel("Password");
        JButton okButton = new JButton("Ok");

        // Set font size for the labels
        Font labelFont = new Font("Arial", Font.PLAIN, 11);
        passwordLabel.setFont(labelFont);

        // Set layout for the landing page
        JPanel landingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 150, 15));
        inputFrame.getContentPane().add(landingPanel);

        // Add components to the landing page
        landingPanel.add(confirmLabel);
        landingPanel.add(Visual.createCenteredLabel(passwordLabel));
        landingPanel.add(passwordField);
        landingPanel.add(okButton);

        // Adjust the size of the components
        passwordLabel.setPreferredSize(new Dimension(80, 8));
        passwordField.setPreferredSize(new Dimension(95, 22));
        okButton.setPreferredSize(new Dimension(75, 30));

        okButton.addActionListener(e -> {
            char[] passwordChars = passwordField.getPassword();
            String password = new String(passwordChars);
            if (password.isEmpty()) {
                Messenger.Popup(inputFrame, "Please type your password.", "W");
            } else {
                if (Checks.isPassword(username, password)) {
                    Setup.deleteUser(inputFrame, username);
                    inputFrame.dispose();
                    origFrame.setVisible(false);
                } else {
                    Messenger.Popup(inputFrame, "Incorrect password. Please try again.", "E");
                }
            }
        });

        passwordField.addActionListener(e -> {
            okButton.doClick(); // Simulate a click on the "Add Car" button
        });

        // Make the landing page visible
        inputFrame.setVisible(true);
    }
}
