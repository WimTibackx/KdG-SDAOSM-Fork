package be.kdg.groepa.model;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
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

    @OneToMany(mappedBy = "traject")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<PlaceTime> placeTimes = new ArrayList<PlaceTime>(2);

    @ManyToOne
    @JoinColumn(name="routeId")
    private Route route;


    public Traject(PlaceTime pointA, PlaceTime pointB) {
        pointA.setTraject(this);
        pointB.setTraject(this);
        this.placeTimes.add(pointA);
        this.placeTimes.add(pointB);
    }

    public List<PlaceTime> getPlaceTimes() {
        return placeTimes;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
