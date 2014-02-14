package be.kdg.groepa.persistence.impl;


import be.kdg.groepa.model.Car;
import be.kdg.groepa.model.SessionObject;
import be.kdg.groepa.model.User;
import be.kdg.groepa.persistence.api.UserDao;
import be.kdg.groepa.persistence.util.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;


@SuppressWarnings("JpaQlInspection")
@Repository("userDao")
public class UserDaoImpl implements UserDao {

    public UserDaoImpl(){

    }

    public User getUser(String username){
        Session ses = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = ses.beginTransaction();
        Query query = ses.createQuery("from User u where u.username = :username");
        //Query query = HibernateUtil.getSessionFactory().openSession().createQuery("from User u where u.username = :username");
        query.setString("username", username);
        User user = (User)query.uniqueResult();
        tx.commit();
        ses.close();
        return user;

    }

    @Override
    public void createSession(SessionObject session) {
        HibernateUtil.addObject(session);
    }

    @Override
    public SessionObject getSession(String token) {
        Session ses = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = ses.beginTransaction();
        Query query = ses.createQuery("from SessionObject s where s.sessionToken = :token");
        query.setString("token", token);
        SessionObject s = (SessionObject)query.uniqueResult();
        tx.commit();
        ses.close();
        return s;
    }

    @Override
    public void deleteSession(SessionObject session) {
        HibernateUtil.deleteObject(session);
    }

    @Override
    public void extendSession(SessionObject session) { // String token
        //SessionObject session = sessions.get(token);
        Session dbSes = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = dbSes.beginTransaction();
        session.setExperiationDate(session.getExperiationDate().plusDays(1L));
        dbSes.saveOrUpdate(session);
        tx.commit();
        dbSes.close();
    }

    public void addUser(User u) throws Exception
    {
        HibernateUtil.addObject(u);
        /*if (users.contains(u.getUsername())) throw new UserExistException("User already exists");
        users.put(u.getUsername(), u);*/
    }

    @Override
    public SessionObject getSessionByUsername(String username) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("from SessionObject s where s.user.username = :username");
        query.setString("username", username);
        SessionObject s = (SessionObject)query.uniqueResult();
        tx.commit();
        session.close();
        return s;
    }


    @Override
    public void addCarToUser(String username, Car c) {
        Session ses = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = ses.beginTransaction();
        User u = getUser(username);
        u.addCar(c);
        c.setUser(u);
        ses.saveOrUpdate(u);
        ses.save(c);
        tx.commit();
        ses.close();
    }
}
