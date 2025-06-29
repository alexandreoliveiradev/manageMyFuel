package pages;

import assemblers.Messenger;
import assemblers.Setup;
import utils.Checks;
import utils.Visual;
import javax.swing.*;
import java.awt.*;

public class UpdatePwd {

    public static JFrame inputFrame;

    public UpdatePwd(JFrame origFrame, String userName) {
        updatePassword(origFrame, userName);
    }

    public static void updatePassword(JFrame origFrame, String username) {
        // Create a JFrame for the landing page
        inputFrame = new JFrame("Update password");
        inputFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inputFrame.setSize(400, 260);
        inputFrame.setLocationRelativeTo(null);

        // Create components for the landing page
        JLabel confirmLabel = new JLabel("Please confirm with your password.");
        confirmLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        JPasswordField passwordField = new JPasswordField();
        JLabel passwordLabel = new JLabel("Current Password");
        JPasswordField newPasswordField = new JPasswordField();
        JLabel newPasswordLabel = new JLabel("New Password");
        JButton okButton = new JButton("Ok");

        // Set font size for the labels
        Font labelFont = new Font("Arial", Font.PLAIN, 11);
        passwordLabel.setFont(labelFont);
        newPasswordLabel.setFont(labelFont);

        // Set layout for the landing page
        JPanel landingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 150, 15));
        inputFrame.getContentPane().add(landingPanel);

        // Add components to the landing page
        landingPanel.add(confirmLabel);
        landingPanel.add(Visual.createCenteredLabel(passwordLabel));
        landingPanel.add(passwordField);
        landingPanel.add(Visual.createCenteredLabel(newPasswordLabel));
        landingPanel.add(newPasswordField);
        landingPanel.add(okButton);

        // Adjust the size of the components
        passwordLabel.setPreferredSize(new Dimension(90, 8));
        passwordField.setPreferredSize(new Dimension(95, 22));
        newPasswordLabel.setPreferredSize(new Dimension(90, 8));
        newPasswordField.setPreferredSize(new Dimension(95, 22));
        okButton.setPreferredSize(new Dimension(75, 30));

        okButton.addActionListener(e -> {
            char[] passwordChars = passwordField.getPassword();
            String password = new String(passwordChars);
            char[] newPasswordChars = newPasswordField.getPassword();
            String newPassword = new String(newPasswordChars);
            if (password.isEmpty()) {
                Messenger.Popup(inputFrame, "Please type your current password.", "W");
            } else if (newPassword.isEmpty()) {
                Messenger.Popup(inputFrame, "Please type your new password.", "W");
            } else if (newPassword.length() < 4) {
                Messenger.Popup(inputFrame, "At least three chars for your password.", "W");
            } else {
                if (Checks.isPassword(username, password)) {
                    Setup.updateUserPassword(inputFrame, username, newPassword);
                    inputFrame.dispose();
                    origFrame.setVisible(false);
                } else {
                    Messenger.Popup(inputFrame, "Incorrect password. Please try again.", "E");
                }
            }
        });

        newPasswordField.addActionListener(e -> {
            okButton.doClick();
        });

        // Make the landing page visible
        inputFrame.setVisible(true);
    }
}
