package no.svitts.core.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateUtil.class);
    private static final SessionFactory SESSION_FACTORY;

    static {
        SESSION_FACTORY = buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public static Session beginTransaction() {
        Session currentSession = getCurrentSession();
        currentSession.beginTransaction();
        return currentSession;
    }

    public static void commitTransaction() {
        getCurrentSession().getTransaction().commit();
    }

    public static void rollbackTransaction() {
        getCurrentSession().getTransaction().rollback();
    }

    public static void closeSession() {
        getCurrentSession().close();

    }

    public static Session getCurrentSession() {
        return SESSION_FACTORY.getCurrentSession();
    }

    private static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration().configure("hibernate_sp.cfg.xml");
        StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        return configuration.buildSessionFactory(standardServiceRegistryBuilder.build());
    }

}
