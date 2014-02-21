package be.kdg.groepa.service.api;

import be.kdg.groepa.model.PlaceTime;
import be.kdg.groepa.model.Route;
import be.kdg.groepa.model.Traject;

/**
 * Created by Tim on 19/02/14.
 */
public interface TrajectService {
    public void addTraject(Traject t);

    void removeTrajectFromRoute(Route route, Traject traj);

    void insertNewRoutePoint(PlaceTime previousPlaceTime, PlaceTime newPlaceTime);
}
