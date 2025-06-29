package pages;

import assemblers.Bootstrap;
import assemblers.JSON;
import assemblers.Messenger;
import assemblers.Setup;
import utils.Popup;
import utils.Visual;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static pages.MainPage.initializeCarButtonsAdd;

public class UserPage {

    private static JFrame viewDetailsFrame;

    public UserPage(JFrame frameIn, String username, JPanel inCarPanel) {
        showViewDetailsPage(frameIn, username, inCarPanel);
    }

    private static void showViewDetailsPage(JFrame frameIn, String username, JPanel inCarPanel) {
        // Create a JFrame for the view details page
        viewDetailsFrame = new JFrame("User info");
        viewDetailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        viewDetailsFrame.setSize(420, 220);
        viewDetailsFrame.setLocationRelativeTo(null);

        // Create components for the view details page
        JLabel nameLabel = new JLabel("Hello " + username);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel signInLabel = new JLabel("What do you want do now?");
        signInLabel.setFont(new Font("Arial", Font.ITALIC, 12));

        JButton updateButton = new JButton("Update Password");
        JButton deleteButton = new JButton("Delete Account ");
        JButton exportButton = new JButton("  Export Data  ");
        JButton importButton = new JButton("  Import Data  ");

        // Set layout for the view details page
        JPanel viewDetailsPanel = new JPanel(new BorderLayout(10, 2));
        viewDetailsFrame.getContentPane().add(viewDetailsPanel);

        // Create a panel for user details
        JPanel detailsPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        detailsPanel.add(nameLabel);
        detailsPanel.add(Visual.createCenteredLabel(signInLabel));

        // Add components to the view details page
        viewDetailsPanel.add(detailsPanel, BorderLayout.NORTH);

        // Create an empty panel for space
        JPanel spacePanel = new JPanel();
        spacePanel.setPreferredSize(new Dimension(1, 2)); // Adjust the height as needed
        viewDetailsPanel.add(spacePanel, BorderLayout.SOUTH);

        updateButton.addActionListener(e -> {
            frameIn.setVisible(true);
            SwingUtilities.invokeLater(() -> new UpdatePwd(viewDetailsFrame, username));
        });

        deleteButton.addActionListener(e -> {
            if (!MainPage.deleteUser(viewDetailsFrame, username).equals("0")){
                frameIn.setVisible(true);
            }
        });

        // Add update/delete buttons at the center
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        viewDetailsPanel.add(buttonPanel, BorderLayout.CENTER);

        exportButton.addActionListener(e -> {
            String jsonStr = Setup.exportUserJSON(username);
            try {
                String out = JSON.saveJsonToFile(viewDetailsFrame, jsonStr, username);
               if (out.equals("0")) {
                   Messenger.Popup(viewDetailsFrame, "File exported successfully.", "I");
               } else if (out.equals("1")) {
                   Messenger.Popup(viewDetailsFrame, "Error saving file.", "E");
                };
            } catch (Exception ex) {
                Messenger.Popup(viewDetailsFrame, ex.getMessage(), "E");
            }
        });

        importButton.addActionListener(e -> {
            String jsonStr = "";
            if (Popup.confirmDecision("This can replace your current data. Are you sure you want to proceed?")==JOptionPane.YES_OPTION) {
                try {
                    jsonStr = JSON.getJsonFromFile(viewDetailsFrame);
                } catch (UnsupportedLookAndFeelException | IOException ex) {
                    Messenger.Popup(viewDetailsFrame, ex.getMessage(), "E");
                }

                if (jsonStr.equals("1")) {
                    Messenger.Popup(viewDetailsFrame, "Error importing", "E");
                } else {
                    if (Setup.createUserFromStringJson(jsonStr).equals("1")) {
                        Messenger.Popup(viewDetailsFrame, "Error creating user.", "E");
                    } else {
                        JSON.createUserJSON(viewDetailsFrame, Setup.getUsers());
                        new Bootstrap(viewDetailsFrame);
                        Messenger.Popup(viewDetailsFrame, "Data imported successfully.", "I");
                    }
                }
                initializeCarButtonsAdd(frameIn, inCarPanel);
            }

        });


        // Create a panel for export/import buttons at the center
        JPanel exportImportPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        exportImportPanel.add(exportButton);
        exportImportPanel.add(importButton);
        viewDetailsPanel.add(exportImportPanel, BorderLayout.SOUTH);

        // Add footer under the export/import buttons
        Visual.addFooter(viewDetailsFrame, frameIn);

        // Make the view details page visible
        viewDetailsFrame.setVisible(true);
    }
}
