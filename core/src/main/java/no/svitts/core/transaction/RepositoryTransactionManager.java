package no.svitts.core.transaction;

import com.google.inject.Inject;
import no.svitts.core.exception.TransactionException;
import no.svitts.core.repository.Repository;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RepositoryTransactionManager<T> implements TransactionManager<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryTransactionManager.class);

    private Repository<T> repository;
    private SessionFactory sessionFactory;

    @Inject
    public RepositoryTransactionManager(Repository<T> repository, SessionFactory sessionFactory) {
        this.repository = repository;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public <R> R transaction(TransactionCallback<T, R> transactionCallback) {
        try {
            beginTransaction();
            R result = transactionCallback.execute(repository);
            commitTransaction();
            return result;
        } catch (HibernateException e) {
            rollbackTransaction();
            LOGGER.error("Could not execute transaction", e);
            throw new TransactionException(e);
        }
    }

    @Override
    public void transactionWithoutResult(TransactionCallbackWithoutResult<T> transactionCallbackWithoutResult) {
        try {
            beginTransaction();
            transactionCallbackWithoutResult.execute(repository);
            commitTransaction();
        } catch (HibernateException e) {
            rollbackTransaction();
            LOGGER.error("Could not execute transaction without result", e);
            throw new TransactionException(e);
        }
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

}
