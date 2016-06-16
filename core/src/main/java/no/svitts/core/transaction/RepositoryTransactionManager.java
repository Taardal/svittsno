package no.svitts.core.transaction;

import no.svitts.core.exception.TransactionException;
import no.svitts.core.repository.Repository;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RepositoryTransactionManager<T> implements TransactionManager<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryTransactionManager.class);

    private Repository<T> repository;
    private SessionFactory sessionFactory;

    public RepositoryTransactionManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public RepositoryTransactionManager(Repository<T> repository, SessionFactory sessionFactory) {
        this.repository = repository;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public <R> R executeTransaction(TransactionCallback<T, R> transactionCallback) {
        try {
            beginTransaction();
            R result = transactionCallback.execute(repository);
            commitTransaction();
            return result;
        } catch (HibernateException e) {
            rollbackTransaction();
            LOGGER.error("Could not executeTransaction executeTransaction", e);
            throw new TransactionException(e);
        }
    }

    @Override
    public void executeTransactionWithoutResult(TransactionCallbackWithoutResult<T> transactionCallbackWithoutResult) {
        try {
            beginTransaction();
            transactionCallbackWithoutResult.execute(repository);
            commitTransaction();
        } catch (HibernateException e) {
            rollbackTransaction();
            LOGGER.error("Could not executeTransaction executeTransaction", e);
            throw new TransactionException(e);
        }
    }

    @Override
    public void setRepository(Repository<T> repository) {
        this.repository = repository;
    }

    private Transaction beginTransaction() {
        return getCurrentSession().beginTransaction();
    }

    private void commitTransaction() {
        getCurrentSession().getTransaction().commit();
    }

    private void rollbackTransaction() {
        getCurrentSession().getTransaction().rollback();
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    private SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration().configure("hibernate_sp.cfg.xml");
        StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        return configuration.buildSessionFactory(standardServiceRegistryBuilder.build());
    }

}
