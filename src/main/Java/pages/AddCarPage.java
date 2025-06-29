package pages;

import assemblers.Setup;
import assemblers.Messenger;
import utils.Visual;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;


public class AddCarPage {

    private static JFrame addCarFrame;

    public AddCarPage(JFrame frameIn, String username, JLabel currentCarLabel, JPanel currentCarPanel) {
        // Show the add car page
        showAddCarPage(frameIn, username, currentCarLabel, currentCarPanel);
    }

    private static void showAddCarPage(JFrame frameIn, String username, JLabel currentCarLabel, JPanel currentCarPanel) {
        // Create a JFrame for the add car page
        addCarFrame = new JFrame("Add a new car");
        addCarFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addCarFrame.setSize(420, 260);
        addCarFrame.setLocationRelativeTo(null);

        // Create components for the add car page
        JLabel nameLabel = new JLabel("Car Name:");
        JTextField nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(150, 25));

        JCheckBox favoriteCheckbox = new JCheckBox("Favorite");

        JLabel observationLabel = new JLabel("Observations:");
        JTextArea observationArea = new JTextArea();
        observationArea.setPreferredSize(new Dimension(180, 60));
        observationArea.setLineWrap(true);  // Enable line wrap
        JScrollPane observationScrollPane = new JScrollPane(observationArea);

        JLabel fuelTypeLabel = new JLabel("           Fuel Type:");
        ArrayList<String> fuelTypeArray = new ArrayList<>();
        fuelTypeArray.add("Gasolina 95");
        fuelTypeArray.add("Gasolina 98");
        fuelTypeArray.add("Gasóleo");
        fuelTypeArray.add("Híbrido");
        JComboBox<String> fuelTypeComboBox = new JComboBox<>(fuelTypeArray.toArray(new String[0]));
        fuelTypeComboBox.setPreferredSize(new Dimension(120, fuelTypeComboBox.getPreferredSize().height));

        JLabel nextInspectionLabel = new JLabel("             Next inspection:");

        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(dateEditor);

        JButton addButton = new JButton("Add Car");

        // Set layout for the add car page
        JPanel addCarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 12));
        addCarFrame.getContentPane().add(addCarPanel);

        Visual.addFooter(addCarFrame, frameIn);

        JPanel spacePanel = new JPanel();
        spacePanel.setPreferredSize(new Dimension(10, 20));


        // Add components to the add car page
        addCarPanel.add(nameLabel);
        addCarPanel.add(nameField);
        addCarPanel.add(favoriteCheckbox);
        addCarPanel.add(observationLabel);
        addCarPanel.add(observationScrollPane);
        addCarPanel.add(spacePanel);
        addCarPanel.add(fuelTypeLabel);
        addCarPanel.add(fuelTypeComboBox);
        addCarPanel.add(spacePanel);
        addCarPanel.add(nextInspectionLabel);
        addCarPanel.add(dateSpinner);
        addCarPanel.add(spacePanel);
        addCarPanel.add(addButton);

        // Add ActionListener to the Add Car button
        addButton.addActionListener(e -> {
            String carName = nameField.getText().trim();
            String observation = observationArea.getText().trim();
            String fuelType = Objects.requireNonNull(fuelTypeComboBox.getSelectedItem()).toString();
            char isFavorite = favoriteCheckbox.isSelected() ? 'Y' : 'N';
            Date nextInspection = (Date) dateSpinner.getValue();

            if (carName.isEmpty()){
                Messenger.Popup(addCarFrame, "Please write a car name.", "W");
            } else {
                // Process the data (you can customize this part based on your requirements)
                if (!Setup.createCar(addCarFrame, username, carName, observation, isFavorite, fuelType, nextInspection).equals("0")){
                    // Close the add car page (you may want to perform validation before closing)
                    addCarFrame.dispose();
                    currentCarLabel.setText("   " + username + "'s car: " + Setup.getUserDefaultCar(username)); // Update the label
                    MainPage.initializeCarButtonsAdd(frameIn, currentCarPanel);
                }
            }
        });

        nameField.addActionListener(e -> {
            addButton.doClick(); // Simulate a click on the "Add Car" button
        });


        // Make the add car page visible
        addCarFrame.setVisible(true);
    }
}
