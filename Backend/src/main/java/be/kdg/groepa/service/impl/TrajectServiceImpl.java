package be.kdg.groepa.service.impl;

import be.kdg.groepa.model.PlaceTime;
import be.kdg.groepa.model.Route;
import be.kdg.groepa.model.Traject;
import be.kdg.groepa.persistence.api.TrajectDao;
import be.kdg.groepa.service.api.TrajectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Tim on 19/02/14.
 */
@Service("TrajectService")
public class TrajectServiceImpl implements TrajectService {
    @Autowired
    private TrajectDao trajectDao;

    @Override
    public void addTraject(Traject traj) {
        traj.getRoute().addTraject(traj);
        traj.getPickup().setRoute(traj.getRoute());
        traj.getDropoff().setRoute(traj.getRoute());
        trajectDao.addTraject(traj);
    }

    @Override
    public void removeTrajectFromRoute(Route route, Traject traj) {
        route.removeTraject(traj);
        trajectDao.removeTrajectFromRoute(route, traj);
    }

    @Override
    public void insertNewRoutePoint(PlaceTime previousPlaceTime, PlaceTime newPlaceTime) {
        int previousIndex = previousPlaceTime.getRoute().getPlaceTimes().indexOf(previousPlaceTime);
        previousPlaceTime.getRoute().getPlaceTimes().add(previousIndex+1, newPlaceTime);
        trajectDao.saveRouteAndPoints(previousPlaceTime.getRoute(), previousPlaceTime, newPlaceTime);
    }


}
