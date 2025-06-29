package utils;
import assemblers.Setup;
import domains.User;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

public class Checks {

    // Method to check internet connectivity
    public static boolean isInternetAvailable() {
        try {
            URL url = new URL("http://www.google.com"); // You can change this URL to a reliable one
            URLConnection connection = url.openConnection();
            connection.connect();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean isPassword(String user, String password) {
        return Setup.comparePassword(user, password);
    }

}
