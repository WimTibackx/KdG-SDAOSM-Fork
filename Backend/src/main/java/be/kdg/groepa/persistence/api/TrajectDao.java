package be.kdg.groepa.persistence.api;

import be.kdg.groepa.model.PlaceTime;
import be.kdg.groepa.model.Route;
import be.kdg.groepa.model.Traject;
import be.kdg.groepa.model.User;

import java.util.List;

/**
 * Created by Tim on 19/02/14.
 */
public interface TrajectDao {
    void addTraject(Traject t);

    Traject getTrajectById(int trajectId);

    public List<Traject> getAcceptedTrajects(User user);
    public List<Traject> getRequestedTrajects(User user);
    public List<Traject> getRequestedOnMyRoutes(User user);

    public void updateTraject(Traject t);
    public void removeTraject(Traject t);

    /*
     * We could trust in lazy-loading the whole model, but I'd rather play it safe
     *  and write a query to get a property a couple of objects removed from traject.
     */
    public User getChauffeurByTraject(Traject t);
}
