package assemblers;

import com.fasterxml.jackson.databind.ObjectMapper;
import utils.Formatter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

public class JSON {
    public static void createUserJSON(Frame frame, Object object) {

        // Convert object to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(object);
            String encodedJson = Encripter.encodeToB64(json);
            File file = new File("src/main/resources/data/users.txt");
            Files.write(file.toPath(), encodedJson.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            Messenger.Popup(frame, "Error writing to file, try again later.", "E");
        }
    }

    public static String exportUserJSON(Object object) {

        // Convert object to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(object);
            return  Encripter.encodeToB64(json);
        } catch (IOException e) {
            e.printStackTrace();
            Messenger.Popup(new Frame("Error"), "Error exporting to file, try again later.", "E");
        }
        return "[]";
    }

    public static String saveJsonToFile(Frame frame, String jsonStr, String username) throws UnsupportedLookAndFeelException {

        LookAndFeel ui = UIManager.getLookAndFeel();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            return "1";
        }

        JFileChooser fileChooser = new JFileChooser();
        String today = Formatter.getFormattedDate(new Date());
        String suggestedFileName = "MyFuel_" + username + "_" + today + ".json";
        fileChooser.setSelectedFile(new File(suggestedFileName));

        FileNameExtensionFilter jsonFilter = new FileNameExtensionFilter("JSON Files (*.json)", "json");
        fileChooser.addChoosableFileFilter(jsonFilter);
        fileChooser.setFileFilter(jsonFilter);

        fileChooser.setDialogTitle("Save JSON File");
        int userSelection = fileChooser.showSaveDialog(frame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                writer.write(jsonStr);
                UIManager.setLookAndFeel(ui);
                return "0";
            } catch (IOException e) {
                e.printStackTrace();
                UIManager.setLookAndFeel(ui);
                return "1";
            }
        } else {
            UIManager.setLookAndFeel(ui);
            return "-1";
        }
    }

    public static String getJsonFromFile(Frame frame) throws UnsupportedLookAndFeelException, IOException {

        LookAndFeel ui = UIManager.getLookAndFeel();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            return "1";
        }

        // Create a file chooser
        JFileChooser fileChooser = new JFileChooser();

        // Set the default file name and type
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON files", "json"));

        // Show the dialog to choose a file
        int result = fileChooser.showSaveDialog(frame);

        // If the user selects a file and clicks "Save"
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            // Get the absolute path of the selected file
            try {
                UIManager.setLookAndFeel(ui);
            } catch (UnsupportedLookAndFeelException e) {
                Messenger.Popup(frame, e.getMessage(), "E");
                return "1";
            }
            String file = Files.readString(Path.of(selectedFile.getAbsolutePath()));
            return Decripter.decodeB64(file);
        } else {
            return "0";
        }
    }

    public static String readUserJSON(Frame frame) {
        try {
            // Read JSON from a file
            Path filePath = Path.of("src/main/resources/data/users.txt");

            if (Files.notExists(filePath)) {
                Files.createDirectories(filePath.getParent());
                Files.createFile(filePath);
                // optionally write an empty or default content
                Files.writeString(filePath, "");
            }


            String json = Files.readString(filePath);
            String decodedJson = Decripter.decodeB64(json);

            if (!json.isEmpty()) {
                return decodedJson;
            }

        } catch (IOException e) {
            e.printStackTrace();
            Messenger.Popup(frame, "Error reading from file: " + e.getMessage(), "E");
        }
        return "0";
    }

}
