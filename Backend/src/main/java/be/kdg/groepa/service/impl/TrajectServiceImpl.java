package be.kdg.groepa.service.impl;

import be.kdg.groepa.model.PlaceTime;
import be.kdg.groepa.model.Route;
import be.kdg.groepa.model.Traject;
import be.kdg.groepa.model.User;
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
        if (!previousPlaceTime.getRoute().getPlaceTimes().contains(newPlaceTime)) {
            int previousIndex = previousPlaceTime.getRoute().getPlaceTimes().indexOf(previousPlaceTime);
            previousPlaceTime.getRoute().getPlaceTimes().add(previousIndex + 1, newPlaceTime);
        trajectDao.saveRouteAndPoints(previousPlaceTime.getRoute(), previousPlaceTime, newPlaceTime);
        }
    }

    @Override
    public void addNewTrajectToRoute(PlaceTime previousPlaceTime1, PlaceTime newPlaceTime1, PlaceTime previousPlaceTime2, PlaceTime newPlaceTime2, User user) {
        Traject resultTraject = new Traject(newPlaceTime1, newPlaceTime2, previousPlaceTime1.getRoute(), user);
        newPlaceTime1.setTraject(resultTraject);
        resultTraject.setRoute(previousPlaceTime1.getRoute());
        newPlaceTime2.setTraject(resultTraject);
        trajectDao.addTraject(resultTraject);
        insertNewRoutePoint(previousPlaceTime1, newPlaceTime1);
        insertNewRoutePoint(previousPlaceTime2, newPlaceTime2);

    }
}
