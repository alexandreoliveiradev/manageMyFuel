package pages;

import assemblers.Setup;
import utils.Visual;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class UpdateCarPage {

    private static JFrame updateCarFrame;
    private static String newCar;

    public UpdateCarPage(JFrame frameIn, String username, JLabel currentCarLabel) {
        showUpdateCarPage(frameIn, username, currentCarLabel);
    }

    private static void showUpdateCarPage(JFrame frameIn, String username, JLabel currentCarLabel) {
        // Create a JFrame for the update car page
        updateCarFrame = new JFrame("Update Current Car");
        updateCarFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        updateCarFrame.setSize(400, 200);
        updateCarFrame.setLocationRelativeTo(null);

        // Create components for the update car page
        JLabel currentCarTextLabel = new JLabel("Current Car:");
        String currentCarName = Setup.getUserDefaultCar(username);
        JLabel currentCarNameLabel = new JLabel("<html><b>" + currentCarName + "</b></html>");

        JLabel newCarLabel = new JLabel("Set default car:");
        ArrayList<String> carNames = Setup.getUserCarNames(username);
        JComboBox<String> carComboBox = new JComboBox<>(carNames.toArray(new String[0]));

        JButton okButton = new JButton("Ok");

        // Set layout for the update car page
        JPanel updateCarPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        updateCarFrame.getContentPane().add(updateCarPanel);

        Visual.addFooter(updateCarFrame, frameIn);

        // Add components to the update car page
        JPanel currentCarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        currentCarPanel.add(currentCarTextLabel);
        currentCarPanel.add(currentCarNameLabel);

        JPanel newCarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        newCarPanel.add(newCarLabel);
        newCarPanel.add(carComboBox);

        updateCarPanel.add(currentCarPanel);
        updateCarPanel.add(newCarPanel);

        // Centered panel for OK button
        JPanel okButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        okButton.setPreferredSize(new Dimension(60, okButton.getPreferredSize().height));
        carComboBox.setPreferredSize(new Dimension(120, carComboBox.getPreferredSize().height));
        okButtonPanel.add(okButton);
        updateCarPanel.add(okButtonPanel);

        // Add ActionListener to the OK button
        okButton.addActionListener(e -> {
            newCar = Objects.requireNonNull(carComboBox.getSelectedItem()).toString();
            // Process the data (you can customize this part based on your requirements)
            Setup.updateUserDefaultCar(updateCarFrame, username, newCar);
            // Close the update car page
            updateCarFrame.dispose();
            MainPage.car_name = Setup.getUserDefaultCar(username);
            currentCarLabel.setText("   " + username + "'s car: " + newCar); // Update the label
            updateCarFrame.setVisible(false);
            frameIn.setVisible(true);
        });

        // Make the update car page visible
        updateCarFrame.setVisible(true);
    }
}
