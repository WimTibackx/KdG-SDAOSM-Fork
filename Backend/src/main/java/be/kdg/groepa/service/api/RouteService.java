package be.kdg.groepa.service.api;

import be.kdg.groepa.dtos.AddRouteDTO;
import be.kdg.groepa.model.*;
import be.kdg.groepa.persistence.api.RouteDao;
import be.kdg.groepa.persistence.api.TrajectDao;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import java.util.List;

/**
 * Created by Pieter-Jan on 18-2-14.
 */
public interface RouteService {
    public void setRouteDao(RouteDao dao);
    public void setTrajectDao(TrajectDao dao);

    public void addRoute(Route r);
    public void addPlace(Place p);
    public void addWeekdayRoute(WeekdayRoute wr);
    public void addRide(Ride r);
    public void confirmRide(int routeId, LocalDateTime date);
    public PlaceTime getPlaceTimeById(int id);
    public Route getRouteById(int routeId);
    public List<WeekdayRoute> getWeekdayRoutesOfRoute(int routeId);
    public List<Route> getRoutes(User user);
    public void addRouteByAddRouteDTO(AddRouteDTO r, User user);
    public List<Route> findCarpoolers(double startLat, double startLon, double endLat, double endLon, User.Gender g, boolean smoker, double radius, LocalTime dep, int timeDiff);
}
