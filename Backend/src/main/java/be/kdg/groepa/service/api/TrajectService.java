package be.kdg.groepa.service.api;

import be.kdg.groepa.dtos.UpcomingTrajectDTO;
import be.kdg.groepa.model.PlaceTime;
import be.kdg.groepa.model.Route;
import be.kdg.groepa.model.Traject;
import be.kdg.groepa.model.User;
import org.threeten.bp.LocalDate;

import java.util.List;

/**
 * Created by Tim on 19/02/14.
 */
public interface TrajectService {
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
}
