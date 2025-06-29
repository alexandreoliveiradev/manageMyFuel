package pages;

import assemblers.Messenger;
import assemblers.Setup;
import utils.Popup;
import utils.Visual;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.Objects;

public class MainPage {

    static String car_name;
    private static final int big_width = 140;

    static int init = 0;
    private static final int small_width = 120;
    private static final int bigHeight = 80;
    private static final int smallHeight = 40;

    static String userName;
    static JFrame landingFrame;
    static JLabel currentCarLabel; // Declare currentCarLabel at the class level

    private static JButton changeCarButton;
    private static JButton removeCarButton;

    private static JLabel carLabel;
    private static JPanel lowerButtonPanel;  // Declare lowerButtonPanel at the class level

    public MainPage(JFrame landingFrameIn, String username_in) {
        SwingUtilities.invokeLater(MainPage::createAndShowGUI);
        userName = username_in;
        landingFrame = landingFrameIn;
        init = 0;
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("My Fuel App");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set a consistent font
        Font defaultFont = new Font("Arial", Font.PLAIN, 14);
        UIManager.put("Button.font", defaultFont);
        UIManager.put("Label.font", defaultFont);

        // Create a label for the current car name
        car_name = Setup.getUserDefaultCar(userName);
        currentCarLabel = new JLabel("   " + userName + "'s car: " + car_name);
        currentCarLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Create a JPanel for the upper buttons using GridBagLayout
        JPanel upperButtonPanel = new JPanel(new GridBagLayout());

        // Create smaller buttons
        JButton newFuelButton = createStyledButton("Add new fuel up", big_width, bigHeight);
        JButton statsButton = createStyledButton("View Stats", big_width, bigHeight);
        JButton historyButton = createStyledButton("Check History", big_width, bigHeight);

        addActionButtonListener(historyButton, frame, "Hist");
        addActionButtonListener(statsButton, frame, "Stats");
        addActionButtonListener(newFuelButton, frame, "New");

        addComponentToPanel(upperButtonPanel, newFuelButton, 0);
        addComponentToPanel(upperButtonPanel, statsButton, 1);
        addComponentToPanel(upperButtonPanel, historyButton, 2);

        // Create a space panel to add a gap between the upper and lower grids
        JPanel spacePanel = new JPanel();

        // Create a JPanel for the lower buttons using GridBagLayout
        lowerButtonPanel = new JPanel(new GridBagLayout());

        // Create three buttons for changing, removing, and adding a car
        changeCarButton = createStyledButton("Change Car", small_width, smallHeight);
        removeCarButton = createStyledButton("Remove Car", small_width, smallHeight);
        JButton addCarButton = createStyledButton("Add Car", small_width, smallHeight);

        addComponentToPanel(lowerButtonPanel, addCarButton, 2);

        // Create a JPanel for the current car label using FlowLayout
        JPanel currentCarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        currentCarPanel.add(currentCarLabel);


        addActionButtonListener(addCarButton, frame, "Add", currentCarPanel);
        if (init < 1) {
            initializeCarButtons(frame, currentCarPanel);
        }

        // Create a JPanel for the main content using BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(currentCarPanel, BorderLayout.NORTH);
        mainPanel.add(upperButtonPanel, BorderLayout.CENTER);
        mainPanel.add(spacePanel, BorderLayout.SOUTH);
        mainPanel.add(lowerButtonPanel, BorderLayout.SOUTH);


        // Add the mainPanel to the content pane of the JFrame
        frame.getContentPane().add(mainPanel);

        Visual.addFooter(frame, landingFrame, true, userName, false, currentCarPanel);

        // Set the default close operation and size of the JFrame
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


        Date today = new Date();
        if (Setup.getNextInspectionByCar(userName, car_name).compareTo(today) <= 0) {
            Messenger.Popup(landingFrame, "Don't forget to take your " + car_name + " to inspection.\n" +
                    "You can click on the car (top right) \nto update your revision date.", "I");
        }


    }

