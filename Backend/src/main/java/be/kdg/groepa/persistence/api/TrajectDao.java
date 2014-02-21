package be.kdg.groepa.persistence.api;

import be.kdg.groepa.model.PlaceTime;
import be.kdg.groepa.model.Route;
import be.kdg.groepa.model.Traject;

/**
 * Created by Tim on 19/02/14.
 */
public interface TrajectDao {
    public void addTraject(Traject t);

    void removeTrajectFromRoute(Route route, Traject traj);

    void saveRoute(Route route);

    void saveRouteAndPoints(Route route, PlaceTime previousPlaceTime, PlaceTime newPlaceTime);
}
