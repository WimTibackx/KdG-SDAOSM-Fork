package be.kdg.groepa.service.impl;

import be.kdg.groepa.dtos.AddRouteDTO;
import be.kdg.groepa.dtos.PlaceDTO;
import be.kdg.groepa.exceptions.CarNotFoundException;
import be.kdg.groepa.helpers.CostManager;
import be.kdg.groepa.model.*;
import be.kdg.groepa.persistence.api.RouteDao;
import be.kdg.groepa.persistence.api.TrajectDao;
import be.kdg.groepa.persistence.api.UserDao;
import be.kdg.groepa.service.api.CarService;
import be.kdg.groepa.service.api.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Pieter-Jan on 18-2-14.
 */
@Service("RouteService")
public class RouteServiceImpl implements RouteService {
    private RouteDao routeDao;
    private TrajectDao trajectDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    @Override
    public void setRouteDao(RouteDao dao) {
        this.routeDao = dao;
    }

    @Autowired
    @Override
    public void setTrajectDao(TrajectDao dao) {
        this.trajectDao = dao;
    }

    @Autowired
    private CarService carService;

    @Override
    public void addRoute(Route r) {
        if (r.isRepeating()) routeDao.addRepeatingRoute(r);
        else routeDao.addNonRepeatingRoute(r);

        Traject t = new Traject(r.getAllPlaceTimes().get(0), r.getAllPlaceTimes().get(r.getAllPlaceTimes().size() - 1), r, r.getChauffeur());
        trajectDao.addTraject(t);



        r.getChauffeur().addRoute(r);
        userDao.updateUser(r.getChauffeur());
    }

    @Override
    public void addPlace(Place p) {
        routeDao.addPlace(p);
    }

    @Override
    public void addWeekdayRoute(WeekdayRoute wr) {
        routeDao.addWeekdayRoute(wr);
    }

    @Override
    public void addRide(Ride r) {
        routeDao.addRide(r);
    }

    @Override
    public void confirmRide(Route r) {
        //Ride ride = new Ride(CostManager.calculateCost(r), CostManager.getTotalDistance(r));
        routeDao.confirmRide(r);
    }
    @Override
    public PlaceTime getPlaceTimeById(int id){
        return routeDao.getPlaceTimeById(id);
    }

    @Override
    public Route getRouteById(int routeId) {
        return routeDao.getRouteById(routeId);
    }

    // Move along, nothing to see here...
    @Override
    public List<WeekdayRoute> getWeekdayRoutesOfRoute(int routeId) {
        return routeDao.getWeekdayRoutesOfRoute(routeId);
    }

    @Override
    public List<Route> getRoutes(User user) {
        return routeDao.getRoutes(user);
    }

    @Override
    public void addRouteByAddRouteDTO(AddRouteDTO dto, User user) {
        Car car=null;
        try {
            car = carService.get(dto.getCarId());
        } catch (CarNotFoundException e) {
            e.printStackTrace();
        }
        Route r = new Route(dto.isRepeating(), dto.getFreeSpots(), dto.getStartDate(), dto.getEndDate(), user, car);
        List<Place> places = new ArrayList<>();
        for (PlaceDTO placeDto : dto.getPlaces()) {
            Place place = new Place(placeDto.getName(), placeDto.getLat(), placeDto.getLon());
            places.add(place);
        }

        List<PlaceTime> placeTimes = new ArrayList<>();
        List<WeekdayRoute> weekdayRoutes = new ArrayList<>();
        for (Map.Entry<DayOfWeek, List<LocalTime>> e : dto.getTimes().entrySet()) {
            WeekdayRoute wdr = null;
            if (dto.isRepeating()) {
                wdr = new WeekdayRoute(r, e.getKey().getValue()-1);
                weekdayRoutes.add(wdr);
            }
            for (int i=0; i<places.size(); i++) {
                PlaceTime pt = new PlaceTime(e.getValue().get(i), places.get(i));
                pt.setRoute(r);

                if (dto.isRepeating()) {
                    pt.setWeekdayRoute(wdr);
                }
                placeTimes.add(pt);
            }
        }



        if (r.isRepeating()) {
            routeDao.addRepeatingRoute(r);
        } else {
            routeDao.addNonRepeatingRoute(r);
        }

        Traject t = new Traject(placeTimes.get(0), placeTimes.get(placeTimes.size()-1),r,user);
        r.addTraject(t);
    }
}


