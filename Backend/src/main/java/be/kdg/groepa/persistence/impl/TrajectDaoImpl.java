package be.kdg.groepa.persistence.impl;

import be.kdg.groepa.model.PlaceTime;
import be.kdg.groepa.model.Route;
import be.kdg.groepa.model.Traject;
import be.kdg.groepa.model.User;
import be.kdg.groepa.persistence.api.TrajectDao;
import be.kdg.groepa.persistence.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

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
        ses.saveOrUpdate(traject.getRoute().getChauffeur());
        ses.saveOrUpdate(traject.getRoute());
        ses.saveOrUpdate(traject.getUser());
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
        ses.saveOrUpdate(previousPlaceTime.getRoute());
        ses.saveOrUpdate(previousPlaceTime);
        ses.saveOrUpdate(newPlaceTime.getRoute());
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

    @Override
    public List<Traject> getAcceptedTrajects(User user) {
        Session ses = HibernateUtil.openSession();
        Query query = ses.createQuery("select t from Traject t join t.pickup join t.dropoff join t.route join t.pickup.place join t.dropoff.place join t.pickup.weekdayRoute join t.dropoff.weekdayRoute join t.route.chauffeur join t.route.car where t.user.id = :userid and t.isAccepted = true");
        query.setParameter("userid", user.getId());
        List<Traject> trajects = query.list();
        HibernateUtil.closeSession(ses);
        return trajects;
    }

    @Override
    public List<Traject> getRequestedTrajects(User user) {
        Session ses = HibernateUtil.openSession();
        Query query = ses.createQuery("select t from Traject t where isAccepted = false and user.id = :userId");
        query.setParameter("userId", user.getId());
        List<Traject> trajects = query.list();
        HibernateUtil.closeSession(ses);
        return trajects;
    }

    @Override
    public List<Traject> getRequestedOnMyRoutes(User user) {
        Session ses = HibernateUtil.openSession();
        Query query = ses.createQuery("select t from Traject t join t.route where isAccepted = false and t.route.chauffeur.id = :userId");
        query.setParameter("userId", user.getId());
        List<Traject> trajects = query.list();
        HibernateUtil.closeSession(ses);
        return trajects;
    }

    @Override
    public void updateTraject(Traject t) {
        Session ses = HibernateUtil.openSession();
        ses.saveOrUpdate(t);
        HibernateUtil.closeSession(ses);
    }

    @Override
    public void removeTraject(Traject t) {
        Session ses = HibernateUtil.openSession();
        ses.delete(t);
        HibernateUtil.closeSession(ses);
    }

    @Override
    public User getChauffeurByTraject(Traject t) {
        Session ses = HibernateUtil.openSession();
        Query query = ses.createQuery("select u from Traject t join t.route join t.route.chauffeur u where t.id = :trajectId");
        query.setParameter("trajectId", t.getId());
        User user = (User)query.uniqueResult();
        HibernateUtil.closeSession(ses);
        return user;
    }
}
