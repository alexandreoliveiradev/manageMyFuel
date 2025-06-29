package utils;

import assemblers.Messenger;
import pages.MainPage;
import pages.UserPage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public class Visual {

    // Helper method to create a centered JLabel
    public static JLabel createCenteredLabel(JLabel label) {
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private static void setupGithubLabel(JLabel githubLabel, JFrame frame) {
        githubLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        githubLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                try {
                    if (Checks.isInternetAvailable()) {
                        Desktop.getDesktop().browse(new URI("https://github.com/alexandreoliveiradev"));
                    } else {
                        Messenger.Popup(frame, "Connect to the internet to check this link.", "E");
                    }
                } catch (IOException | URISyntaxException ex) {
                    ex.printStackTrace();
                    Messenger.Popup(new JFrame(), ex.getMessage(), "E");
                }
            }
        });
    }

    public static void addFooter(JFrame currentFrame, JFrame oldFrame) {
        addFooter(currentFrame, oldFrame, false, null, true, null);
    }


    public static void addFooter(JFrame currentFrame, JFrame oldFrame, boolean mainPage, String username, Boolean leave, JPanel inCarPanel) {
        // Create a footer panel for developer information
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));  // Align components to the right
        JLabel developerLabel = new JLabel("                  Developed by Alexandre Oliveira                  ");
        developerLabel.setFont(new Font("Arial", Font.PLAIN, 10));

        ImageIcon githubIcon = new ImageIcon(Objects.requireNonNull(MainPage.class.getResource("/icons/github.png")));
        JLabel githubLabel = new JLabel(githubIcon);
        setupGithubLabel(githubLabel, currentFrame);

        // Add an empty space between githubLabel and deleteUserLabel
        JLabel spaceLabel = new JLabel("    ");

        // Add the new close icon
        ImageIcon closeIcon = new ImageIcon(Objects.requireNonNull(MainPage.class.getResource("/icons/close.png")));
        JLabel closeLabel = new JLabel(closeIcon);

        closeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (!leave) {
                    if (utils.Popup.confirmDecision("Are you sure you want to leave?") == JOptionPane.YES_OPTION) {
                        // Close the JFrame when the close icon is clicked
                        currentFrame.setVisible(false);
                        oldFrame.setVisible(true);
                    } else {
                        currentFrame.setVisible(true);
                    }
                } else {
                    currentFrame.setVisible(false);
                    oldFrame.setVisible(true);
                }
            }
        });

        footerPanel.add(closeLabel);
        footerPanel.add(developerLabel);
        footerPanel.add(githubLabel);
        footerPanel.add(spaceLabel);  // Add an empty space
        if (mainPage) {
            // Add the new close icon
            ImageIcon userIcon = new ImageIcon(Objects.requireNonNull(MainPage.class.getResource("/icons/user.png")));
            JLabel userLabel = new JLabel(userIcon);


            userLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            userLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    currentFrame.setVisible(true);
                    new UserPage(currentFrame, username, inCarPanel);
                }
            });

            footerPanel.add(userLabel);
        }

        // Add the footer panel to the content pane of the JFrame
        currentFrame.getContentPane().add(footerPanel, BorderLayout.SOUTH);
    }

    public static void addFooterTable(JFrame currentFrame, JFrame oldFrame, JPanel southPanel) {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JLabel developerLabel = new JLabel("                  Developed by Alexandre Oliveira                  ");
        developerLabel.setFont(new Font("Arial", Font.PLAIN, 10));

        ImageIcon githubIcon = new ImageIcon(Objects.requireNonNull(MainPage.class.getResource("/icons/github.png")));
        JLabel githubLabel = new JLabel(githubIcon);
        setupGithubLabel(githubLabel, currentFrame);

        JLabel spaceLabel = new JLabel("    ");

        ImageIcon closeIcon = new ImageIcon(Objects.requireNonNull(MainPage.class.getResource("/icons/close.png")));
        JLabel closeLabel = new JLabel(closeIcon);

        closeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                currentFrame.setVisible(false);
                oldFrame.setVisible(true);
            }
        });

        footerPanel.add(closeLabel);
        footerPanel.add(developerLabel);
        footerPanel.add(githubLabel);
        footerPanel.add(spaceLabel);
        southPanel.add(footerPanel, BorderLayout.SOUTH);

        // Add the footer panel to the content pane of the JFrame
        currentFrame.getContentPane().add(southPanel, BorderLayout.SOUTH);
    }
}
