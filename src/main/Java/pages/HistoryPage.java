package pages;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import assemblers.Messenger;
import assemblers.Setup;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import utils.Visual;

import static pages.HistoryPage.ActionEditor.refreshData;

public class HistoryPage {

    private static final SimpleDateFormat INPUT_FORMAT = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
    private static final SimpleDateFormat OUTPUT_FORMAT = new SimpleDateFormat("dd-MMM-yyyy");

    private static JScrollPane scrollPane;

    private static boolean [] filtered = {true};

    public HistoryPage(JFrame frame, String history, String username, String carName) {
        createAndShowGUI(frame, history, username, carName);
    }

    public static void createAndShowGUI(JFrame origFrame, String fuelRecordsJson, String username, String carName) {
        JFrame frame = new JFrame("Fuel History");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel userNameLabel = new JLabel("        Your " + carName + "'s fuels!");
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel userNamePanel = new JPanel(new BorderLayout());
        userNamePanel.add(userNameLabel, BorderLayout.LINE_START);

        // Parse the JSON string into a JsonNode
        JsonNode fuelRecords = parseJsonString(fuelRecordsJson);

        filtered = new boolean[]{true};

        boolean MoreThanZeroCarFuels = Setup.getMoreThanZeroCarFuels(username, carName);

        if (!MoreThanZeroCarFuels) {
            filtered[0] = false;
        }

        JTable table = createTable(fuelRecords, username, frame, carName, scrollPane);

        boolean hasTwoCarWithFuels = Setup.hasTwoCarWithFuels(username);
        // Add the table to a JScrollPane
        scrollPane = new JScrollPane(table);

        if (hasTwoCarWithFuels && MoreThanZeroCarFuels) {
            JLabel viewAllLabel = new JLabel("View all fuels        ");
            viewAllLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            userNamePanel.add(viewAllLabel, BorderLayout.LINE_END);

            viewAllLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Add your click event handling code here
                    if (filtered[0]) {
                        filtered[0] = false;
                        int height = refreshData(table, username, carName);
                        scrollPane.setPreferredSize(new Dimension(640, height));
                        frame.pack(); // Pack the frame to adjust its size
                        viewAllLabel.setText("View " + carName + " fuels        ");
                        userNameLabel.setText("        All of " + username + "'s fuels!");
                    } else {
                        filtered[0] = true;
                        int height =  refreshData(table, username, carName);
                        scrollPane.setPreferredSize(new Dimension(640, height));
                        frame.pack(); // Pack the frame to adjust its size
                        viewAllLabel.setText("View all fuels        ");
                        userNameLabel.setText("        Your " + carName + "'s fuels!");
                    }
                }
            });
        }

        userNamePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        frame.setLayout(new BorderLayout());
        frame.add(userNamePanel, BorderLayout.NORTH);

        // Create a panel for the "Ok" button
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel filterLabel = new JLabel("<html><b><i>You can order by any row by clicking on its header.</i></b></html>");
        filterLabel.setFont(new Font(filterLabel.getFont().getName(), Font.PLAIN, 10));
        filterPanel.add(filterLabel);

        // Add the table to the CENTER position
        frame.add(scrollPane, BorderLayout.CENTER);

        // Create a panel for the SOUTH position and add the "Ok" button and footer
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(filterPanel, BorderLayout.NORTH);
        Visual.addFooterTable(frame, origFrame, southPanel);
        frame.add(southPanel, BorderLayout.SOUTH);

        // Calculate the preferred size based on the row count
        int rowCount = table.getRowCount();
        int rowHeight = table.getRowHeight();
        int preferredHeight = Math.min( rowCount * ( rowHeight + 6) + 50, 600); // Set a maximum height, adjust as needed
        scrollPane.setPreferredSize(new Dimension(640, preferredHeight));

        frame.pack(); // Pack the frame to adjust its size

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JsonNode parseJsonString(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
            Messenger.Popup(new JFrame(), e.getMessage(), "E");
            return null;
        }
    }

    private static JTable createTable(JsonNode fuelRecords, String username, JFrame origFrame, String carName, JScrollPane scrollPane) {
        String[] columnNames = {"ID", " Car", " Date", " Price/l", " Price", " Liters", " Brand", " Total Kms", " Action"};

        // Convert JsonNode to a List<JsonNode>
        List<JsonNode> fuelRecordsList = convertToList(fuelRecords);

        // Sort the records by fuelDate in descending order
        sortRecordsByDateDescending(fuelRecordsList);

        // Create a DefaultTableModel
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8; // Action column is editable
            }
        };

        // Populate the table model with data
        for (JsonNode record : fuelRecordsList) {
            String fuelDate = record.get("fuelDate").asText();
            boolean isDefault = Objects.equals(record.get("carName").asText(), carName);
            if (isDefault || !HistoryPage.filtered[0]) {
                model.addRow(new Object[]{
                        record.get("id").asInt(),
                        "  " + record.get("carName").asText(),
                        "  " + formatDateString(fuelDate),
                        "  " + formatDoubleString(String.valueOf(record.get("literPrice").asDouble())) + " €",
                        "  " + formatDoubleString(String.valueOf(record.get("price").asDouble())) + " €",
                        "  " + formatDoubleString(String.valueOf(record.get("liters").asDouble())),
                        "  " + record.get("brandName").asText(),
                        "  " + record.get("totalKms").asInt(),
                        "Edit/Remove"
                });
            }
        }

        // Create a JTable with the model
        JTable table = new JTable(model);

        table.setAutoCreateRowSorter(true);

        // Set column widths
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);
        table.getColumnModel().getColumn(1).setPreferredWidth(70); // Car Name
        table.getColumnModel().getColumn(2).setPreferredWidth(85); // Fuel Date
        table.getColumnModel().getColumn(3).setPreferredWidth(60);  // liter Price
        table.getColumnModel().getColumn(4).setPreferredWidth(42);  // Price
        table.getColumnModel().getColumn(5).setPreferredWidth(44);  // liters
        table.getColumnModel().getColumn(6).setPreferredWidth(80); // Brand Name
        table.getColumnModel().getColumn(7).setPreferredWidth(70);  // Total Kms
        table.getColumnModel().getColumn(8).setPreferredWidth(100);  // Action

        // Add custom renderer and editor for the Action column
        table.getColumnModel().getColumn(8).setCellRenderer(new ActionRenderer());
        table.getColumnModel().getColumn(8).setCellEditor(new ActionEditor(new JCheckBox(), table, username, origFrame, carName, scrollPane));

        // Set the selection background color to match the table background
        table.setSelectionBackground(Color.white);

        return table;
    }

    private static Date parseDate(String dateString) {
        try {
            return INPUT_FORMAT.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
            Messenger.Popup(new JFrame(), e.getMessage(), "E");
            return null;
        }
    }

    private static List<JsonNode> convertToList(JsonNode fuelRecords) {
        List<JsonNode> resultList = new ArrayList<>();
        if (fuelRecords != null && fuelRecords.isArray()) {
            fuelRecords.forEach(resultList::add);
        }
        return resultList;
    }

    public static String formatDateString(String inputDateString) {
        try {
            Date date = INPUT_FORMAT.parse(inputDateString);
            return OUTPUT_FORMAT.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Messenger.Popup(new JFrame(), e.getMessage(), "E");
            return null;
        }
    }

    public static String formatDoubleString(String inputDoubleString) {
        return inputDoubleString.replace(".0", "");
    }

    private static List<JsonNode> sortRecordsByDateDescending(List<JsonNode> fuelRecordsList) {
        fuelRecordsList.sort((record1, record2) -> {
            Date date1 = parseDate(record1.get("fuelDate").asText());
            Date date2 = parseDate(record2.get("fuelDate").asText());
            assert date2 != null;
            return date2.compareTo(date1);
        });
        return fuelRecordsList;
    }

    // Add a new renderer for the Action column
    static class ActionRenderer extends JPanel implements TableCellRenderer {

        public ActionRenderer() {
            setOpaque(false);
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));

            JButton editButton = new JButton(new ImageIcon(Objects.requireNonNull(MainPage.class.getResource("/icons/editFuel.png"))));
            JButton deleteButton = new JButton(new ImageIcon(Objects.requireNonNull(MainPage.class.getResource("/icons/deleteFuel.png"))));

            // Set the background color of the buttons to white
            editButton.setBackground(Color.WHITE);
            deleteButton.setBackground(Color.WHITE);

            // Remove border around the icons
            editButton.setBorderPainted(false);
            deleteButton.setBorderPainted(false);

            editButton.setContentAreaFilled(false);
            deleteButton.setContentAreaFilled(false);

            add(editButton);
            add(deleteButton);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            table.setRowHeight(row, 24);  // Set row height
            return this;
        }
    }

    // Modify the ButtonEditor to handle actions based on the clicked icon
    static class ActionEditor extends DefaultCellEditor {

        protected JPanel panel;

        private JScrollPane scrollPane;

        public ActionEditor(JCheckBox checkBox, JTable table, String username, JFrame frameIn, String carName, JScrollPane scrollPane) {
            super(checkBox);
            this.scrollPane = scrollPane;

            panel = new JPanel();
            panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));

            // Set the background color of the panel to white
            panel.setBackground(Color.WHITE);

            JButton editButton = new JButton(new ImageIcon(Objects.requireNonNull(MainPage.class.getResource("/icons/editFuel.png"))));
            JButton deleteButton = new JButton(new ImageIcon(Objects.requireNonNull(MainPage.class.getResource("/icons/deleteFuel.png"))));

            // Set the background color of the buttons to white
            editButton.setBackground(Color.WHITE);
            deleteButton.setBackground(Color.WHITE);

            // Remove border around the icons
            editButton.setBorderPainted(false);
            deleteButton.setBorderPainted(false);

            editButton.setContentAreaFilled(false);
            deleteButton.setContentAreaFilled(false);

            editButton.addActionListener(e -> {
                handleUpdate(frameIn, username, (String) table.getModel().getValueAt(table.getSelectedRow(), 1),
                        (int) table.getModel().getValueAt(table.getSelectedRow(), 0),
                        (String) table.getModel().getValueAt(table.getSelectedRow(), 2),
                        (String) table.getModel().getValueAt(table.getSelectedRow(), 3),
                        (String) table.getModel().getValueAt(table.getSelectedRow(), 4),
                        (String) table.getModel().getValueAt(table.getSelectedRow(), 5),
                        (String) table.getModel().getValueAt(table.getSelectedRow(), 6),
                        (String) table.getModel().getValueAt(table.getSelectedRow(), 7),
                        table
                );
                fireEditingStopped();
            });


            deleteButton.addActionListener(e -> {
                handleDelete(frameIn, username, (String) table.getModel().getValueAt(table.getSelectedRow(), 1),
                        (int) table.getModel().getValueAt(table.getSelectedRow(), 0));
                int height = refreshData(table, username, carName);
                scrollPane.setPreferredSize(new Dimension(640, height));
                frameIn.pack(); // Pack the frame to adjust its size
                fireEditingStopped();
            });

            panel.add(editButton);
            panel.add(deleteButton);
        }

        public static int refreshData(JTable table, String username, String carName) {
            JsonNode fuelRecords = parseJsonString(Setup.getHistoryCar(username)); // Replace with your method to get updated data

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);

            List<JsonNode> fuelRecordsList = convertToList(fuelRecords);
            sortRecordsByDateDescending(fuelRecordsList);

            // Populate the table model with updated data
            for (JsonNode record : fuelRecordsList) {
                String fuelDate = record.get("fuelDate").asText();
                String recordName = record.get("carName").asText();
                boolean isDefault = Objects.equals(recordName, carName.trim());
                if (!HistoryPage.filtered[0] || isDefault) {
                    model.addRow(new Object[]{
                            record.get("id").asInt(),
                            "  " + record.get("carName").asText(),
                            "  " + formatDateString(fuelDate),
                            "  " + formatDoubleString(String.valueOf(record.get("literPrice").asDouble())) + " €",
                            "  " + formatDoubleString(String.valueOf(record.get("price").asDouble())) + " €",
                            "  " + formatDoubleString(String.valueOf(record.get("liters").asDouble())),
                            "  " + record.get("brandName").asText(),
                            "  " + record.get("totalKms").asInt(),
                            "Edit/Remove"
                    });
                }
            }


            // Notify the table that the data has changed
            model.fireTableDataChanged();
            int rowCount = table.getRowCount();
            int rowHeight = table.getRowHeight();
            int preferredHeight = Math.min( rowCount * ( rowHeight + 6) + 50, 600); // Set a maximum height, adjust as needed
            return preferredHeight;
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            // Set the background color of the panel to match the row background color
            panel.setBackground(table.getSelectionBackground());
            return panel;
        }

        public Object getCellEditorValue() {
            return "";
        }



        private void handleUpdate(JFrame frame, String username, String carName, int id, String fuelDate,
                                  String literPrice, String price, String liters, String brandName, String totalKms, JTable table) {
            launchUpdateCarPage(frame, username, carName, id, fuelDate, literPrice, price, liters, brandName, totalKms, table);

        }

        private static void launchUpdateCarPage(JFrame frameIn, String username, String carName, int id, String fuelDate,
                                                String literPrice, String price, String liters, String brandName, String totalKms, JTable table) {
            SwingUtilities.invokeLater(() -> new UpdateFuelPage(frameIn, username, carName, id, fuelDate, literPrice, price, liters, brandName, totalKms, table));
        }

        private void handleDelete(JFrame frame, String username, String carName, int id) {
            if (utils.Popup.confirmDecision("Are you sure you want to delete the record?") == JOptionPane.YES_OPTION) {
                Setup.deleteCarFuel(frame, username, carName, id);
                Messenger.Popup(frame, "Fuel removed.", "I");
            }
        }

        public boolean stopCellEditing() {
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }


   /* public static void main(String[] args) {
        String history = "[{\"id\":2,\"carName\":\"polo\",\"price\":301.0,\"liters\":25.0,\"totalKms\":1050011,\"literPrice\":1.813,\"brandName\":\"Galp\",\"fuelDate\":\"Wed Dec 06 00:00:00 WET 2023\"},{\"id\":2,\"carName\":\"polo\",\"price\":30.0,\"liters\":25.0,\"totalKms\":105000,\"literPrice\":1.8,\"brandName\":\"Galp\",\"fuelDate\":\"Wed Dec 06 00:00:00 WET 2023\"},{\"id\":3,\"carName\":\"polo\",\"price\":50.0,\"liters\":20.0,\"totalKms\":19950,\"literPrice\":1.654,\"brandName\":\"Galp\",\"fuelDate\":\"Thu Dec 07 00:54:49 WET 2023\"},{\"id\":1,\"carName\":\"Seat Arona\",\"price\":1.0,\"liters\":4.0,\"totalKms\":3,\"literPrice\":2.0,\"brandName\":\"Galp\",\"fuelDate\":\"Thu Dec 07 01:02:17 WET 2023\"},{\"id\":2,\"carName\":\"Seat Arona\",\"price\":1.0,\"liters\":4.0,\"totalKms\":3,\"literPrice\":2.0,\"brandName\":\"Galp\",\"fuelDate\":\"Thu Dec 07 14:17:55 WET 2023\"}]";

        new HistoryPage(new JFrame(), history, "alex", "polo");

        Date date = new Date();
        System.out.println(date());

    }*/


}
