package be.kdg.groepa.model;

import org.hibernate.annotations.Type;
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
    @Type(type="org.jadira.usertype.dateandtime.threetenbp.PersistentLocalTime")
    private LocalTime time;

    @ManyToOne
    @JoinColumn(name="placeId", nullable = true)
    private Place place;

    @ManyToOne
    @JoinColumn(name="weekdayRouteId")
    private WeekdayRoute weekdayRoute;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="trajectId", nullable = true)
    private Traject traject;

    @ManyToOne
    @JoinColumn(name="routeId", nullable = false)
    private Route route;

    /*
     * TODO WE REALLY NEED TO CLEAN UP THESE CONTROLLERS/USAGES...
     */

    public PlaceTime(LocalTime time, Place place, Route route) {
        this(time, place);
        this.route = route;
    }

    public PlaceTime(LocalTime time, Place place, WeekdayRoute weekdayRoute, Route route) {
        this(time, place, weekdayRoute);
        this.route = route;
    }

    public PlaceTime(LocalTime time, Place place) {
        this(time);
        this.place = place;
    }

    public PlaceTime(LocalTime time) {
        this.time = time;
    }

    public PlaceTime(LocalTime time, Place place, WeekdayRoute weekdayRoute) {
        this(time, place);
        this.weekdayRoute = weekdayRoute;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public void setWeekdayRoute(WeekdayRoute weekdayRoute) {
        this.weekdayRoute = weekdayRoute;
    }

    public void setTraject(Traject traject) {
        this.traject = traject;
    }

    public Place getPlace() {
        return place;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public WeekdayRoute getWeekdayRoute() {
        return weekdayRoute;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setPlacetimeId(int placetimeId) {
        this.placetimeId = placetimeId;
    }

    public int getPlacetimeId() {
        return placetimeId;
    }
}
