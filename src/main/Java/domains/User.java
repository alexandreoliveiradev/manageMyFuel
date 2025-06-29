package domains;

import assemblers.JSON;
import assemblers.Messenger;
import assemblers.Setup;
import utils.Crypter;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class User {
    private final String username;
    private String password;
    public ArrayList <Car> cars;

    public User(String username, String password){
        cars = new ArrayList<>();
        this.username = username;
        this.password = Crypter.encrypt(password);
    }

    public User(String username, String encryptedPassword, ArrayList<Car> cars) {
        this.cars = cars;
        this.username = username;
        this.password = encryptedPassword;
    }

    public String getDefaultCar(){
        for (Car car : cars) {
            if (car.isDefault()) {
                return car.carName;
            }
        }
        return "No car added yet.";
    }

    public ArrayList<String> getCarBrands(){
        ArrayList<String> brands = new ArrayList<>();
        for (Car car : cars){
            for (String brand : car.getBrands()) {
                if (!brands.contains(brand)) {
                    brands.add(brand);
                }
            }
        }
        return brands;
    }

    public void addCarFuel(String carName, Date fuelDate, float price, float literPrice, int totalKms, float liters, String brandName){
        for (Car car : cars) {
            if(Objects.equals(car.getCarName(), carName)){
                car.addFuel(fuelDate, price, literPrice, totalKms, liters, brandName);
            }
        }
    }

    public String createCar(JFrame frame, String carName, String observations,
                            char isDefault, String fuelType, Date nextInspection){
        for (Car car : cars){
            if (Objects.equals(car.getCarName(), carName)){
                Messenger.Popup(frame, "Car name taken, use another.", "W");
                return "0";
            }
        }

        if (cars.isEmpty()) {
            isDefault =  'Y';
        }
        if (isDefault == 'Y'){
            for (Car car : cars){
                car.setDefault('N');
            }
        }
        cars.add(new Car(carName, isDefault, observations, fuelType, username, nextInspection));
        JSON.createUserJSON(frame, Setup.getUsers());
        return "1";
    }

    public void removeCar(String carName){
        cars.removeIf(car -> Objects.equals(car.getCarName(), carName));
    }

    public int getNumCars(){
        return cars.size();
    }

    public void updatePassword(String newPassword){
        this.password = newPassword;
    }

    public String getUserName () {
        return this.username;
    }

    public String getPassword() {
        return String.valueOf(this.password);
    }

    public ArrayList<String> getCarNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Car car : cars){
            names.add(car.getCarName());
        }
        return names;
    }

    public void updateFavorite(String newDefaultCar) {
        for (Car car : cars) {
            if (Objects.equals(car.getCarName(), newDefaultCar)){
                car.setDefault('Y');
            } else {
                car.setDefault('N');
            }
        }
    }


    public String getCarObservation(String carName) {
        String output = Objects.requireNonNull(getCarByName(carName)).getObservations();
        if (output == null) {
            return "No observations.";
        } return output;
    }

    public void updateCarObservations(String carName, String editedObservations) {
        Objects.requireNonNull(getCarByName(carName)).updateObservations(editedObservations);
    }

    public boolean comparePassword(String tryPassword) {
        return Crypter.decrypt(this.getPassword()).equals(tryPassword);
    }

    public String getCarFuelType(String carName) {
        return Objects.requireNonNull(getCarByName(carName)).getFuelType();
    }

    private Car getCarByName(String carName){
        for (Car car : cars){
            if (Objects.equals(car.getCarName(), carName)) {
                return car;
            }
        }
        return null;
    }

    public boolean getMoreThanZeroCarFuels(String carname){
        for (Car car : cars){
            if (Objects.equals(car.getCarName(), carname) && !car.getFuels().equals("[]")){
              return true;
            }
        }
        return false;
    }

    public String getCarFuels() {
        StringBuilder allCars = new StringBuilder();
        int count = 0;
        for (Car car : cars){
            if (car.getFuels().equals("[]")){
                continue;
            }
            if (count > 0) {
                allCars.append(",");
            }
            allCars.append(car.getFuels().replace("car_name", car.getCarName()));
            count++;
        }
        return allCars.toString().replace("],[", ",");
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    public void deleteCarFuel(String carName, int id) {
        Objects.requireNonNull(getCarByName(carName.trim())).deleteFuelById(id);
    }

    public String updateCarFuel(String carName, int id, Date fuelDate, double literPrice,
                              double price, double liters, String brandName, int totalKms) {
        return Objects.requireNonNull(getCarByName(carName.trim())).updateFuelById(id, fuelDate,
                literPrice, price, liters, brandName.trim(), totalKms);
    }

    public boolean hasTwoCarWithFuels() {
        int count = 0;
        for (Car car : cars){
            if (!car.getFuels().equals("[]")){
                count++;
            }
            if (count > 1){
                return true;
            }
        }
        return false;
    }

    public void updateUser(User newUser) {
        ArrayList<Car> newCars = newUser.cars;
        for (Car car : newCars){
            if (!this.cars.contains(car)) {
                cars.add(car);
            } else {
                Objects.requireNonNull(getCarByName(car.getCarName())).updateCar(car);
            }
        }
    }

    public float getMonthSpend(String carName, int month) {
        float output = 0;
        if (carName==null){
            for (Car car : cars){
                output += car.getMonthSpend(month);
            }
        } else {
            Car car = getCarByName(carName);
            output = car.getMonthSpend(month);
        }
        return output;
    }

    public int getMonthNumberFuels(String carName, int month) {
        int output = 0;
        if (carName==null){
            for (Car car : cars){
                output += car.getMonthNumberFuels(month);
            }
        } else {
            output += Objects.requireNonNull(getCarByName(carName)).getMonthNumberFuels(month);
        }
        return output;
    }

    public float getMonthMinPrice(String carName, int month) {
        float output = 100.0f;
        float min;
        if (carName==null){
            for (Car car : cars){
                min = car.getMonthMinPrice(month);
                if (min < output && min != 0.0f){
                    output = min;
                }
            }
            if (output == 100.0f){
                output = 0.0f;
            }
        } else {
            output = Objects.requireNonNull(getCarByName(carName)).getMonthMinPrice(month);
        }
        return output;
    }

    public float getMonthMaxPrice(String carName, int month) {
        float output = 0;
        float max;
        if (carName==null){
            for (Car car : cars){
                max = car.getMonthMaxPrice(month);
                if (max > output){
                    output = max;
                }
            }
        } else {
            output = Objects.requireNonNull(getCarByName(carName)).getMonthMaxPrice(month);
        }
        return output;
    }

    public Date getNextInspection(String carName) {
        for (Car car : cars){
            if (Objects.equals(car.getCarName(), carName)) {
                return car.getNextInspection();
            }
        }
        return null;
    }

    public void setNextInspection(String carName, Date nextRevision) {
        for (Car car : cars){
            if (Objects.equals(car.getCarName(), carName)) {
                car.setNextInspection(nextRevision);
            }
        }
    }

}
