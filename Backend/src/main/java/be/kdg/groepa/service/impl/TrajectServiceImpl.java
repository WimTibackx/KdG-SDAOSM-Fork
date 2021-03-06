package be.kdg.groepa.service.impl;

import be.kdg.groepa.dtos.UpcomingTrajectDTO;
import be.kdg.groepa.exceptions.*;
import be.kdg.groepa.model.*;
import be.kdg.groepa.persistence.api.TrajectDao;
import be.kdg.groepa.service.api.RouteService;
import be.kdg.groepa.service.api.TextMessageService;
import be.kdg.groepa.service.api.TrajectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.temporal.TemporalAdjusters;

import java.util.*;

/**
 * Created by Tim on 19/02/14.
 */
@Service("TrajectService")
public class TrajectServiceImpl implements TrajectService {
    private TrajectDao trajectDao;
    @Autowired
    private RouteService routeService;
    @Autowired
    private TextMessageService textMessageService;

    @Autowired
    @Override
    public void setTrajectDao(TrajectDao dao) {
        this.trajectDao = dao;
    }

    @Override
    public Traject getTrajectById(int trajectId) {
        return trajectDao.getTrajectById(trajectId);
    }

    @Override
    public LocalDate getNextDayOfTraject(Traject traject) {
        if (traject.getRoute().getEndDate().isBefore(LocalDate.now())) return null;
        DayOfWeek dow = DayOfWeek.of(traject.getPickup().getWeekdayRoute().getDay()+1);
        LocalDate nextDay = LocalDate.now().with(TemporalAdjusters.nextOrSame(dow));
        if (nextDay.isAfter(traject.getRoute().getEndDate())) return null;
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

    @Override
    public List<Traject> getAcceptedTrajects(User user) {
        return trajectDao.getAcceptedTrajects(user);
    }

    @Override
    public List<Traject> getRequestedTrajects(User user) {
        return trajectDao.getRequestedTrajects(user);
    }

    @Override
    public List<Traject> getRequestedOnMyRoutes(User user) {
        return trajectDao.getRequestedOnMyRoutes(user);
    }

    @Override
    public boolean requestTraject(User user, Route route, int idPickup, int idDropoff) throws PlaceTimesOfDifferentRoutesException, PlaceTimesOfDifferentWeekdayRoutesException, PlaceTimesInWrongSequenceException, TrajectNotEnoughCapacityException {
        final String msgBody="%s heeft een traject aangevraagd van %s tot %s. U kan dit via uw profiel accepteren of weigeren.";
        PlaceTime ptPickup = routeService.getPlaceTimeById(idPickup);
        PlaceTime ptDropoff = routeService.getPlaceTimeById(idDropoff);
        if (ptPickup.getRoute().getId() != ptDropoff.getRoute().getId() || ptPickup.getRoute().getId() != route.getId()) {
            throw new PlaceTimesOfDifferentRoutesException();
        }
        if (route.isRepeating() && ptPickup.getWeekdayRoute().getWeekdayrouteId() != ptDropoff.getWeekdayRoute().getWeekdayrouteId()) {
            throw new PlaceTimesOfDifferentWeekdayRoutesException();
        }
        if (ptPickup.getTime().compareTo(ptDropoff.getTime()) >= 0) {
            throw new PlaceTimesInWrongSequenceException();
        }
        if (!isCapacityLeft(route, ptPickup, ptDropoff)) {
            throw new TrajectNotEnoughCapacityException();
        }

        Traject t = new Traject(ptPickup, ptDropoff, route, user);
        trajectDao.addTraject(t);

        TextMessage msg = new TextMessage(route.getChauffeur(),user,"[Automatisch bericht] Traject aangevraagd.",String.format(msgBody, user.getName(), ptPickup.getPlace().getName(), ptDropoff.getPlace().getName()));
        textMessageService.addNewMessage(msg);
        return true;
    }

    @Override
    public void acceptTraject(int trajectId, User currentUser) throws UnauthorizedException {
        final String msgBody="U vroeg een traject aan van %s tot %s. %s accepteerde uw aanvraag.";
        Traject t = this.getTrajectById(trajectId);
        User chauffeur = trajectDao.getChauffeurByTraject(t);
        if (!chauffeur.getId().equals(currentUser.getId())) {
            throw new UnauthorizedException();
        }

        t.setAccepted(true);
        trajectDao.updateTraject(t);
        TextMessage msg = new TextMessage(chauffeur,t.getUser(),"[Automatisch bericht] Trajectaanvraag geaccepteerd.",String.format(msgBody, t.getPickup().getPlace().getName(), t.getDropoff().getPlace().getName(), chauffeur.getName()));
        textMessageService.addNewMessage(msg);
    }

    @Override
    public void rejectTraject(int trajectId, User currentUser) throws UnauthorizedException {
        final String msgBody="U vroeg een traject aan van %s tot %s. Helaas werd uw aanvraag geweigerd.";
        Traject t = this.getTrajectById(trajectId);
        User chauffeur = trajectDao.getChauffeurByTraject(t);
        if (!chauffeur.getId().equals(currentUser.getId())) {
            throw new UnauthorizedException();
        }

        trajectDao.removeTraject(t);
        TextMessage msg = new TextMessage(chauffeur,t.getUser(),"[Automatisch bericht] Trajectaanvraag geweigerd.",String.format(msgBody, t.getPickup().getPlace().getName(), t.getDropoff().getPlace().getName()));
        textMessageService.addNewMessage(msg);
    }

    /**
     * Determines whether there is capacity left between the pickup-point and
     * the dropoff-point.
     * It will calculate the capacity that is left between every two
     * sequential placetimes on the route or weekdayroute by loading all the
     * trajects for the route or weekdayroute and determining which placetimes
     * they pass.
     * <p>
     * Afterwards, we'll check where the placetimes in the parameters pass and
     * whether there's enough capacity left for them to pass there.
     *
     * @param route     The route on which the traject is added
     * @param ptPickup  The pickup point for the traject. Must have weekdayroute
     *                  association loaded.
     * @param ptDropoff The dropoff point for the traject. Must have weekdayroute
     *                  association loaded.
     * @return          <code>true</code> if there is capacity left for the traject;
     *                  <code>false</code> otherwise.
     */
    private boolean isCapacityLeft(Route route, PlaceTime ptPickup, PlaceTime ptDropoff) {
        List<PlaceTime> placeTimes;
        if (route.isRepeating()) {
            placeTimes = ptPickup.getWeekdayRoute().getPlaceTimes();
        } else {
            placeTimes = route.getPlaceTimes();
        }

        Map<Integer, Integer> ptSeqMap=new HashMap<>();
        Map<Integer, Integer> capMap=new HashMap<>();
        int routeCap = route.getCapacity();
        for (int i=0; i<placeTimes.size(); i++) {
            ptSeqMap.put(placeTimes.get(i).getPlacetimeId(), i);
            capMap.put(i, routeCap);
        }

        List<Traject> trajects;
        if (route.isRepeating()) {
            trajects = new ArrayList<>(ptPickup.getWeekdayRoute().getTrajects());
        } else {
            trajects = route.getTrajects();
        }

        //Just a list of keyvaluepairs... We can't have one of them being keys in a map
        List<AbstractMap.SimpleEntry<Integer, Integer>> pdSeqNrs=new ArrayList<>();
        for (Traject t : trajects) {
            pdSeqNrs.add(new AbstractMap.SimpleEntry<>(ptSeqMap.get(t.getPickup().getPlacetimeId()), ptSeqMap.get(t.getDropoff().getPlacetimeId())));
        }

        for (AbstractMap.SimpleEntry<Integer, Integer> e : pdSeqNrs) {
            int ptSeqNr = e.getKey();
            while (ptSeqNr < e.getValue()) {
                capMap.put(ptSeqNr,capMap.get(ptSeqNr)-1);
                ptSeqNr++;
            }
        }

        boolean enoughCapacityLeft = true;
        int pickupSeqNr = ptSeqMap.get(ptPickup.getPlacetimeId());
        int dropoffSeqNr = ptSeqMap.get(ptDropoff.getPlacetimeId());
        for (int i=pickupSeqNr; i<dropoffSeqNr; i++) {
            if (capMap.get(i)<1) {
                enoughCapacityLeft = false;
            }
        }
        return enoughCapacityLeft;
    }
}
