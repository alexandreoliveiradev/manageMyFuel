package assemblers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import domains.Car;
import domains.Fuel;
import domains.User;
import utils.Checks;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Setup {
    static ArrayList <User> users = new ArrayList<>();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

    public static String getUser(Frame frame, String username, String password) {
        if (users.isEmpty()){
            if (Objects.equals(createUser(frame, username, password), "0")) {
                return "0";
            } else {
                JSON.createUserJSON(frame, getUsers());
                return username;
            }
        } else {
            for (User user : users) {
                if (Objects.equals(user.getUserName(), username)) {
                    if (!Checks.isPassword(username, password)) {
                        Messenger.Popup(frame, "Wrong password, try again.", "W");
                        return "0";
                    } else {
                        Messenger.Popup(frame, "Welcome Back, " + username, "I");
                        return username;
                    }
                }
            }
            return createUser(frame, username, password);
        }

    }

    public static String createUser(Frame frame, String username, String password){
        if (!users.isEmpty()) {
            for (User user : users) {
                if (Objects.equals(user.getUserName(), username)) {
                    Messenger.Popup(frame, "Username already taken, choose another please.", "E");
                    return "0";
                }
            }
        }
        User newUser = new User(username, password);
        users.add(newUser);
        JSON.createUserJSON(frame, getUsers());
        Messenger.Popup(frame, "Welcome " + username, "I");
        return username;
    }

    public static boolean comparePassword(String username, String password){
        return Objects.requireNonNull(getUserFromUsername(username)).comparePassword(password);
    }

    public static String getUserDefaultCar(String username){
        for (User user : users) {
            if (Objects.equals(user.getUserName(), username)) {
                return user.getDefaultCar();
            }
        }
        return "Error";
    }

    public static String createCar(JFrame frame, String username, String carName, String observations,
                                   char isDefault, String fuelType, Date nextInspection){
        for (User user : users){
            if (Objects.equals(user.getUserName(), username)){
                String ret = user.createCar(frame,carName, observations, isDefault, fuelType, nextInspection);
                if (!Objects.equals(ret, "0")) {
                    Messenger.Popup(frame, "Car " + carName + " added.", "I");
                    return "1";
                }
                break;
            }
        }
        return "0";
    }

    public static void deleteUser(JFrame frame, String username){
        for (User user : users) {
            if (Objects.equals(user.getUserName(), username)){
                users.remove(user);
                JSON.createUserJSON(frame, getUsers());
                Messenger.Popup(frame, "Thanks for using my app, " + username + ".", "I");
                return;
            }
        }
    }

    public static void removeCar(JFrame frame, String username, String carName){
        for (User user : users){
            if (Objects.equals(user.getUserName(), username)){
                user.removeCar(carName);
                if (user.getNumCars() > 0) {
                    user.updateFavorite(user.getCarNames().get(0));
                }
                JSON.createUserJSON(frame, Setup.getUsers());
                Messenger.Popup(frame, "Car " + carName + " removed.", "I");
                break;
            }
        }
    }

    public static int getNumberCars(String username){
        for (User user : users){
            if (Objects.equals(user.getUserName(), username)){
               return user.getNumCars();
            }
        }
        return 0;
    }


    public static ArrayList<String> getUserCarNames(String username) {
        return Objects.requireNonNull(getUserFromUsername(username)).getCarNames();
    }

    public static void updateUserDefaultCar(JFrame updateCarFrame, String username, String newDefaultCar) {
        String defaultCar = getUserDefaultCar(username);
        Objects.requireNonNull(getUserFromUsername(username)).updateFavorite(newDefaultCar);
        JSON.createUserJSON(updateCarFrame, Setup.getUsers());
        if (!Objects.equals(defaultCar, newDefaultCar)) {
            Messenger.Popup(updateCarFrame, "New favorite car updated to " + newDefaultCar + ".", "I");
        }

    }

    public static String getCarObservations(String username, String carName) {
        return Objects.requireNonNull(getUserFromUsername(username)).getCarObservation(carName);
    }

    private static User getUserFromUsername(String username){
        for (User user : users){
             if (Objects.equals(user.getUserName(), username)) {
              return user;
            }
        }
        return null;
    }

    public static void updateCarObservations(String username, String carName, String editedObservations) {
        Objects.requireNonNull(getUserFromUsername(username)).updateCarObservations(carName, editedObservations);

    }

    public static ArrayList<User> getUsers() {
        return new ArrayList<>(users);
    }

    public static void createUsersFromJson(JFrame frame) {
        String json = JSON.readUserJSON(frame);

        if (!json.equals("0")) {
            for (User user : createUsersFromStringJson(json)) {
                if (!users.contains(user)){
                    users.add(user);
                }
            }
        }
    }

    public static String createUserFromStringJson(String json) {
        ArrayList<User> newUsers = createUsersFromStringJson(json);
        for (User user : newUsers){
            if (!users.contains(user)) {
                users.add(user);
            } else {
                Objects.requireNonNull(getUserFromUsername(user.getUserName())).updateUser(user);
            }
            return "0";
        }
        return "1";
    }

    public static ArrayList<User> createUsersFromStringJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<User> users = new ArrayList<>();

        try {
            JsonNode jsonNode = objectMapper.readTree(json);

            if (jsonNode.isArray()) {
                for (JsonNode userNode : jsonNode) {
                    User user = createUser(userNode);
                    users.add(user);
                }
            } else {
                // If it's not an array, create a single user
                User user = createUser(jsonNode);
                users.add(user);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace(); // Handle the exception according to your needs
            Messenger.Popup(new JFrame(), e.getMessage(), "E");
        }
        return users;
    }

    private static User createUser(JsonNode jsonNode) throws ParseException, JsonProcessingException {
        String username = jsonNode.get("userName").asText();
        String encryptedPassword = jsonNode.get("password").asText();

        ArrayList<String> brands = new ArrayList<>();
        JsonNode brandsNode = jsonNode.get("carBrands");
        if (brandsNode != null && brandsNode.isArray()) {
            for (JsonNode brandNode : brandsNode) {
                String brand = brandNode.asText();
                brands.add(brand);
            }
        }

        ArrayList<Car> cars = new ArrayList<>();
        JsonNode carsNode = jsonNode.get("cars");
        if (carsNode != null && carsNode.isArray()) {
            for (JsonNode carNode : carsNode) {
                String carName = carNode.get("carName").asText();
                String observations = carNode.get("observations").asText();
                String fuelType = carNode.get("fuelType").asText();
                String nextInspectionStr = carNode.get("nextInspection").asText();
                long millis = Long.parseLong(nextInspectionStr);
                Date nextInspection = new Date(millis);
                char isDefault = carNode.get("default").asBoolean() ? 'Y' : 'N';

                ArrayList<Fuel> fuels = new ArrayList<>();
                JsonNode fuelsNode = new ObjectMapper().readTree(carNode.get("fuels").asText());

                if (fuelsNode != null && fuelsNode.isArray()) {
                    for (JsonNode fuelNode : fuelsNode) {
                        String dateStr = fuelNode.get("fuelDate").asText();
                        Date fuelDate = DATE_FORMAT.parse(dateStr);
                        float price = (float) fuelNode.get("price").asDouble();
                        float literPrice = (float) fuelNode.get("literPrice").asDouble();
                        int totalKms = fuelNode.get("totalKms").asInt();
                        int liters = fuelNode.get("liters").asInt();
                        int id = fuelNode.get("id").asInt();

                        String brandName = fuelNode.get("brandName").asText();

                        Fuel fuel = new Fuel(fuelDate, price, literPrice, totalKms, liters, brandName, id);
                        fuels.add(fuel);
                    }
                }

                Car car = new Car(carName, observations, isDefault, fuels, brands, fuelType, username, nextInspection);
                cars.add(car);
            }
        }

        return new User(username, encryptedPassword, cars);
    }


    public static String exportUserJSON(String userName){
       return JSON.exportUserJSON(getUserFromUsername(userName));
    }
    public static ArrayList<String> getUserCarBrands(String username) {
       return Objects.requireNonNull(getUserFromUsername(username)).getCarBrands();
    }

    public static void addFuelToCar(Frame addFuelFrame, String username, String carName, Date fuelDate, float price, float literPrice, int totalKms, float liters, String brandName) {
        Objects.requireNonNull(getUserFromUsername(username)).addCarFuel(carName, fuelDate, price, literPrice, totalKms, liters, brandName);
        JSON.createUserJSON(addFuelFrame, Setup.getUsers());
    }

    public static void updateUserPassword(JFrame inputFrame, String username, String newPassword) {
        String encryptedPassword = Encripter.encryptText(newPassword);
        Objects.requireNonNull(getUserFromUsername(username)).updatePassword(encryptedPassword);
        JSON.createUserJSON(inputFrame, Setup.getUsers());
        Messenger.Popup(inputFrame, "Password updated.", "I");
    }

    public static String getUserCarFuelType(String username, String carName) {
       return Objects.requireNonNull(getUserFromUsername(username)).getCarFuelType(carName);
    }

    public static String getHistoryCar(String username){
        String output = Objects.requireNonNull(getUserFromUsername(username)).getCarFuels();
        if (!Objects.equals(output, "[]")){
            return output;
        }
        return "Start adding some fuels!";
    }

    public static boolean getMoreThanZeroCarFuels(String username, String carName){
        return Objects.requireNonNull(getUserFromUsername(username)).getMoreThanZeroCarFuels(carName);
    }

    public static void deleteCarFuel(JFrame inputFrame, String username, String carName, int id) {
        Objects.requireNonNull(getUserFromUsername(username)).deleteCarFuel(carName, id);
        JSON.createUserJSON(inputFrame, Setup.getUsers());
    }

    public static String updateCarFuel(JFrame inputFrame, String username, String carName, int id, Date fuelDate,
                                     double literPrice, double price, double liters, String brandName, int totalKms) {
        if (Objects.equals(Objects.requireNonNull(getUserFromUsername(username)).updateCarFuel(carName, id,
                fuelDate, literPrice, price, liters, brandName, totalKms), "0")){
            JSON.createUserJSON(inputFrame, Setup.getUsers());
            return "0";
        }
        return "1";
    }

    public static boolean hasTwoCarWithFuels(String username) {
        return Objects.requireNonNull(getUserFromUsername(username)).hasTwoCarWithFuels();
    }

    public static float getCarMonthSpend(String username, String carName, int month) {
        float output = 0.f;
        if (carName == null){
           User user = getUserFromUsername(username);
           output += user.getMonthSpend(null, month);
        } else {
           output = getUserFromUsername(username).getMonthSpend(carName, month);
        }
        return output;
    }

    public static int getMonthNumberFuels(String username, String carName, int month) {
        int output;
        if (carName == null){
            output = Objects.requireNonNull(getUserFromUsername(username)).getMonthNumberFuels(null, month);
        } else {
            output = Objects.requireNonNull(getUserFromUsername(username)).getMonthNumberFuels(carName, month);
        }
        return output;
    }

    public static float getMonthMinPrice(String username, String carName, int month) {
        float output;
        if (carName == null){
            output = Objects.requireNonNull(getUserFromUsername(username)).getMonthMinPrice(null, month);
        } else {
            output = Objects.requireNonNull(getUserFromUsername(username)).getMonthMinPrice(carName, month);
        }
        return output;
    }

    public static float getMonthMaxPrice(String username, String carName, int month) {
        float output;
        if (carName == null){
            output = Objects.requireNonNull(getUserFromUsername(username)).getMonthMaxPrice(null, month);
        } else {
            output = Objects.requireNonNull(getUserFromUsername(username)).getMonthMaxPrice(carName, month);
        }
        return output;
    }

    public static Date getNextInspectionByCar(String username, String carName) {
        return Objects.requireNonNull(getUserFromUsername(username)).getNextInspection(carName);
    }

    public static void updateCarNextInspection(String username, String carName, Date revisionDate) {
        Objects.requireNonNull(getUserFromUsername(username)).setNextInspection(carName, revisionDate);
    }
}
