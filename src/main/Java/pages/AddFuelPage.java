package pages;

import assemblers.Messenger;
import assemblers.Setup;
import utils.Visual;

import javax.swing.*;
import java.awt.*;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class AddFuelPage {

    private static JFrame addFuelFrame;

    public AddFuelPage(JFrame frameIn, String username, String carname) {
        showAddFuelPage(frameIn, username, carname);
    }

    private static void showAddFuelPage(JFrame frameIn, String username, String carname) {
        addFuelFrame = new JFrame("Add Fuel Entry");
        addFuelFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addFuelFrame.setSize(500, 340);
        addFuelFrame.setLocationRelativeTo(null);

        try {
            // Create components for the add fuel page
            JLabel dateLabel = new JLabel("Fuel Date:");
            JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
            dateSpinner.setEditor(dateEditor);

            JLabel priceLabel = new JLabel("Price:");
            JTextField priceField = new JTextField(10);

            JLabel literPriceLabel = new JLabel("Liter Price:");
            JTextField literPriceField = new JTextField(10);

            JLabel totalKmsLabel = new JLabel("Total Kms:");
            JTextField totalKmsField = new JTextField(10);

            JLabel litersLabel = new JLabel("Liters:");
            JTextField litersField = new JTextField(10);

            JLabel brandLabel = new JLabel("Brand Name:");
            JComboBox<String> brandComboBox = createBrandComboBox(username);

            JButton addButton = new JButton("Add Fuel Entry");

            // Set layout for the add fuel page
            JPanel addFuelPanel = new JPanel(new GridBagLayout());
            addFuelFrame.getContentPane().add(addFuelPanel);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);

            gbc.gridx = 0;
            gbc.gridy = 0;
            addFuelPanel.add(dateLabel, gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;
            addFuelPanel.add(dateSpinner, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            addFuelPanel.add(priceLabel, gbc);

            gbc.gridx = 1;
            gbc.gridy = 1;
            addFuelPanel.add(priceField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            addFuelPanel.add(literPriceLabel, gbc);

            gbc.gridx = 1;
            gbc.gridy = 2;
            addFuelPanel.add(literPriceField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            addFuelPanel.add(totalKmsLabel, gbc);

            gbc.gridx = 1;
            gbc.gridy = 3;
            addFuelPanel.add(totalKmsField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 4;
            addFuelPanel.add(litersLabel, gbc);

            gbc.gridx = 1;
            gbc.gridy = 4;
            addFuelPanel.add(litersField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 5;
            addFuelPanel.add(brandLabel, gbc);

            gbc.gridx = 1;
            gbc.gridy = 5;
            gbc.insets = new Insets(10, 0, 0, 0);  // Add some space at the top
            addFuelPanel.add(brandComboBox, gbc);

            gbc.gridx = 0;
            gbc.gridy = 6;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(25, 0, 0, 0);  // Add some space at the top
            addFuelPanel.add(addButton, gbc);

            // Add ActionListener to the Add Fuel Entry button
            addButton.addActionListener(e -> {
                try {
                    Date fuelDate = (Date) dateSpinner.getValue();
                    Float price = getFieldValue(priceField, "Price", Float.class);
                    Float literPrice = getFieldValue(literPriceField, "liter Price", Float.class);
                    Integer totalKms = getFieldValue(totalKmsField, "Total Kms", Integer.class);
                    Float liters = getFieldValue(litersField, "Liters", Float.class);

                    String brandName = getSelectedBrandName(brandComboBox);

                    if (fuelDate == null || brandName == null || totalKms == null || (price == null && literPrice == null)) {
                        Messenger.Popup(addFuelFrame, "Please fill in all required fields.", "W");
                    } else {
                        // Process the data (you can customize this part based on your requirements)
                        String message = MessageFormat.format("Fuel Date: {0}\nPrice: {1}€\nLiter Price: {2}\nTotal Kms: {3}\nLiters: {4}\nBrand Name: {5}",
                                fuelDate, price, literPrice, totalKms, liters, brandName);

                        if (price == null){
                            price = 0.0f;
                        }
                        if (literPrice == null){
                            literPrice = 0.0f;
                        }
                        if (liters == null){
                            liters = 0.0f;
                        }
                        Setup.addFuelToCar(addFuelFrame, username, carname, fuelDate, price, literPrice, totalKms, liters, brandName);
                        Messenger.Popup(addFuelFrame, message.replaceAll(", \\d{2}:\\d{2}", ""), "+");
                        // Close the add fuel page (you may want to perform validation before closing)
                        addFuelFrame.dispose();
                        addFuelFrame.setVisible(false);
                        frameIn.setVisible(true);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Messenger.Popup(new JFrame(), ex.getMessage(), "E");
                }
            });

            litersField.addActionListener(e -> {
                addButton.doClick();
            });

            brandComboBox.addActionListener(e -> {
                addButton.doClick();
            });


            // Add the JScrollPane to the frame
            FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
            addFuelFrame.setLayout(flowLayout);

            Visual.addFooter(addFuelFrame, frameIn);


            // Make the add fuel page visible
            addFuelFrame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static JComboBox<String> createBrandComboBox(String username) {
        try {
            ArrayList<String> brandList = Setup.getUserCarBrands(username);
            Collections.sort(brandList);
            JComboBox<String> brandComboBox = new JComboBox<>(brandList.toArray(new String[0]));
            brandComboBox.setEditable(true);
            return brandComboBox;
        } catch (Exception e) {
            e.printStackTrace();
            return new JComboBox<>();
        }
    }

    private static <T extends Number> T getFieldValue(JTextField textField, String fieldName, Class<T> clazz) throws ParseException {
        String text = textField.getText().trim();
        if (text.isEmpty()) {
            return null;
        }

        try {
            if (clazz == Float.class) {
                return clazz.cast(Float.parseFloat(text));
            } else if (clazz == Integer.class) {
                return clazz.cast(Integer.parseInt(text));
            } else {
                throw new UnsupportedOperationException("Unsupported type: " + clazz.getSimpleName());
            }
        } catch (NumberFormatException e) {
            throw new ParseException("Invalid value for " + fieldName, textField.getCaretPosition());
        }
    }


    private static String getSelectedBrandName(JComboBox<String> brandComboBox) {
        Object selectedBrand = brandComboBox.getSelectedItem();
        return selectedBrand != null ? selectedBrand.toString() : null;
    }
}
