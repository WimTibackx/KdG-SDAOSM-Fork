package be.kdg.groepa.persistence.impl;


import be.kdg.groepa.model.*;
import be.kdg.groepa.persistence.api.UserDao;
import be.kdg.groepa.persistence.util.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.threeten.bp.LocalDateTime;

import javax.transaction.Transactional;
import java.io.File;
import java.util.List;


@SuppressWarnings("JpaQlInspection")
@Repository("userDao")
public class UserDaoImpl implements UserDao {

    public UserDaoImpl(){

    }

    public User getUser(String username){
        Session ses = HibernateUtil.openSession();
        Query query = ses.createQuery("from User u where u.username = :username");
        query.setString("username", username);
        User user = (User)query.uniqueResult();
        HibernateUtil.closeSession(ses);
        return user;

    }

    public User getUser(Integer id){
        Session ses = HibernateUtil.openSession();
        Query query = ses.createQuery("from User u where u.id = :id");
        query.setInteger("id", id);
        User user = (User)query.uniqueResult();
        HibernateUtil.closeSession(ses);
        return user;

    }

    @Transactional
    @Override
    public void changePassword(String username, String newPassword)
    {
        Session ses = HibernateUtil.openSession();
        Query query = ses.createQuery("FROM User u WHERE u.username = :username").setString("username", username);
        User u = (User)query.uniqueResult();
        u.setPassword(newPassword);
        ses.saveOrUpdate(u);
        HibernateUtil.closeSession(ses);
    }

    @Override
    public void createSession(SessionObject session) {
        HibernateUtil.addObject(session);
    }

    @Override
    public SessionObject getSession(String token) {
        Session ses = HibernateUtil.openSession();
        Query query = ses.createQuery("from SessionObject s where s.sessionToken = :token");
        query.setString("token", token);
        SessionObject s = (SessionObject)query.uniqueResult();
        HibernateUtil.closeSession(ses);
        return s;
    }

    @Override
    public void deleteSession(SessionObject session) {
        HibernateUtil.deleteObject(session);
    }

    @Override
    public void extendSession(SessionObject session) {
        Session dbSes = HibernateUtil.openSession();
        session.setExperiationDate(LocalDateTime.now().plusDays(1L));
        dbSes.saveOrUpdate(session);
        HibernateUtil.closeSession(dbSes);
    }

    @Override
    public void addUser(User u)
    {
        HibernateUtil.addObject(u);
    }

    @Override
    public SessionObject getSessionByUsername(String username) {
        Session session = HibernateUtil.openSession();
        Query query = session.createQuery("from SessionObject s where s.user.username = :username");
        query.setString("username", username);
        SessionObject s = (SessionObject)query.uniqueResult();
        HibernateUtil.closeSession(session);
        return s;
    }


    @Transactional
    @Override
    public void addCarToUser(String username, Car c) {
        Session ses = HibernateUtil.openSession();
        User u = getUser(username);
        u.addCar(c);
        c.setUser(u);
        ses.saveOrUpdate(u);
        ses.saveOrUpdate(c);
        ses.flush();
        HibernateUtil.closeSession(ses);
    }

    @Override
    public void updateCar(Car c) {
        Session ses = HibernateUtil.openSession();
        ses.saveOrUpdate(c);
        ses.flush();
        HibernateUtil.closeSession(ses);
    }

    @Override
    public void editUserPicture(String username, File newPicture) {
        Session ses = HibernateUtil.openSession();
        User u = getUser(username);
        u.editImage(newPicture);
        ses.saveOrUpdate(u);
        HibernateUtil.closeSession(ses);
    }

    @Override
    public void removeUserPicture(String username) {
        Session ses = HibernateUtil.openSession();
        User u = getUser(username);
        u.removeImage();
        ses.saveOrUpdate(u);
        HibernateUtil.closeSession(ses);
    }

    @Override
    public void removeCarPicture(Car car) {
        Session ses = HibernateUtil.openSession();
        car.removeImage();
        ses.saveOrUpdate(car);
        HibernateUtil.closeSession(ses);
    }

    @Override
    public Car getCar(int id) {
        Session ses = HibernateUtil.openSession();
        Query query = ses.createQuery("from Car c where c.id = :id");
        query.setInteger("id",id);
        Car c = (Car)query.uniqueResult();
        HibernateUtil.closeSession(ses);
        return c;
    }

    @Override
    public void deleteCar(Car car) {
        Session ses = HibernateUtil.openSession();
        ses.delete(car);
        HibernateUtil.closeSession(ses);
    }

    @Override
    public void updateUser(User user) {
        Session ses = HibernateUtil.openSession();
        ses.saveOrUpdate(user);
        HibernateUtil.closeSession(ses);
    }

    @Override
    public List<Route> getRoutesFromUser(String username) {
        Session ses = HibernateUtil.openSession();
        Query query = ses.createQuery("from Route r where r.chauffeur.username = :username");
        query.setString("username",username);
        return query.list();
    }

    @Override
    public void setUserAndroidId(String username, String id) {
        Session ses = HibernateUtil.openSession();
        User u = getUser(username);
        u.setAndroidId(id);
        ses.saveOrUpdate(u);
        HibernateUtil.closeSession(ses);
    }
}
