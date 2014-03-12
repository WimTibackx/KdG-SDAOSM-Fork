package be.kdg.groepa.model;

import be.kdg.groepa.helpers.ImageHelper;

import javax.persistence.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pieter-Jan on 5-2-14.
 */
@Entity
@Table(name="t_car")
public class Car {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer carId;

    @Column(name="name")
    private String brand;

    @Column(name="type")
    private String type;

    @Column(name="consumption")
    private double consumption;

    @Column(name="fuelType")
    private FuelType fuelType;

    @ManyToOne
    @JoinColumn(name="userId", nullable = true)
    private User user;

    @OneToMany
    @JoinColumn(name="routeId")
    //@Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Route> routes = new ArrayList<Route>();

    @Column(name="pictureURL", nullable = true)
    private String pictureURL;

    public Car(){}

    public Car(String brand, String type, double cons, FuelType fuelType) {
        this.brand = brand;
        this.type = type;
        this.consumption = cons;
        this.fuelType = fuelType;
    }

    public Car(String brand, String type, double cons, FuelType fuelType, File picture) {
        this.brand = brand;
        this.type = type;
        this.consumption = cons;
        this.fuelType = fuelType;
        this.pictureURL = ImageHelper.writeCarImage(picture, brand + type);
    }

    public Integer getCarId() {
        return carId;
    }

    public String getBrand() {
        return brand;
    }

    public String getType() {
        return type;
    }

    public double getConsumption() {
        return consumption;
    }

    public void addRoute(Route r)
    {
        this.routes.add(r);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public void removeImage(){
        ImageHelper.removeImage(pictureURL);
        this.pictureURL = null;
    }

    public User getUser() {
        return user;
    }

    public enum FuelType{
        SUPER95,
        SUPER98,
        DIESEL
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setConsumption(double consumption) {
        this.consumption = consumption;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }
}
