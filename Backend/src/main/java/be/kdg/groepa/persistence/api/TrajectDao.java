package be.kdg.groepa.persistence.api;

import be.kdg.groepa.model.Route;
import be.kdg.groepa.model.Traject;

/**
 * Created by Tim on 19/02/14.
 */
public interface TrajectDao {
    public void addTrajectToRoute(Traject t, Route r);
}
