package be.kdg.groepa.model;

import javax.persistence.*;
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
    @OneToMany
    @JoinColumn(name="placetimeId")
    private List<PlaceTime> placeTimes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="routeId", nullable=false)
    private Route route;

    public WeekdayRoute() {}

    public WeekdayRoute(Route route, int day) {
        this.route = route;
        this.day = day;
    }

    public void addPlaceTime(PlaceTime pt)
    {
        this.placeTimes.add(pt);
    }
}
