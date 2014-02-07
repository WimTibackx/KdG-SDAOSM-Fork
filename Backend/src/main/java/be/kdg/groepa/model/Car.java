package be.kdg.groepa.model;

import javax.persistence.*;

/**
 * Created by Pieter-Jan on 5-2-14.
 */
// @Entity
// @Table(name="t_car")
public class Car {

    // @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // @Column(name="name")
    private String brand;

    // @Column(name="type")
    private String type;

    // @Column(name="consumption")
    private double consumption;

    // @ManyToOne
    // @JoinColumn(name="userId", nullable = false)
    private User user;

    public Car(String brand, String type, double cons) {
        this.brand = brand;
        this.type = type;
        this.consumption = cons;
    }
    // private String pictureURL;

}
