package be.kdg.groepa.service.impl;

import be.kdg.groepa.model.Place;
import be.kdg.groepa.model.PlaceTime;
import be.kdg.groepa.model.Route;
import be.kdg.groepa.model.WeekdayRoute;
import be.kdg.groepa.persistence.api.RouteDao;
import be.kdg.groepa.service.api.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Pieter-Jan on 18-2-14.
 */
@Service("RouteService")
public class RouteServiceImpl implements RouteService {

    @Autowired
    private RouteDao routeDao;

    @Override
    public void addRoute(Route r) {
        for(PlaceTime pt:r.getAllPlaceTimes()){
            pt.setRoute(r);
        }
        routeDao.addRoute(r);
    }

    @Override
    public void addPlace(Place p) {
        routeDao.addPlace(p);
    }

    @Override
    public void addPlaceTimeToPlace(PlaceTime pt, Place p) {
        routeDao.addPlaceTimeToPlace(pt, p);
    }

    @Override
    public void addWeekdayRoute(WeekdayRoute wr) {
        routeDao.addWeekdayRoute(wr);
    }
}


