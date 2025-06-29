package pages;

import assemblers.JSON;
import assemblers.Messenger;
import assemblers.Setup;
import utils.Formatter;
import utils.Visual;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

public class CarPage {

    private static JFrame viewDetailsFrame;
    private static String carName;
    private static String observations;
    private static Date inspectionDate;
    public CarPage(JFrame frameIn, String username) {
        showViewDetailsPage(frameIn, username);
    }

    private static void showViewDetailsPage(JFrame frameIn, String username) {
        // Create a JFrame for the view details page
        viewDetailsFrame = new JFrame("View Car Details");
        viewDetailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        viewDetailsFrame.setSize(420, 400);
        viewDetailsFrame.setLocationRelativeTo(null);

        // Get car details
        carName = Setup.getUserDefaultCar(username);
        String oldObservations = Setup.getCarObservations(username, carName);
        String oldInspectionDate = Setup.getNextInspectionByCar(username, carName).toString();

        // Create components for the view details page
        JLabel carNameLabel = new JLabel("Name: " + carName);
        carNameLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel observationsLabel = new JLabel("<html><br>Observations: </html>");
        JLabel fuelTypeLabel = new JLabel("               Fuel : " + Setup.getUserCarFuelType(username, carName));

        JTextArea observationsTextArea = new JTextArea(observations);
        observationsTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        observationsTextArea.setLineWrap(true);
        observationsTextArea.setWrapStyleWord(true);

        JLabel dateLabel = new JLabel("Next inspection:");
        JLabel spaceL = new JLabel("    ");
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setValue(Setup.getNextInspectionByCar(username, carName));

        JButton okButton = new JButton("Ok");

        // Set layout for the view details page
        JPanel viewDetailsPanel = new JPanel(new BorderLayout(10, 10));
        viewDetailsFrame.getContentPane().add(viewDetailsPanel);

        // Create a panel for car details
        JPanel carDetailsPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        carDetailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        carDetailsPanel.add(carNameLabel);
        carDetailsPanel.add(fuelTypeLabel);
        carDetailsPanel.add(observationsLabel);

        // Create a panel for observations with scroll pane
        JScrollPane scrollPane = new JScrollPane(observationsTextArea);
        scrollPane.setPreferredSize(new Dimension(300, 200));

        // Add components to the view details page
        viewDetailsPanel.add(carDetailsPanel, BorderLayout.NORTH);
        viewDetailsPanel.add(scrollPane, BorderLayout.CENTER);

        // Create an empty panel for space
        JPanel spacePanel = new JPanel();
        spacePanel.setPreferredSize(new Dimension(1, 10)); // Adjust the height as needed
        viewDetailsPanel.add(spacePanel, BorderLayout.SOUTH);


        // Add OK button at the bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        buttonPanel.add(dateLabel);
        buttonPanel.add(dateSpinner);
        buttonPanel.add(spaceL);
        buttonPanel.add(okButton);

        okButton.addActionListener(e -> {

            observations = observationsTextArea.getText();
            inspectionDate = (Date) dateSpinner.getValue();


            // Update the observations label
            observationsLabel.setText("Observations: " + observations);

            Setup.updateCarObservations(username, carName, observations);
            JSON.createUserJSON(viewDetailsFrame, Setup.getUsers());
            if (!(Objects.equals(oldObservations, observations))) {
                Messenger.Popup(viewDetailsFrame, "Observations updated.", "I");
            }

            Setup.updateCarNextInspection(username, carName, inspectionDate);
            if (!(Objects.equals(oldInspectionDate, inspectionDate.toString()))) {
                Messenger.Popup(viewDetailsFrame, "Next inspection updated.", "I");
            }

            JSON.createUserJSON(viewDetailsFrame, Setup.getUsers());

            viewDetailsFrame.dispose();
        });

        viewDetailsPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add footer under the OK button
        Visual.addFooter(viewDetailsFrame, frameIn);

        // Make the view details page visible
        viewDetailsFrame.setVisible(true);
    }
}
