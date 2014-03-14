package be.kdg.groepa.persistence.impl;

import be.kdg.groepa.controllers.LoginController;
import be.kdg.groepa.model.*;
import be.kdg.groepa.persistence.api.RouteDao;
import be.kdg.groepa.persistence.util.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

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
    public void confirmRide(Route r, LocalDateTime date) {
        Ride ride = new Ride(r, date);
        for (Traject t : r.getTrajects())
        {
            ride.setTraject(t);
            t.addRide(ride);
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
        pt.getPlace().getPlaceTimes().size();
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

    //public List<Route> findCarpoolers(PlaceTime pt1, PlaceTime pt2, User.Gender g, boolean smoker, double radius) {    // TODO: NOT FINISHED!
    public List<Integer> findCarpoolers(double startLat, double startLon, double endLat, double endLon, User.Gender g, boolean smoker, double radius, LocalTime dep, int timeDiff) {    // TODO: NOT FINISHED!
        Session ses = HibernateUtil.openSession();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        Logger logger =  Logger.getLogger(LoginController.class.getName());
        int genderInt = g.equals(User.Gender.FEMALE)? 1 : 0 ;
        int smokerInt = smoker ? 1: 0;


        double latRadius = radius / 111;
        double amountKmAtStartLong = ((Math.PI / 180)*6365*Math.cos(startLon));
        double amountKmAtEndLong = ((Math.PI / 180)*6365*Math.cos(endLon));
        double lonStartRadius = radius / amountKmAtStartLong;
        double lonEndRadius = radius / amountKmAtEndLong;



        logger.info("lat: " + (startLat + latRadius ) + "Lon: " + ((Math.abs(lonStartRadius) + startLon)));
        logger.info("lat: " + (endLat + latRadius ) + "Lon: " + ((Math.abs(lonEndRadius) + endLon)));
        Query query = ses.createSQLQuery("SELECT DISTINCT r.id FROM t_route r " +
                "JOIN t_user u ON r.userId = u.id " +
                "JOIN t_placetime pt ON pt.routeId = r.id " +
                "JOIN t_place p ON p.placeId = pt.placeId " +
                "WHERE ABS(p.lat - :p1Lat) <= :rLat AND ABS(p.lon - :p1Lon) <= :rLonStart " +
                "AND ABS(p.lat - :p2Lat) <= :rLat AND ABS(p.lon - :p2Lon) <= :rLonEnd AND u.gender = :g " +
                "AND u.smoker = :sm " +
                "AND ABS((TIME_TO_SEC(pt.time) -  TIME_TO_SEC(:dep))) <= :td");
        query.setParameter("g", genderInt);
        query.setParameter("p1Lat", startLat);
        query.setParameter("p1Lon", startLon);
        query.setParameter("p2Lat", endLat);
        query.setParameter("p2Lon", endLon);
        query.setDouble("rLat", Math.abs(latRadius));
        query.setDouble("rLonStart", Math.abs(lonStartRadius));
        query.setDouble("rLonEnd", Math.abs(lonEndRadius));
        query.setParameter("dep", dep.toString(dtf));
        query.setParameter("td", timeDiff);
        query.setParameter("sm", smokerInt) ;
        List<Integer> rtval = query.list();
        logger.info(rtval.size());
        HibernateUtil.closeSession(ses);
        return rtval;
    }

    public void addPlaceTime(PlaceTime pt) {
        Session ses = HibernateUtil.openSession();
        ses.saveOrUpdate(pt);
        HibernateUtil.closeSession(ses);
    }

    @Override
    public void updateRoute(Route r) {
        Session ses = HibernateUtil.openSession();
        ses.saveOrUpdate(r);
        HibernateUtil.closeSession(ses);
    }
}
