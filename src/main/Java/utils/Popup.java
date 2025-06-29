package utils;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Popup {

    public static void newPopup(Frame frame, String message, String msgType) {
        if (Objects.equals(msgType, "E")) {
            JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
        } else if (Objects.equals(msgType, "W")) {
            JOptionPane.showMessageDialog(frame, message, "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (Objects.equals(msgType, "+")) {
            JOptionPane.showMessageDialog(frame, message);
        } else {
            JOptionPane.showMessageDialog(frame, message, "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    public static int confirmDecision(String question) {
        return JOptionPane.showConfirmDialog(null, question, "Confirmation", JOptionPane.YES_NO_OPTION);
    }

    public static void popup(Frame frame, String message, String msgType) {
        newPopup(frame, message, msgType);
    }
}
