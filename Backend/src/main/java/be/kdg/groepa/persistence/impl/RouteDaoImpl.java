package be.kdg.groepa.persistence.impl;

import be.kdg.groepa.model.*;
import be.kdg.groepa.persistence.api.RouteDao;
import be.kdg.groepa.persistence.util.HibernateUtil;
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

        /*Session ses = HibernateUtil.openSession();
        for(PlaceTime pt:r.getAllPlaceTimes()){
            ses.saveOrUpdate(pt.getPlace());
            ses.saveOrUpdate(pt);
        }
        ses.saveOrUpdate(r);
        HibernateUtil.closeSession(ses);
        // HibernateUtil.addObject(r); ???

        Session ses = HibernateUtil.openSession();
        List<WeekdayRoute> wrs = r.getWeekdayRoutes();
        for (int j = 0; j < wrs.size(); j++)
        {
            WeekdayRoute wr = wrs.get(j);
            int sz = wr.getPlaceTimes().size();
            for (int i = 0; i < sz; i++)
            {
                System.out.println(wr.getPlaceTimes().size());
                PlaceTime pt = wr.getPlaceTimes().get(i);
                savePlaceTimeToPlace(pt, pt.getPlace(), ses);
                pt.setWeekdayRoute(wr);
                wr.addPlaceTime(pt);
                ses.saveOrUpdate(pt);
            }
            ses.saveOrUpdate(wr);
            r.addWeekdayRoute(wr);
            wr.setRoute(r);
            ses.saveOrUpdate(r);
        }*/

        Session ses = HibernateUtil.openSession();
        List<WeekdayRoute> wrs = r.getWeekdayRoutes();
        for (WeekdayRoute wr : wrs)
        {
            List<PlaceTime> placetimes = wr.getPlaceTimes();
            for (PlaceTime pt : placetimes)
            {
                ses.saveOrUpdate(pt.getPlace());
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
            ses.saveOrUpdate(pt.getPlace());//savePlaceTimeToPlace(pt, pt.getPlace(), ses);
            ses.saveOrUpdate(pt);
        }
        ses.saveOrUpdate(r);
        HibernateUtil.closeSession(ses);
    }

    private void savePlaceTimeToPlace(PlaceTime pt, Place p,Session s)
    {
        Query q = s.createQuery("FROM Place plaats WHERE plaats.name = :na");
        q.setString("na", p.getName());
        s.saveOrUpdate(pt);
        Place place = null;//(Place)q.uniqueResult();
        if (place != null)
        {
            place.addPlaceTime(pt);
            pt.setPlace(place);
            s.saveOrUpdate(place);
            s.saveOrUpdate(pt);
            p = null;
            p = place;
        }
        else
        {
            s.saveOrUpdate(p);     // Might have to change
            p.addPlaceTime(pt);
            pt.setPlace(p);
            s.saveOrUpdate(p);
            s.saveOrUpdate(pt);
        }
    }




    /*
    @Override
    public void addRepeatingRoute(Route r)
    {
        //List<Place> places = r.
    } */

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
        p.addPlaceTime(pt);
        ses.saveOrUpdate(pt);
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

    @Override
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
}
