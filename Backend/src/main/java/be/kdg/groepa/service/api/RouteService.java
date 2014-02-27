package be.kdg.groepa.service.api;

import be.kdg.groepa.model.*;

import java.util.List;

/**
 * Created by Pieter-Jan on 18-2-14.
 */
public interface RouteService {

    public void addRoute(Route r);
    public void addPlace(Place p);
    public void addPlaceTimeToPlace(PlaceTime pt, Place p);
    public void addPlaceTimeToRoute(Route r, PlaceTime pt);
    public void addWeekdayRoute(WeekdayRoute wr);
    public void addRide(Ride r);
    public void confirmRide(List<Traject> trajecten);

    public PlaceTime getPlaceTimeById(int id);
    public Route getRouteById(int routeId);
}
