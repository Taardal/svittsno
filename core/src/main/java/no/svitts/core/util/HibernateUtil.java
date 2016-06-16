package no.svitts.core.util;

import no.svitts.core.repository.Repository;
import no.svitts.core.unitofwork.UnitOfWork;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

    public static <T, R> R transaction(Repository<T> repository, UnitOfWork<T, R> unitOfWork) {
        try {
            beginTransaction();
            R result = unitOfWork.execute(repository);
            commitTransaction();
            return result;
        } catch (HibernateException e) {
            rollbackTransaction();
            throw e;
        }
    }

    public static Session getCurrentSession() {
        return SESSION_FACTORY.getCurrentSession();
    }

    private static Transaction beginTransaction() {
        return getCurrentSession().beginTransaction();
    }

    private static void commitTransaction() {
        getCurrentSession().getTransaction().commit();
    }

    private static void rollbackTransaction() {
        getCurrentSession().getTransaction().rollback();
    }

    private static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration().configure("hibernate_sp.cfg.xml");
        StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        return configuration.buildSessionFactory(standardServiceRegistryBuilder.build());
    }

}
