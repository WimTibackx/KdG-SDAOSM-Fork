package be.kdg.groepa.service.api;

import be.kdg.groepa.dtos.UpcomingTrajectDTO;
import be.kdg.groepa.exceptions.*;
import be.kdg.groepa.model.PlaceTime;
import be.kdg.groepa.model.Route;
import be.kdg.groepa.model.Traject;
import be.kdg.groepa.model.User;
import be.kdg.groepa.persistence.api.TrajectDao;
import org.threeten.bp.LocalDate;

import java.util.List;

/**
 * Created by Tim on 19/02/14.
 */
public interface TrajectService {
    public void setTrajectDao(TrajectDao dao);

    void addTraject(Traject t);

    void removeTrajectFromRoute(Route route, Traject traj);

    void insertNewRoutePoint(PlaceTime previousPlaceTime, PlaceTime newPlaceTime);

    void addNewTrajectToRoute(PlaceTime placeTime, PlaceTime newPlaceTime, PlaceTime placeTime1, PlaceTime newPlaceTime2, User user);

    Traject getTrajectById(int trajectId);

    /*
     * This method calculates the next LocalDate on which the traject will be driven (or null if it is in the past)
     * It needs a preloaded placetime (pickup and dropoff), weekdayroute, and route
     */
    public LocalDate getNextDayOfTraject(Traject traject);
    public List<UpcomingTrajectDTO> getUpcomingTrajects(User user);
    public List<Traject> getAcceptedTrajects(User user);
    public List<Traject> getRequestedTrajects(User user);
    public List<Traject> getRequestedOnMyRoutes(User user);

    public boolean requestTraject(User user, Route route, int idPickup, int idDropoff) throws PlaceTimesOfDifferentRoutesException, PlaceTimesOfDifferentWeekdayRoutesException, PlaceTimesInWrongSequenceException, TrajectNotEnoughCapacityException;
    public void acceptTraject(int trajectId, User currentUser) throws UnauthorizedException;
    public void rejectTraject(int trajectId, User currentUser) throws UnauthorizedException;
}
