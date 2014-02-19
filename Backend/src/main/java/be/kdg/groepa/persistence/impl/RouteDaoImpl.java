package be.kdg.groepa.persistence.impl;

import be.kdg.groepa.model.Place;
import be.kdg.groepa.model.PlaceTime;
import be.kdg.groepa.model.Route;
import be.kdg.groepa.model.WeekdayRoute;
import be.kdg.groepa.persistence.api.RouteDao;
import be.kdg.groepa.persistence.util.HibernateUtil;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 * Created by Pieter-Jan on 18-2-14.
 */

@SuppressWarnings("JpaQlInspection")
@Repository("routeDao")
public class RouteDaoImpl implements RouteDao {

    @Override
    public void addRoute(Route r) {
        Session ses = HibernateUtil.openSession();
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
    public void addPlaceTime(PlaceTime pt) {
        Session ses = HibernateUtil.openSession();
        ses.saveOrUpdate(pt);
        HibernateUtil.closeSession(ses);
    }

    @Override
    public void addWeekdayRoute(WeekdayRoute wr) {
        Session ses = HibernateUtil.openSession();
        ses.saveOrUpdate(wr);
        HibernateUtil.closeSession(ses);
    }

}
