package be.kdg.groepa.model;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pieter-Jan on 14-2-14.
 */

@Entity
@Table(name="t_weekdayroute")
public class WeekdayRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int weekdayrouteId;

    @Column(name="day")
    private int day;     // day 0 to 6: monday to sunday

    // foreign keys
    @OneToMany(mappedBy="weekdayRoute")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<PlaceTime> placeTimes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="routeId", nullable = true)
    private Route route;

    public WeekdayRoute() {}

    public WeekdayRoute(int day)
    {
        this.day = day;
    }

    public WeekdayRoute(Route route, int day) {
        this.route = route;
        this.day = day;
    }

    public void addPlaceTime(PlaceTime pt)
    {
        this.placeTimes.add(pt);
        pt.setWeekdayRoute(this);
    }

    public void setRoute(Route r)
    {
        this.route = r;
    }
}
