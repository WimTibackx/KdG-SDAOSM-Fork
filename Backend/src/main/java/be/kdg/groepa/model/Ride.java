package be.kdg.groepa.model;

import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pieter-Jan on 25-2-14.
 */

@Entity
@Table(name="t_ride")
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rideId;

    @Column(name="totalCost")
    private double totalCost;

    @OneToMany(mappedBy="ride")
    @Cascade(CascadeType.ALL)
    private List<Traject> trajecten = new ArrayList<>();

    public Ride () {}

    public Ride(double cost)
    {
        this.totalCost = cost;
    }

    public void addTraject(Traject t)
    {
        trajecten.add(t);
    }
}