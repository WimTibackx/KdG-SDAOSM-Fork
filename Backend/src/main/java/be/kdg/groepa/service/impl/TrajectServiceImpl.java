package be.kdg.groepa.service.impl;

import be.kdg.groepa.dtos.UpcomingTrajectDTO;
import be.kdg.groepa.model.PlaceTime;
import be.kdg.groepa.model.Route;
import be.kdg.groepa.model.Traject;
import be.kdg.groepa.model.User;
import be.kdg.groepa.persistence.api.TrajectDao;
import be.kdg.groepa.service.api.TrajectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.temporal.TemporalAdjusters;

import javax.transaction.Transactional;
import java.util.*;

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

    @Transactional
    @Override
    public void removeTrajectFromRoute(Route route, Traject traj) {
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

    @Transactional
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

    @Override
    public Traject getTrajectById(int trajectId) {
        return trajectDao.getTrajectById(trajectId);
    }

    @Override
    public LocalDate getNextDayOfTraject(Traject traject) {
        if (traject.getRoute().getEndDate().isBefore(LocalDate.now().atStartOfDay())) return null;
        DayOfWeek dow = DayOfWeek.of(traject.getPickup().getWeekdayRoute().getDay()+1);
        LocalDate nextDay = LocalDate.now().with(TemporalAdjusters.nextOrSame(dow));
        if (nextDay.isAfter(traject.getRoute().getEndDate().toLocalDate())) return null;
        return nextDay;
    }

    @Override
    public List<UpcomingTrajectDTO> getUpcomingTrajects(User user) {
        List<Traject> trajects = trajectDao.getAcceptedTrajects(user);
        Iterator<Traject> it = trajects.iterator();
        Map<Traject, LocalDate> nextTrajectOccurrence = new HashMap<>();
        List<UpcomingTrajectDTO> dtos = new ArrayList<>();
        while (it.hasNext()) {
            Traject t = it.next();
            LocalDate next = this.getNextDayOfTraject(t);
            if (next == null) continue;
            nextTrajectOccurrence.put(t, next);
        }
        for (Map.Entry<Traject, LocalDate> e : nextTrajectOccurrence.entrySet()) {
            dtos.add(new UpcomingTrajectDTO(e.getKey(), e.getValue()));
        }
        return dtos;
    }
}
