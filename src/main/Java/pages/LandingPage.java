package pages;

import assemblers.Bootstrap;
import assemblers.Messenger;
import assemblers.Setup;
import utils.Visual;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class LandingPage {

    public static JFrame landingFrame;

    public static void showLandingPage() {
        // Create a JFrame for the landing page
        landingFrame = new JFrame("My Fuel App");
        landingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        landingFrame.setSize(400, 280);
        landingFrame.setLocationRelativeTo(null);

        Image icon = Toolkit.getDefaultToolkit().getImage(MainPage.class.getResource("/icons/favicon.png"));
        landingFrame.setIconImage(icon);

        // Create components for the landing page
        JLabel welcomeLabel = new JLabel("Your favorite Fuel Tracker!");
        JLabel signInLabel = new JLabel("Create account or Log In");
        signInLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        JTextField usernameField = new JTextField();
        JLabel usernameLabel = new JLabel("Username");
        JPasswordField passwordField = new JPasswordField();
        JLabel passwordLabel = new JLabel("Password");
        JButton signInButton = new JButton("Sign In");

        // Set font size for the labels
        Font labelFont = new Font("Arial", Font.PLAIN, 11);
        usernameLabel.setFont(labelFont);
        passwordLabel.setFont(labelFont);

        // Set layout for the landing page
        JPanel landingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 150, 10));
        landingFrame.getContentPane().add(landingPanel);

        // Add components to the landing page
        landingPanel.add(welcomeLabel);
        landingPanel.add(signInLabel);
        landingPanel.add(Visual.createCenteredLabel(usernameLabel));
        landingPanel.add(usernameField);
        landingPanel.add(Visual.createCenteredLabel(passwordLabel));
        landingPanel.add(passwordField);
        landingPanel.add(signInButton);

        new Bootstrap(landingFrame);

        // Adjust the size of the components
        welcomeLabel.setPreferredSize(new Dimension(300, 22));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        usernameLabel.setPreferredSize(new Dimension(80, 11));
        usernameField.setPreferredSize(new Dimension(95, 20));
        passwordLabel.setPreferredSize(new Dimension(80, 11));
        passwordField.setPreferredSize(new Dimension(95, 22));
        signInButton.setPreferredSize(new Dimension(75, 30));

        signInButton.addActionListener(e -> signIn(usernameField, passwordField));

        // Add ActionListener to the passwordField for Enter key
        passwordField.addActionListener(e -> signIn(usernameField, passwordField));

        // Make the landing page visible
        landingFrame.setVisible(true);
    }

    // Method to handle Sign In
    private static void signIn(JTextField usernameField, JPasswordField passwordField) {
        String username = usernameField.getText().trim();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);
        if (username.isEmpty()) {
            Messenger.Popup(landingFrame, "Please enter your username.", "W");
        } else if (username.length() < 3) {
            Messenger.Popup(landingFrame, "At least three chars for your username.", "E");
        } else if (password.isEmpty()) {
            Messenger.Popup(landingFrame, "Please enter your password.", "W");
        } else if (password.length() < 3) {
            Messenger.Popup(landingFrame, "At least three chars for your password.", "W");
        } else {
            // Launch your main application
            String ret = Setup.getUser(landingFrame, username, password);
            if (!Objects.equals(ret, "0")) {
                usernameField.setText("");
                passwordField.setText("");
                launchMainApplication(landingFrame, username);
                // Close the landing page
                landingFrame.dispose();
            }
        }
    }

    private static void launchMainApplication(JFrame landingFrame, String username) {
        // Open your main application (replace MainPage with your actual class name)
        SwingUtilities.invokeLater(() -> new MainPage(landingFrame, username));
    }

}