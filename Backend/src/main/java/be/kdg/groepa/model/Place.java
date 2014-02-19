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
    private float lat;

    @Column(name="lon")
    private float lon;

    @JoinColumn(name="placetimeId")
    private ArrayList<PlaceTime> placeTime;

    public Place() {}

    public Place(String name, float lat, float lon) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }
}
