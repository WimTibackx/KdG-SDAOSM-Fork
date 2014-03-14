package be.kdg.groepa.model;

import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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
    private PlaceTime pickup;

    @OneToOne
    @JoinColumn(name="dropoffId")
    private PlaceTime dropoff;

    @ManyToOne
    @JoinColumn(name="routeId")
    private Route route;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    @OneToMany(mappedBy="traject")
    @Cascade(CascadeType.ALL)
    private List<Ride> rides = new ArrayList<>();

    @Column(name="isAccepted")
    private boolean isAccepted;

    @ManyToOne
    @JoinColumn(name="weekdayrouteId")
    private WeekdayRoute weekdayRoute;

    public Traject(){

    }

    public Traject(PlaceTime pointA, PlaceTime pointB, Route route, User user) {
        pointA.addTraject(this);
        pointB.addTraject(this);
        this.pickup = pointA;
        this.dropoff = pointB;
        this.isAccepted = false;
        this.route = route;
        this.user = user;
    }

    public Traject(PlaceTime pointA, PlaceTime pointB, Route route, User user, WeekdayRoute weekdayRoute) {
        this(pointA, pointB, route, user);
        this.weekdayRoute = weekdayRoute;
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

    public WeekdayRoute getWeekdayRoute() {
        return weekdayRoute;
    }

    public void setWeekdayRoute(WeekdayRoute weekdayRoute) {
        this.weekdayRoute = weekdayRoute;
    }

    public User getUser() {
        return user;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

    public void addRide(Ride r)
    {
        this.rides.add(r);
    }

    public List<Ride> getRides() {
        return rides;
    }
}
