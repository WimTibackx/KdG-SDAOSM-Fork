package be.kdg.groepa.service.impl;

import be.kdg.groepa.model.Route;
import be.kdg.groepa.model.Traject;
import be.kdg.groepa.persistence.api.TrajectDao;
import be.kdg.groepa.service.api.TrajectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Tim on 19/02/14.
 */
@Service("TrajectService")
public class TrajectServiceImpl implements TrajectService {
    @Autowired
    private TrajectDao trajectDao;

    @Override
    public void addTrajectToRoute(Traject t, Route r) {
        trajectDao.addTrajectToRoute(t, r);
    }
}
