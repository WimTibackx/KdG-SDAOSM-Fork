package be.kdg.groepa.model;

import be.kdg.groepa.helpers.ImageHelper;

import javax.persistence.*;
import java.io.File;

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

    @ManyToOne
    @JoinColumn(name="userId", nullable = true)
    private User user;

    @Column(name="pictureURL")
    private String pictureURL;

    public Car(){}

    public Car(String brand, String type, double cons) {
        this.brand = brand;
        this.type = type;
        this.consumption = cons;
    }

    public Car(String brand, String type, double cons, File picture) {
        this.brand = brand;
        this.type = type;
        this.consumption = cons;
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



    public void setUser(User user) {
        this.user = user;
    }

}
