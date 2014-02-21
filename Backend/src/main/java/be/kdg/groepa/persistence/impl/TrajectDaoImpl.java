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
public class TrajectDaoImpl implements TrajectDao{
    @Override
    public void addTrajectToRoute(Traject traject, Route route) {
        Session ses = HibernateUtil.openSession();
        traject.setRoute(route);
        for(PlaceTime pt:traject.getPlaceTimes()){
            ses.saveOrUpdate(pt.getPlace());
            ses.saveOrUpdate(pt);
        }
        route.addTraject(traject);
        ses.saveOrUpdate(traject);
        ses.saveOrUpdate(route.getChauffeur());
        ses.saveOrUpdate(route);
        HibernateUtil.closeSession(ses);
    }
}
