package be.kdg.groepa.model;

import org.threeten.bp.LocalTime;

import javax.persistence.*;

/**
 * Created by Pieter-Jan on 14-2-14.
 */

@Entity
@Table(name="t_placetime")
public class PlaceTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int placetimeId;

    @Column(name="time")
    private LocalTime time;

    @ManyToOne
    @JoinColumn(name="placeId", nullable = true)
    private Place place;

    @ManyToOne
    @JoinColumn(name="weekdayRouteId")
    private WeekdayRoute weekdayRoute;

    @ManyToOne
    @JoinColumn(name="routeId")
    private Route route;

    public PlaceTime() {}

    public PlaceTime(LocalTime time, Place place) {  // Private because when adding a new PlaceTime, the Place object might already exist. Use addPlaceTime instead of the constructor.
        this.time = time;
        this.place = place;
    }

    public PlaceTime(LocalTime time)
    {
        this.time = time;
        this.place = null;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public void setWeekdayRoute(WeekdayRoute weekdayRoute) {
        this.weekdayRoute = weekdayRoute;
    }
}
