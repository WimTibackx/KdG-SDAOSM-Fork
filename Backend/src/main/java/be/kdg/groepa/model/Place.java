package be.kdg.groepa.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "place")
    private List<PlaceTime> placeTimes = new ArrayList<>();

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

    protected void addPlaceTime(PlaceTime pt)
    {
        this.placeTimes.add(pt);
    }

    public List<PlaceTime> getPlaceTimes() {
        return placeTimes;
    }
}
