package be.kdg.groepa.persistence.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;


public class HibernateUtil {
    private static SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure("hibernateMySQL.cfg.xml");
            ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session getSession(){
        return sessionFactory.getCurrentSession();
    }

    public static Session openSession(){
        Session ses = sessionFactory.openSession();
        ses.beginTransaction();
        return ses;
    }

    public static void closeSession(Session ses){
        ses.getTransaction().commit();
        ses.close();
    }

    public static void addObject(Object ob){
        Session ses = getSessionFactory().openSession();
        Transaction tx = ses.beginTransaction();
        ses.saveOrUpdate(ob);
        tx.commit();
    }

    public static void deleteObject(Object ob) {
        Session ses = getSessionFactory().openSession();
        Transaction tx = ses.beginTransaction();
        ses.delete(ob);
        tx.commit();
    }
}
