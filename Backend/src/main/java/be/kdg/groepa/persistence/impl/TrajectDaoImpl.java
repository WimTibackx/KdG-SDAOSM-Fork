package be.kdg.groepa.persistence.impl;

import be.kdg.groepa.model.PlaceTime;
import be.kdg.groepa.model.Route;
import be.kdg.groepa.model.Traject;
import be.kdg.groepa.persistence.api.TrajectDao;
import be.kdg.groepa.persistence.util.HibernateUtil;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 * Created by Tim on 19/02/14.
 */
@SuppressWarnings("JpaQlInspection")
@Repository("trajectDao")
public class TrajectDaoImpl implements TrajectDao {
    @Override
    public void addTraject(Traject traject) {
        Session ses = HibernateUtil.openSession();
        ses.saveOrUpdate(traject.getPickup().getPlace());
        ses.saveOrUpdate(traject.getPickup());
        ses.saveOrUpdate(traject.getDropoff().getPlace());
        ses.saveOrUpdate(traject.getDropoff());
        ses.saveOrUpdate(traject.getRoute());
        ses.saveOrUpdate(traject);
        HibernateUtil.closeSession(ses);
    }

    @Override
    public void removeTrajectFromRoute(Route route, Traject traj) {
        Session ses = HibernateUtil.openSession();
        boolean containsPickup = false, containsDropoff = false;
        // If the pickup/dropoff points aren't used by any other trajects, remove them. Otherwise keep them.
        for (Traject tr : route.getTrajects()) {
            if (tr.getDropoff() == traj.getDropoff() || tr.getPickup() == traj.getDropoff()) {
                containsDropoff = true;
            }
            if (tr.getDropoff() == traj.getPickup() || tr.getPickup() == traj.getPickup()) {
                containsPickup = true;
            }
        }
        if(!containsDropoff){
            ses.delete(traj.getDropoff());
        }
        if(!containsPickup){
            ses.delete(traj.getPickup());
        }
        ses.delete(traj);
        ses.saveOrUpdate(route);
        HibernateUtil.closeSession(ses);
    }

    @Override
    public void saveRoute(Route route) {
        Session ses = HibernateUtil.openSession();
        ses.saveOrUpdate(route);
        HibernateUtil.closeSession(ses);
    }

    @Override
    public void saveRouteAndPoints(Route route, PlaceTime previousPlaceTime, PlaceTime newPlaceTime) {
        Session ses = HibernateUtil.openSession();
        ses.saveOrUpdate(previousPlaceTime.getPlace());
        ses.saveOrUpdate(newPlaceTime.getPlace());
        ses.saveOrUpdate(previousPlaceTime);
        ses.saveOrUpdate(newPlaceTime);
        ses.saveOrUpdate(route);
        HibernateUtil.closeSession(ses);
    }
}