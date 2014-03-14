package be.kdg.groepa.persistence.api;

import be.kdg.groepa.model.*;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import java.util.List;

/**
 * Created by Pieter-Jan on 18-2-14.
 */
public interface RouteDao {

    public void addRepeatingRoute(Route r);
    public void addNonRepeatingRoute(Route r);
    public void addPlace(Place p);
    public void addWeekdayRoute(WeekdayRoute wr);
    public void addRide(Ride r);
    public void confirmRide(Route r, LocalDateTime date);
    public void editRoute(Route r, List<PlaceTime> placetimes);                        // Use case: edit Route. Use "editRoute" for non-repeating routes, "editWeekdayRoute" for repeating routes.
    public void editWeekdayRoute(WeekdayRoute wr, List<PlaceTime> placetimes);
    public PlaceTime getPlaceTimeById(int id);
    public Route getRouteById(int routeId);
    public List<WeekdayRoute> getWeekdayRoutesOfRoute(int routeId);
    public List<Route> getRoutes(User user);
    public void addPlaceTime(PlaceTime pt);
    public List<Route> findCarpoolers(double startLat, double startLon, double endLat, double endLon, User.Gender g, boolean smoker, double radius, LocalTime dep, int timeDiff);
    public void updateRoute(Route r);
}