    private static JButton createStyledButton(String text, int width, int height) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(width, height));
        button.setBackground(new Color(63, 81, 181)); // Background color
        button.setForeground(Color.WHITE); // Text color
        button.setFocusPainted(false); // Remove focus border
        return button;
    }

    private static void addActionButtonListener(JButton button, JFrame frame, String message, JPanel currentCarPanel) {
        button.addActionListener(e -> {
            if (Objects.equals(message, "Add")) {
                launchAddCarPage(frame, currentCarPanel);
            } else if (Objects.equals(message, "Del")) {
                car_name = Setup.getUserDefaultCar(userName);
                if (Popup.confirmDecision("Are you sure you want to delete " + car_name + "?") == JOptionPane.YES_OPTION) {
                    Setup.removeCar(frame, userName, car_name);
                    car_name = Setup.getUserDefaultCar(userName);
                    currentCarLabel.setText("   " + userName + "'s car: " + car_name); // Update the label
                    if (Setup.getNumberCars(userName) < 1) {
                        initializeCarButtonsRemove(currentCarPanel);
                    }
                }
            } else if (Objects.equals(message, "Upd")) {
                launchUpdateCarPage(frame);
            } else if (Objects.equals(message, "New")) {
                if (Setup.getNumberCars(userName) > 0) {
                    launchAddFuelPage(frame, car_name);
                } else {
                    Messenger.Popup(frame, "Please add a car to unlock this feature.", "I");
                }
            } else if (Objects.equals(message, "Hist")) {
                if (Setup.getNumberCars(userName) > 0) {
                    launchViewHistory(frame, userName, car_name);
                } else {
                    Messenger.Popup(frame, "Please add a car to unlock this feature.", "I");
                }
            } else if (Objects.equals(message, "Stats")) {
                if (Setup.getNumberCars(userName) > 0) {
                    //Messenger.Popup(frame, "feature in development", "E");
                    launchViewStats(frame, userName, car_name);
                } else {
                    Messenger.Popup(frame, "Please add a car to unlock this feature.", "I");
                }
            } else {
                Messenger.Popup(frame, message, "I");
            }
        });
    }

    private static void launchViewStats(JFrame frame, String userName, String carName) {
        SwingUtilities.invokeLater(() -> new StatsPage(frame, userName, carName));
    }

    private static void launchAddFuelPage(JFrame frame, String carName) {
        SwingUtilities.invokeLater(() -> new AddFuelPage(frame, userName, carName));
    }

    private static void launchViewHistory(JFrame frame, String userName, String carName){
        String history = Setup.getHistoryCar(userName);
        if (!Objects.equals(history, "Start adding some fuels!")) {
            SwingUtilities.invokeLater(() -> new HistoryPage(frame, history, userName, carName));
        } else {
            Messenger.Popup(frame, history, "I");
        }
    }

    private static void addActionButtonListener(JButton button, JFrame frame, String message) {
        addActionButtonListener(button, frame, message, null);
    }

    private static void addComponentToPanel(JPanel panel, JComponent component, int gridx) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(component, gbc);
    }

    private static void launchAddCarPage(JFrame frameIn, JPanel currentCarPanel) {
        SwingUtilities.invokeLater(() -> new AddCarPage(frameIn, userName, currentCarLabel, currentCarPanel));
    }

    private static void launchUpdateCarPage(JFrame frameIn) {
        SwingUtilities.invokeLater(() -> new UpdateCarPage(frameIn, userName, currentCarLabel));
    }


    public static void initializeCarButtons(JFrame frame, JPanel inCarPanel) {
        if (Setup.getNumberCars(userName) > 0) {
            init++;
            car_name = Setup.getUserDefaultCar(userName);
            inCarPanel.removeAll();

            // Use BorderLayout for currentCarPanel
            inCarPanel.setLayout(new BorderLayout());

            currentCarLabel = new JLabel("   " + userName + "'s car: " + car_name);
            currentCarLabel.setFont(new Font("Arial", Font.BOLD, 16));
            inCarPanel.add(currentCarLabel, BorderLayout.WEST);

            ImageIcon carIcon = new ImageIcon(Objects.requireNonNull(MainPage.class.getResource("/icons/car.png")));
            carLabel = new JLabel(carIcon);
            carLabel.setHorizontalAlignment(SwingConstants.RIGHT);

            // Adjust the layout parameters to add a gap to the right
            inCarPanel.add(carLabel, BorderLayout.EAST);
            inCarPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10)); // Add a 5px gap to the right

            carLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            carLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    new CarPage(frame, userName);
                }
            });

            // Add listeners to the buttons
            addComponentToPanel(lowerButtonPanel, changeCarButton, 1);
            addComponentToPanel(lowerButtonPanel, removeCarButton, 3);

            addActionButtonListener(changeCarButton, frame, "Upd", lowerButtonPanel);
            addActionButtonListener(removeCarButton, frame, "Del", lowerButtonPanel);

            // Repaint the lowerButtonPanel to reflect the changes
            lowerButtonPanel.revalidate();
            lowerButtonPanel.repaint();
        }
    }


    public static void initializeCarButtonsRemove(JPanel inCarPanel) {
        if (Setup.getNumberCars(userName) == 0) {
            // Hide the components
            changeCarButton.setVisible(false);
            removeCarButton.setVisible(false);
            currentCarLabel.setVisible(false);
            carLabel.setVisible(false);

            // Repaint the inCarPanel to reflect the changes
            inCarPanel.revalidate();
            inCarPanel.repaint();
        }
    }

    public static void initializeCarButtonsAdd(JFrame frameIn, JPanel inCarPanel) {
        if (Setup.getNumberCars(userName) > 0 && init > 0) {
            // Hide the components
            changeCarButton.setVisible(true);
            removeCarButton.setVisible(true);
            currentCarLabel.setVisible(true);
            carLabel.setVisible(true);

            // Repaint the inCarPanel to reflect the changes
            inCarPanel.revalidate();
            inCarPanel.repaint();
        }
        else {
            initializeCarButtons(frameIn, inCarPanel);
        }
    }

    public static String deleteUser(JFrame frame, String username) {
        if (Popup.confirmDecision("Are you sure you want to delete your account?") == JOptionPane.YES_OPTION) {
            // Creating and showing InputPwd on the EDT
            SwingUtilities.invokeLater(() -> new InputPwd(frame, username));

            // Return immediately as the subsequent code should not execute until the user interacts with InputPwd
            return "1";
        }
        return "0";
    }


}




