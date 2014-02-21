package be.kdg.groepa.dtos;

import be.kdg.groepa.model.Car;

/**
 * Created by Thierry on 14/02/14.
 */
public class CarDTO {
    private Integer carId;

    private String brand;

    private String type;

    private double consumption;

    private Car.FuelType fuelType;

    private String photoUrl;

    public CarDTO() {}

    public CarDTO(Car car) {
        this.carId = car.getCarId();
        this.brand = car.getBrand();
        this.type = car.getType();
        this.consumption = car.getConsumption();
        this.fuelType = car.getFuelType();
        this.photoUrl = car.getPictureURL();
    }

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getConsumption() {
        return consumption;
    }

    public void setConsumption(double consumption) {
        this.consumption = consumption;
    }

    public Car.FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(Car.FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
