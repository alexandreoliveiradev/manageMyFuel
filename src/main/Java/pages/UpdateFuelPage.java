package pages;

import assemblers.Messenger;
import assemblers.Setup;
import utils.Visual;

import javax.swing.*;
import java.awt.*;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class UpdateFuelPage {

    private static JFrame updateFuelFrame;

    public UpdateFuelPage(JFrame frameIn, String username, String carname, int id, String fuelDate,
                          String literPrice, String price, String liters, String brandName, String totalKms, JTable table) {
        showUpdateFuelPage(frameIn, username, carname, id, fuelDate, literPrice, price, liters, brandName, totalKms, table);
    }

    private static void showUpdateFuelPage(JFrame frameIn, String username, String carname, int id, String fuelDate,
                                           String literPrice, String price, String liters, String brandName, String totalKms,
                                           JTable table) {
        updateFuelFrame = new JFrame("Edit Fuel");
        updateFuelFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        updateFuelFrame.setSize(500, 340);
        updateFuelFrame.setLocationRelativeTo(null);

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
            JComboBox<String> brandComboBox = createBrandComboBox(username, brandName.trim());

            JButton addButton = new JButton("Confirm");

            // Set layout for the add fuel page
            JPanel addFuelPanel = new JPanel(new GridBagLayout());
            updateFuelFrame.getContentPane().add(addFuelPanel);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);

            gbc.gridx = 0;
            gbc.gridy = 0;
            addFuelPanel.add(dateLabel, gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;
            String strDate = fuelDate.trim();
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy");
            Date date = inputFormat.parse(strDate);

            dateSpinner.setValue(date);
            addFuelPanel.add(dateSpinner, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            addFuelPanel.add(priceLabel, gbc);

            gbc.gridx = 1;
            gbc.gridy = 1;
            priceField.setText(price.trim().replace("€", ""));
            addFuelPanel.add(priceField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            addFuelPanel.add(literPriceLabel, gbc);

            gbc.gridx = 1;
            gbc.gridy = 2;
            literPriceField.setText(literPrice.trim().replace("€",""));
            addFuelPanel.add(literPriceField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            addFuelPanel.add(totalKmsLabel, gbc);

            gbc.gridx = 1;
            gbc.gridy = 3;
            totalKmsField.setText(totalKms.trim());
            addFuelPanel.add(totalKmsField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 4;
            addFuelPanel.add(litersLabel, gbc);

            gbc.gridx = 1;
            gbc.gridy = 4;
            litersField.setText(liters.trim());
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
                    Date newFuelDate = (Date) dateSpinner.getValue();
                    Float newPrice = getFieldValue(priceField, "Price", Float.class);
                    Float newLiterPrice = getFieldValue(literPriceField, "liter Price", Float.class);
                    Integer newTotalKms = getFieldValue(totalKmsField, "Total Kms", Integer.class);
                    Float newLiters = getFieldValue(litersField, "Liters", Float.class);

                    String newBrandName = getSelectedBrandName(brandComboBox);

                    if (newFuelDate == null || newBrandName == null || newTotalKms == null || (newPrice == null && newLiterPrice == null)) {
                        Messenger.Popup(updateFuelFrame, "Please fill in all required fields.", "W");
                    } else {
                        // Process the data (you can customize this part based on your requirements)
                        String message = MessageFormat.format("Fuel Date: {0}\nPrice: {1}€\nLiter Price: {2}€\nTotal Kms: {3}\nLiters: {4}\nBrand Name: {5}",
                                newFuelDate, newPrice, newLiterPrice, newTotalKms, newLiters, newBrandName);

                        if (newPrice == null){
                            newPrice = 0.0f;
                        }
                        if (newLiterPrice == null){
                            newLiterPrice = 0.0f;
                        }
                        if (newLiters == null){
                            newLiters = 0.0f;
                        }
                        if (Setup.updateCarFuel(updateFuelFrame, username, carname, id, newFuelDate, newLiterPrice, newPrice, newLiters, newBrandName, newTotalKms)=="0"){
                            Messenger.Popup(updateFuelFrame, "Fuel updated to:\n" + message.replaceAll(", \\d{2}:\\d{2}", ""), "+");
                            HistoryPage.ActionEditor.refreshData(table, username, carname);
                        }
                        // Close the add fuel page (you may want to perform validation before closing)
                        updateFuelFrame.dispose();
                        updateFuelFrame.setVisible(false);
                        frameIn.setVisible(true);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Messenger.Popup(new JFrame(), ex.getMessage(), "E");
                }
            });
            

            // Add the JScrollPane to the frame
            FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
            updateFuelFrame.setLayout(flowLayout);

            Visual.addFooter(updateFuelFrame, frameIn);


            // Make the add fuel page visible
            updateFuelFrame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            Messenger.Popup(updateFuelFrame, e.getMessage(), "E");
        }
    }

    private static JComboBox<String> createBrandComboBox(String username, String brandname) {
        try {
            ArrayList<String> brandList = Setup.getUserCarBrands(username);
            Collections.sort(brandList);
            if (brandList.contains(brandname.trim())){
                brandList.remove(brandname.trim());
                brandList.add(0, brandname.trim());
            }
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
