package be.kdg.groepa.model;

import be.kdg.groepa.helpers.CostManager;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;
import org.threeten.bp.LocalDateTime;

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

    @Column(name="distance")
    private double distance;

    @Column(name="date")
    @Type(type="org.jadira.usertype.dateandtime.threetenbp.PersistentLocalDateTime")
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name="trajectId")
    private Traject traject;

    public Ride () {}

    public Ride(double cost, double distance)
    {
        this.totalCost = cost;
        this.distance = distance;
    }

    public Ride(Route r, LocalDateTime date)
    {
        this.totalCost = CostManager.calculateCost(r);
        this.distance = CostManager.getTotalDistance(r);
        this.date = date;
    }

    public Traject getTraject() {
        return traject;
    }

    public void setTraject(Traject traject) {
        this.traject = traject;
    }
}
