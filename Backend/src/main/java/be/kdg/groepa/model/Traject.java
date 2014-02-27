package be.kdg.groepa.model;


import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Tim on 19/02/14.
 */
@Entity
@Table(name = "t_traject")
public class Traject {
    // Een traject is een verzameling van 2 PlaceTimes: een begin- en eindpunt
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name="pickupId", nullable = false)
    @Cascade(CascadeType.ALL)
    private PlaceTime pickup;

    @OneToOne
    @JoinColumn(name="dropoffId")
    @Cascade(CascadeType.ALL)
    private PlaceTime dropoff;

    @ManyToOne
    @JoinColumn(name="routeId")
    private Route route;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    @ManyToOne
    @JoinColumn(name="rideId")
    private Ride ride;

    @Column(name="isAccepted")
    private boolean isAccepted;

    public Traject(){

    }

    public Traject(PlaceTime pointA, PlaceTime pointB, Route route, User user) {
        pointA.setTraject(this);
        pointB.setTraject(this);
        this.pickup = pointA;
        this.dropoff = pointB;
        this.isAccepted = false;
        this.route = route;
        this.user = user;
    }

    public void setRide(Ride r){
        this.ride = r;
    }

    public PlaceTime getPickup() { return pickup; }

    public PlaceTime getDropoff() {
        return dropoff;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Route getRoute() {
        return route;
    }

    public int getId() {
        return id;
    }
}
