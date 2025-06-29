package domains;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Car {

    final String carName;
    private String observations;
    private char isDefault;

    public ArrayList <Fuel> fuels;

    public ArrayList <String> brands;

    public String fuelType;
    public String owner;

    private Date nextInspection;

    public Car (String carName, char isDefault, String observations, String fuelType, String owner, Date nextInspection){
        this.carName = carName;
        this.nextInspection = nextInspection;

        if (isDefault == 'Y') {
            this.isDefault = isDefault;
        } else {
            this.isDefault = 'N';
        }
        this.observations = observations;
        fuels = new ArrayList<>();
        brands = new ArrayList<>();
        this.fuelType = fuelType;
        this.owner = owner;
    }

    public Car(String carName, String observations, char isDefault, ArrayList<Fuel> fuels,
               ArrayList<String> brands, String fuelType, String owner, Date nextInspection) {
        this.carName = carName;
        this.observations = observations;
        this.isDefault = isDefault;
        this.fuels = fuels;
        this.brands = brands;
        this.fuelType= fuelType;
        this.owner = owner;
        this.nextInspection = nextInspection;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Car car = (Car) object;
        return Objects.equals(carName, car.carName) && Objects.equals(owner, car.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(carName, owner);
    }

    public boolean isDefault(){
        return this.isDefault == 'Y';
    }

    public void setDefault(char newDefault){
        this.isDefault = newDefault;
    }

    public String getFuelType(){
        return this.fuelType;
    }
    public String getObservations(){
        return this.observations;
    }

    public Date getNextInspection() { return this.nextInspection;}
    public void setNextInspection(Date nextInspection) {this.nextInspection = nextInspection;}

    public ArrayList<String> getBrands(){
        return this.brands;
    }

    public void updateObservations(String newObservations){
        this.observations = newObservations;
    }

    public String getCarName(){
        return this.carName;
    }

    public void addFuel(Date fuelDate, float price, float literPrice, int totalKms, float liters, String brandName) {
        if (liters == 0.0f){
            liters = price/literPrice;
        } else if (price == 0.0f) {
            price = liters*literPrice;
        } else if (literPrice == 0.0f){
            literPrice = liters/price;
        }
        Fuel newFuel = new Fuel(fuelDate, price, literPrice, totalKms, liters, brandName);
        fuels.add(newFuel);
        if (!brands.contains(brandName)){
            brands.add(brandName);
        }
    }

    public String getFuels() {
        StringBuilder output = new StringBuilder("[");
        for (Fuel fuel : fuels) {
            output.append(fuel.getFuelSt()).append(",");
        }
        // Remove the trailing comma if there are elements in the array
        if (!fuels.isEmpty()) {
            output.setLength(output.length() - 1);
        }
        output.append("]");

        return output.toString();
    }

    public void deleteFuelById(int id) {
        fuels.removeIf(fuel -> fuel.getId() == id);
    }

    public String updateFuelById(int id, Date fuelDate, double literPrice, double price,
                                 double liters, String brandName, int totalKms) {
        for (Fuel fuel : fuels){
            if (fuel.getId() == id){
                return fuel.updateFuel(fuelDate, literPrice, price, liters, brandName, totalKms);
            }
        }
        return "1";
    }

    public void updateCar(Car car) {
        for (String brand : car.getBrands()){
            if (!this.brands.contains(brand)){
                this.brands.add(brand);
            }
        }
        this.nextInspection = car.getNextInspection();
        this.fuelType = car.getFuelType();
        this.observations = car.getObservations();
        for (Fuel fuel : car.fuels){
            if (!this.fuels.contains(getFuelById(fuel.getId()))){
                fuels.add(fuel);
            } else {
                Fuel newFuel = new Fuel(fuel.getFuelDate(), fuel.getPrice(), fuel.getliterPrice(),
                        fuel.getTotalKms(), fuel.getliters(), fuel.getBrand_name());
                this.fuels.add(newFuel);
            }
        }
    }

    public Fuel getFuelById(int id){
        for (Fuel fuel : fuels){
            if (fuel.getId() == id) {
                return fuel;
            }
        }
        return null;
    }

    public float getMonthSpend(int month) {
        float output = 0.f;
        ArrayList <Fuel> monthFuels = getMonthFuels(month);
        for (Fuel fuel : monthFuels){
            output += fuel.getPrice();
        }
        return output;
    }

    public int getMonthNumberFuels(int month) {
        int count = 0;
        ArrayList <Fuel> monthFuels = getMonthFuels(month);
        for (Fuel ignore : monthFuels){
            count += 1;
        }
        return count;
    }

    public ArrayList<Fuel> getMonthFuels(int month){
        ArrayList <Fuel> monthFuels = new ArrayList<>();
        int inmonth;
        for (Fuel fuel : fuels){
            inmonth = fuel.getFuelDate().getMonth() + 1;
            if (inmonth==month){
                monthFuels.add(fuel);
            }
        }
        return monthFuels;
    }

    public float getMonthMaxPrice(int month) {
        float max = 0.0f;
        float price;
        ArrayList <Fuel> monthFuels = getMonthFuels(month);
        for (Fuel fuel : monthFuels){
            price = fuel.getliterPrice();
            if (price > max){
                max = price;
            }
        }
        return max;
    }

    public float getMonthMinPrice(int month) {
        float min = 100.0f;
        float price;
        ArrayList <Fuel> monthFuels = getMonthFuels(month);
        for (Fuel fuel : monthFuels){
            price = fuel.getliterPrice();
            if (price < min){
                min = price;
            }
        }
        if (min == 100.0f) {
            min = 0.0f;
        }
        return Math.min(min, 100.0f);
    }
}
