package be.kdg.groepa.service.api;

import be.kdg.groepa.model.PlaceTime;
import be.kdg.groepa.model.Route;
import be.kdg.groepa.model.Traject;
import be.kdg.groepa.model.User;

/**
 * Created by Tim on 19/02/14.
 */
public interface TrajectService {
    public void addTraject(Traject t);

    void removeTrajectFromRoute(Route route, Traject traj);

    void insertNewRoutePoint(PlaceTime previousPlaceTime, PlaceTime newPlaceTime);

    void addNewTrajectToRoute(PlaceTime placeTime, PlaceTime newPlaceTime, PlaceTime placeTime1, PlaceTime newPlaceTime2, User user);
}
