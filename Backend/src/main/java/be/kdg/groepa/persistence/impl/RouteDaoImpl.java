package be.kdg.groepa.persistence.impl;

import be.kdg.groepa.model.*;
import be.kdg.groepa.persistence.api.RouteDao;
import be.kdg.groepa.persistence.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pieter-Jan on 18-2-14.
 */

@SuppressWarnings("JpaQlInspection")
@Repository("routeDao")
public class RouteDaoImpl implements RouteDao {

    @Transactional
    @Override
    public void addRepeatingRoute(Route r) {
        Session ses = HibernateUtil.openSession();
        List<WeekdayRoute> wrs = r.getWeekdayRoutes();
        for (WeekdayRoute wr : wrs)
        {
            List<PlaceTime> placetimes = wr.getPlaceTimes();
            for (PlaceTime pt : placetimes)
            {
                ses.saveOrUpdate(pt.getPlace());
                ses.saveOrUpdate(pt.getRoute());
                ses.saveOrUpdate(pt);
            }
            ses.saveOrUpdate(wr);
        }
        ses.saveOrUpdate(r);
        HibernateUtil.closeSession(ses);
    }

    @Transactional
    @Override
    public void addNonRepeatingRoute(Route r)
    {
        Session ses = HibernateUtil.openSession();
        for (PlaceTime pt : r.getPlaceTimes())
        {
            ses.saveOrUpdate(pt.getPlace());
            ses.saveOrUpdate(pt.getRoute());
            ses.saveOrUpdate(pt);
        }
        ses.saveOrUpdate(r);
        HibernateUtil.closeSession(ses);
    }

    @Override
    public void addPlace(Place p) {
        Session ses = HibernateUtil.openSession();
        ses.saveOrUpdate(p);
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
    }*/

    @Override
    public void addWeekdayRoute(WeekdayRoute wr) {
        Session ses = HibernateUtil.openSession();
        ses.saveOrUpdate(wr.getRoute());
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
        Session ses = HibernateUtil.openSession();
        for (PlaceTime t : placetimes)
        {
            t.setWeekdayRoute(wr);
            ses.saveOrUpdate(t);
        }
        wr.setPlaceTimes(placetimes);
        ses.saveOrUpdate(wr);
        HibernateUtil.closeSession(ses);
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
        pt.getTrajecten().size();
        HibernateUtil.closeSession(ses);
        return pt;
    }

    @Override
    public Route getRouteById(int routeId) {
        Session ses = HibernateUtil.openSession();
        Query query = ses.createQuery("select ro from Route ro where ro.id = :id");
        query.setInteger("id", routeId);
        Route route = (Route)query.uniqueResult();
        HibernateUtil.closeSession(ses);
        return route;
    }

    @Override
    public List<WeekdayRoute> getWeekdayRoutesOfRoute(int routeId) {
        Session ses = HibernateUtil.openSession();
        Query query = ses.createQuery("select wdr from WeekdayRoute wdr left join wdr.route where wdr.route.id = :routeId");
        query.setInteger("routeId",routeId);
        List<WeekdayRoute> wdrs = query.list();
        HibernateUtil.closeSession(ses);
        return wdrs;
    }

    @Override
    public List<Route> getRoutes(User user) {
        Session ses = HibernateUtil.openSession();
        Query query = ses.createQuery("from Route r where r.chauffeur.id = :userId");
        query.setInteger("userId",user.getId());
        List<Route> routes = query.list();
        HibernateUtil.closeSession(ses);
        return routes;
    }

    public List<Route> findCarpoolers(PlaceTime pt1, PlaceTime pt2, User.Gender g, boolean smoker, double radius) {    // TODO: NOT FINISHED!
        Session ses = HibernateUtil.openSession();
        Query query = ses.createQuery("   " +
                "FROM t_route r " +
                "JOIN t_user u ON r.userId = u.id " +
                "JOIN t_placetime pt ON pt.routeId = r.routeId " +
                "JOIN t_place p ON p.placeId = pt.placeId " +
                "WHERE u.smoker = :sm AND u.gender = :g " +
                "      AND ABS(p.lat - :p1Lat) <= :r AND ABS(p.lon - :p1Lon) <= :r" +
                "      AND ABS(p.lat - :p2Lat) <= :r AND ABS(p.lon - :p2Lon) <= :r");  // http://www.tutorialspoint.com/hibernate/hibernate_query_language.htm  //FROM Route r INNER JOIN r.user u WHERE u.smoker = :sm AND u.gender = :g
        query.setBoolean("sm", smoker);
        query.setParameter("g", g);
        query.setParameter("p1Lat", pt1.getPlace().getLat());
        query.setParameter("p1Lon", pt1.getPlace().getLon());
        query.setParameter("p2Lat", pt2.getPlace().getLat());
        query.setParameter("p2Lon", pt2.getPlace().getLon());
        List<Route> rtval = query.list();
        HibernateUtil.closeSession(ses);
        return rtval;
    }

    public void addPlaceTime(PlaceTime pt) {
        Session ses = HibernateUtil.openSession();
        ses.saveOrUpdate(pt);
        HibernateUtil.closeSession(ses);
    }
}
