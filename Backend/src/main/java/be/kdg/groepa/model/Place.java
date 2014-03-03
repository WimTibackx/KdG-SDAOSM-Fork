package be.kdg.groepa.model;

import javax.persistence.*;
import java.util.ArrayList;

/**
 * Created by Pieter-Jan on 14-2-14.
 */
@Entity
@Table(name="t_place")
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int placeId;

    @Column(name="name")
    private String name;

    @Column(name="lat")
    private double lat;

    @Column(name="lon")
    private double lon;

    @JoinColumn(name="placetimeId")
    private ArrayList<PlaceTime> placeTime;

    public Place() {}

    public Place(String name, double lat, double lon) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
