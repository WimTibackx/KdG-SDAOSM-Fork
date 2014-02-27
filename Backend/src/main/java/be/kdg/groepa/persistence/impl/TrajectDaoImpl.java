package be.kdg.groepa.persistence.impl;

import be.kdg.groepa.model.PlaceTime;
import be.kdg.groepa.model.Route;
import be.kdg.groepa.model.Traject;
import be.kdg.groepa.persistence.api.TrajectDao;
import be.kdg.groepa.persistence.util.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by Tim on 19/02/14.
 */
@SuppressWarnings("JpaQlInspection")
@Repository("trajectDao")
public class TrajectDaoImpl implements TrajectDao {

    @Transactional
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

    @Transactional
    @Override
    public void removeTrajectFromRoute(Route route, Traject traj) {
        Session ses = HibernateUtil.openSession();
        route.removeTraject(traj);
        ses.delete(traj);
        ses.saveOrUpdate(route.getChauffeur());
        ses.saveOrUpdate(route);
        HibernateUtil.closeSession(ses);
    }

    @Override
    public void saveRoute(Route route) {
        Session ses = HibernateUtil.openSession();
        ses.saveOrUpdate(route);
        HibernateUtil.closeSession(ses);
    }

    @Transactional
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

    @Override
    public Traject getTrajectById(int trajectId) {
        Session ses = HibernateUtil.openSession();
        Query query = ses.createQuery("from Traject traj where traj.id = :id");
        query.setInteger("id", trajectId);
        Traject traject = (Traject)query.uniqueResult();
        HibernateUtil.closeSession(ses);
        return traject;
    }
}
