package be.kdg.groepa.persistence.api;

import be.kdg.groepa.model.*;

import java.util.List;

/**
 * Created by Pieter-Jan on 18-2-14.
 */
public interface RouteDao {

    public void addRepeatingRoute(Route r);
    public void addNonRepeatingRoute(Route r);
    public void addPlace(Place p);
    public void addPlaceTimeToPlace(PlaceTime pt, Place p);
    public void addPlaceTimeToRoute(Route r, PlaceTime pt);
    public void addWeekdayRoute(WeekdayRoute wr);
    public void addRide(Ride r);
    public void confirmRide(Route r);
    public void editRoute(Route r, List<PlaceTime> placetimes);                        // Use case: edit Route. Use "editRoute" for non-repeating routes, "editWeekdayRoute" for repeating routes.
    public void editWeekdayRoute(WeekdayRoute wr, List<PlaceTime> placetimes);
    public PlaceTime getPlaceTimeById(int id);
    public Route getRouteById(int routeId);
    public List<Route> findCarpoolers(PlaceTime pt1, PlaceTime pt2, User.Gender g, boolean smoker, double radius);
}
