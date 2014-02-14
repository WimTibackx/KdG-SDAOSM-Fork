package be.kdg.groepa.model;

import javax.persistence.*;

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

    public Car(){}

    public Car(String brand, String type, double cons) {
        this.brand = brand;
        this.type = type;
        this.consumption = cons;
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

    // private String pictureURL;

}
