package be.kdg.groepa.service.impl;

import be.kdg.groepa.dtos.AddRouteDTO;
import be.kdg.groepa.dtos.ChangeRouteDTO;
import be.kdg.groepa.dtos.PlaceDTO;
import be.kdg.groepa.exceptions.CarNotFoundException;
import be.kdg.groepa.exceptions.UnauthorizedException;
import be.kdg.groepa.helpers.CostManager;
import be.kdg.groepa.model.*;
import be.kdg.groepa.persistence.api.RouteDao;
import be.kdg.groepa.persistence.api.TrajectDao;
import be.kdg.groepa.persistence.api.UserDao;
import be.kdg.groepa.service.api.CarService;
import be.kdg.groepa.service.api.RouteService;
import be.kdg.groepa.service.api.TextMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalTime;
import java.util.ArrayList;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import java.util.List;
import java.util.Map;


import java.util.*;

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

    @Autowired
    private TextMessageService msgService;

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

    private List<User> getRoutePassengers(Route r)
    {
        List<User> rtval = new ArrayList<>();
        for (Traject t : r.getTrajects())
        {
            rtval.add(t.getUser());
        }
        return rtval;
    }

    @Override
    public void confirmRide(int routeId, LocalDateTime date) {
        // TODO: Costmanagers afmaken!
        TextMessage tm;
        Route r = this.getRouteById(routeId);
        List<User> passengers = getRoutePassengers(r);
        for (User p : passengers)
        {
            String temp = String.format("%s has confirmed riding the route on %s.\nPlease contribute to the fuel costs: €%.2f", r.getChauffeur().getName(), date.toString(), 12.2156);
            tm = new TextMessage(r.getChauffeur(), p, "Ride confirmed - " + date.toString(), temp);
            msgService.addNewMessage(tm);
        }
        routeDao.confirmRide(r, date);
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

    @Override
    public List<Route> findCarpoolers(double startLat, double startLon, double endLat, double endLon, User.Gender g, boolean smoker, double radius, LocalTime dep, int timeDiff) {
        // If needed, time difference or radius will have to be reformed here.
        return routeDao.findCarpoolers(startLat, startLon, endLat, endLon, g, smoker, radius, dep, timeDiff);
    }

    @Override
    public void editRoute(ChangeRouteDTO dto, User user) throws UnauthorizedException {
        Route originalRoute = this.getRouteById(dto.getRouteId());
        if (!originalRoute.getChauffeur().equals(user)) throw new UnauthorizedException();

        // First, copy everything onto the new route
        //  Route
        //  Weekdayroutes
        //  Placetimes
        //  Not: Traject, we'll send a message to everyone to check the updated route TODO
        //  Not: places, these should already exist
        Map<Integer, WeekdayRoute> weekdayRouteMap = new HashMap<>();
        Map<Integer, PlaceTime> placeTimeMap = new HashMap<>();
        Route newRoute = new Route(originalRoute.isRepeating(), originalRoute.getCapacity(), dto.getStartDate(), originalRoute.getEndDate(), originalRoute.getChauffeur(), originalRoute.getCar());
        if (originalRoute.isRepeating()) {
            for (WeekdayRoute wdr : this.getWeekdayRoutesOfRoute(dto.getRouteId())) {
                WeekdayRoute newWdr = new WeekdayRoute(newRoute, wdr.getDay());
                weekdayRouteMap.put(wdr.getWeekdayrouteId(), newWdr);
                for (PlaceTime pt : wdr.getPlaceTimes()) {
                    PlaceTime ptLoaded = this.getPlaceTimeById(pt.getPlacetimeId());
                    PlaceTime newPt = new PlaceTime(ptLoaded.getTime(), ptLoaded.getPlace(), newWdr, newRoute);
                    placeTimeMap.put(pt.getPlacetimeId(), newPt);
                }
                newRoute.addWeekdayRoute(newWdr);
            }
        } else {
            for (PlaceTime pt : newRoute.getPlaceTimes()) {
                PlaceTime ptLoaded = this.getPlaceTimeById(pt.getPlacetimeId());
                PlaceTime newPt = new PlaceTime(ptLoaded.getTime(), ptLoaded.getPlace(), newRoute);
                placeTimeMap.put(pt.getPlacetimeId(), newPt);
            }
        }

        // Loop over the changes, and make them
        for (ChangeRouteDTO.Change change : dto.getChanges()) {
            if (change instanceof ChangeRouteDTO.DeletePlaceTime) {
                ChangeRouteDTO.DeletePlaceTime changeT = (ChangeRouteDTO.DeletePlaceTime) change;
                PlaceTime newPt = placeTimeMap.get(changeT.getPlaceTimeId());
                newRoute.getPlaceTimes().remove(newPt);
                if (newRoute.isRepeating()) {
                    newPt.getWeekdayRoute().getPlaceTimes().remove(newPt);
                }

                List<PlaceTime> newPts = null;
                if (newRoute.isRepeating()) {
                    newPts = weekdayRouteMap.get(changeT.getWeekdayRouteId()).getPlaceTimes();
                } else {
                    newPts = newRoute.getPlaceTimes();
                }

                for (ChangeRouteDTO.PlaceTimeSpecifier pts : changeT.getTimes()) {
                    for (PlaceTime remainingNewPt : newPts) {
                        if (Double.compare(remainingNewPt.getPlace().getLat(),pts.getLat()) == 0 && Double.compare(remainingNewPt.getPlace().getLon(), pts.getLng()) == 0) {
                            remainingNewPt.setTime(pts.getTime());
                        }
                    }
                }
            } else if (change instanceof ChangeRouteDTO.ChangeTime) {
                ChangeRouteDTO.ChangeTime changeT = (ChangeRouteDTO.ChangeTime) change;

                List<PlaceTime> newPts = null;
                if (newRoute.isRepeating()) {
                    newPts = weekdayRouteMap.get(changeT.getWeekdayRouteId()).getPlaceTimes();
                } else {
                    newPts = newRoute.getPlaceTimes();
                }

                for (ChangeRouteDTO.PlaceTimeSpecifier pts : changeT.getTimes()) {
                    for (PlaceTime newPt : newPts) {
                        if (Double.compare(newPt.getPlace().getLat(),pts.getLat()) == 0 && Double.compare(newPt.getPlace().getLon(), pts.getLng()) == 0) {
                            newPt.setTime(pts.getTime());
                        }
                    }
                }
            } else if (change instanceof ChangeRouteDTO.AddWeekdayRoute) {
                ChangeRouteDTO.AddWeekdayRoute changeT = (ChangeRouteDTO.AddWeekdayRoute) change;
                WeekdayRoute newWdr = new WeekdayRoute(newRoute, changeT.getDay());
                newRoute.addWeekdayRoute(newWdr);
                for (ChangeRouteDTO.PlaceTimeSpecifier pts : changeT.getTimes()) {
                    new PlaceTime(pts.getTime(), new Place(pts.getAddress(), pts.getLat(), pts.getLng()), newWdr, newRoute);
                }
            } else if (change instanceof ChangeRouteDTO.DeleteWeekdayRoute) {
                ChangeRouteDTO.DeleteWeekdayRoute changeT = (ChangeRouteDTO.DeleteWeekdayRoute) change;
                newRoute.getWeekdayRoutes().remove(weekdayRouteMap.get(changeT.getWeekdayRouteId()));
                Iterator<PlaceTime> ptsInRoute = newRoute.getPlaceTimes().iterator();
                while (ptsInRoute.hasNext()) {
                    PlaceTime pt = ptsInRoute.next();
                    if (pt.getWeekdayRoute().getDay() == weekdayRouteMap.get(changeT.getWeekdayRouteId()).getDay()) {
                        ptsInRoute.remove();
                    }
                }
            } else if (change instanceof ChangeRouteDTO.AddPlaceTime) {
                ChangeRouteDTO.AddPlaceTime changeT = (ChangeRouteDTO.AddPlaceTime) change;
                List<PlaceTime> newPts = null;
                if (newRoute.isRepeating()) {
                    newPts = weekdayRouteMap.get(changeT.getWeekdayRouteId()).getPlaceTimes();
                } else {
                    newPts = newRoute.getPlaceTimes();
                }
                for (ChangeRouteDTO.PlaceTimeSpecifier pts : changeT.getPlaceTimeSpecifiers()) {
                    if (Double.compare(pts.getLat(), changeT.getLat()) == 0 && Double.compare(pts.getLng(), changeT.getLng()) == 0) {
                        new PlaceTime(pts.getTime(), new Place(changeT.getAddress(), changeT.getLat(), changeT.getLng()), weekdayRouteMap.get(changeT.getWeekdayRouteId()), newRoute);
                    } else {
                        for (PlaceTime newPt : newPts) {
                            if (Double.compare(newPt.getPlace().getLat(),pts.getLat()) == 0 && Double.compare(newPt.getPlace().getLon(), pts.getLng()) == 0) {
                                newPt.setTime(pts.getTime());
                            }
                        }
                    }
                }
            } else if (change instanceof ChangeRouteDTO.ChangeCar) {
                ChangeRouteDTO.ChangeCar changeT = (ChangeRouteDTO.ChangeCar) change;
                Car newCar = null;
                try {
                    newCar = carService.get(changeT.getCarId());
                } catch (CarNotFoundException e) {
                    e.printStackTrace();
                    //TODO
                }
                newRoute.setCar(newCar);
                newRoute.setCapacity(changeT.getCapacity());
            }
        }

        // Add the route
        if (newRoute.isRepeating()) {
            routeDao.addRepeatingRoute(newRoute);
        } else {
            routeDao.addNonRepeatingRoute(newRoute);
        }

        // Change the original route (date)
        originalRoute.setEndDate(dto.getStartDate().minusDays(1));
        routeDao.updateRoute(originalRoute);
    }
}


