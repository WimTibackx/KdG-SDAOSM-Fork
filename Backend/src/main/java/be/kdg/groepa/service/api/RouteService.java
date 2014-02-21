package be.kdg.groepa.service.api;

import be.kdg.groepa.model.Place;
import be.kdg.groepa.model.PlaceTime;
import be.kdg.groepa.model.Route;
import be.kdg.groepa.model.WeekdayRoute;

/**
 * Created by Pieter-Jan on 18-2-14.
 */
public interface RouteService {

    public void addRoute(Route r);
    public void addPlace(Place p);
    public void addPlaceTimeToPlace(PlaceTime pt, Place p);
    public void addPlaceTimeToRoute(Route r, PlaceTime pt);
    public void addWeekdayRoute(WeekdayRoute wr);
}
