package domains;

import java.util.Date;

public class Fuel {

    private Date fuelDate;

    private float price;

    private float literPrice;

    private int totalKms;

    private float liters;

    private String brand_name;

    private final int id;

    private static int nextId = 1;

    public Fuel(Date fuelDate, float price, float literPrice, int totalKms, float liters, String brandName, int id) {
        this.fuelDate = fuelDate;
        this.price = price;
        this.literPrice = literPrice;
        this.totalKms = totalKms;
        this.liters = liters;
        brand_name = brandName;
        this.id = id;
    }

    public Fuel(Date fuelDate, float price, float literPrice, int totalKms, float liters, String brandName) {
        this.fuelDate = fuelDate;
        this.price = price;
        this.literPrice = literPrice;
        this.totalKms = totalKms;
        this.liters = liters;
        brand_name = brandName;
        this.id = getNextId();
    }

    private int getNextId() {
        return nextId++;
    }

    public Date getFuelDate() {
        return fuelDate;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public float getliterPrice() {
        return literPrice;
    }

    public int getTotalKms(){
        return totalKms;
    }

    public float getliters() {
        return liters;
    }

    public float getPrice() {
        return price;
    }

    public int getId(){ return id; }

    public String getFuelSt() {
        return "{\"id\":" + getId()
                + ", \"carName\":" + "\"car_name\""
                + ", \"price\":" + getPrice()
                + ", \"liters\":" + getliters()
                + ", \"totalKms\":" + getTotalKms()
                + ", \"literPrice\":" + getliterPrice()
                + ", \"brandName\":\"" + getBrand_name() + "\""
                + ", \"fuelDate\":\"" + getFuelDate() + "\""
                + "}";
    }

    public String updateFuel(Date fuelDate, double literPrice, double price,
                             double liters, String brandName, int totalKms) {
        String oldFuel = this.fuelDate.toString()+this.price+this.totalKms+this.liters+this.brand_name;
        String newFuel = fuelDate.toString()+price+totalKms+liters+brandName;

        double diffDouble = Math.abs(literPrice - this.literPrice);

        if (oldFuel.equals(newFuel) && diffDouble<0.001) {
            return "1";
        }

        this.fuelDate = fuelDate;
        this.price = (float) Math.round(price * 100) / 100;
        this.literPrice = (float) Math.round(literPrice * 1000) / 1000;
        this.totalKms = totalKms;
        this.liters = (float) Math.round(liters * 10) / 10;
        this.brand_name = brandName;

        return "0";
    }

}
