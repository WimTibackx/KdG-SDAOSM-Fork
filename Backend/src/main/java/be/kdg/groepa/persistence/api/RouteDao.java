package be.kdg.groepa.persistence.api;

import be.kdg.groepa.model.Place;
import be.kdg.groepa.model.PlaceTime;
import be.kdg.groepa.model.Route;
import be.kdg.groepa.model.WeekdayRoute;

/**
 * Created by Pieter-Jan on 18-2-14.
 */
public interface RouteDao {

    public void addRoute(Route r);
    public void addPlace(Place p);
    public void addPlaceTime(PlaceTime pt);
    public void addWeekdayRoute(WeekdayRoute wr);
}
