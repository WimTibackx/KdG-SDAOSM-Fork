package be.kdg.groepa.persistence.impl;

import be.kdg.groepa.model.*;
import be.kdg.groepa.persistence.api.RouteDao;
import be.kdg.groepa.persistence.util.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Pieter-Jan on 18-2-14.
 */

@SuppressWarnings("JpaQlInspection")
@Repository("routeDao")
public class RouteDaoImpl implements RouteDao {

    @Transactional
    @Override
    public void addRoute(Route r) {
        Session ses = HibernateUtil.openSession();
        for(PlaceTime pt:r.getAllPlaceTimes()){
            ses.saveOrUpdate(pt.getPlace());
            ses.saveOrUpdate(pt);
        }
        ses.saveOrUpdate(r);
        HibernateUtil.closeSession(ses);
        // HibernateUtil.addObject(r); ???
    }

    @Override
    public void addPlace(Place p) {
        Session ses = HibernateUtil.openSession();
        ses.saveOrUpdate(p);
        HibernateUtil.closeSession(ses);
    }

    @Override
    public void addPlaceTimeToPlace(PlaceTime pt, Place p) {
        Session ses = HibernateUtil.openSession();
        pt.setPlace(p);
        ses.saveOrUpdate(pt);
        HibernateUtil.closeSession(ses);
    }
    /*
    @Override
    public void addWeekdayRouteWithPlaceTimes(WeekdayRoute wr, List<PlaceTime> ps)
    {
        Session ses = HibernateUtil.openSession();
        for (PlaceTime pt : ps)
        {
            wr.addPlaceTime(ps);
        }
    }  */

    @Transactional
    @Override
    public void addPlaceTimeToRoute(Route r, PlaceTime pt)
    {
        Session ses = HibernateUtil.openSession();
        pt.setRoute(r);
        r.addPlaceTime(pt);
        ses.saveOrUpdate(pt);
        ses.saveOrUpdate(r);
        HibernateUtil.closeSession(ses);
    }

    @Override
    public void addWeekdayRoute(WeekdayRoute wr) {
        Session ses = HibernateUtil.openSession();
        ses.saveOrUpdate(wr);
        HibernateUtil.closeSession(ses);
    }

    @Override
    public void addRide(Ride r) {
        Session ses = HibernateUtil.openSession();
        ses.saveOrUpdate(r);
        HibernateUtil.closeSession(ses);
    }

    @Override
    public void editRoute(Route r, List<PlaceTime> placetimes) {
        Session ses = HibernateUtil.openSession();
        for (PlaceTime t : placetimes)
        {
            t.setRoute(r);
            ses.saveOrUpdate(t);
        }
        r.setPlaceTimes(placetimes);
        ses.saveOrUpdate(r);
        HibernateUtil.closeSession(ses);
    }

    @Override
    public void editWeekdayRoute(WeekdayRoute wr, List<PlaceTime> placetimes) {

    }

    @Override
    public void confirmRide(Route r) {
        Ride ride = new Ride(r);
        for (Traject t : r.getTrajects())
        {
            ride.addTraject(t);
            t.setRide(ride);
        }
        Session ses = HibernateUtil.openSession();
        ses.saveOrUpdate(ride);
        HibernateUtil.closeSession(ses);
    }

    public PlaceTime getPlaceTimeById(int id) {
        Session ses = HibernateUtil.openSession();
        Query query = ses.createQuery("from PlaceTime pt where pt.placetimeId = :id");
        query.setInteger("id", id);
        PlaceTime pt = (PlaceTime)query.uniqueResult();
        HibernateUtil.closeSession(ses);
        return pt;
    }

    @Override
    public Route getRouteById(int routeId) {
        Session ses = HibernateUtil.openSession();
        Query query = ses.createQuery("from Route ro where ro.id = :id");
        query.setInteger("id", routeId);
        Route route = (Route)query.uniqueResult();
        HibernateUtil.closeSession(ses);
        return route;
    }
}
