package be.kdg.groepa.model;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @OneToMany(mappedBy="weekdayRoute", fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @OrderBy("time")
    private List<PlaceTime> placeTimes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="routeId", nullable = false)
    private Route route;

    @OneToMany(mappedBy = "weekdayRoute", fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<Traject> trajects = new HashSet<>();

    public void setPlaceTimes(List<PlaceTime> placeTimes) {
        this.placeTimes = placeTimes;
    }

    public WeekdayRoute() {}

    public WeekdayRoute(Route route, int day) {
        this.route = route;
        this.day = day;
    }

    protected void addPlaceTime(PlaceTime pt)
    {
        this.placeTimes.add(pt);
    }

    public void setRoute(Route r)
    {
        this.route = r;
    }

    public int getDay() {
        return day;
    }

    public List<PlaceTime> getPlaceTimes() {
        return placeTimes;
    }

    public Route getRoute() {
        return route;
    }

    public int getWeekdayrouteId() {
        return weekdayrouteId;
    }

    public void addTraject(Traject t) {
        this.trajects.add(t);
        t.setWeekdayRoute(this);
    }

    public Set<Traject> getTrajects() {
        return trajects;
    }

    public void setWeekdayrouteId(int weekdayrouteId) {
        this.weekdayrouteId = weekdayrouteId;
    }
}
