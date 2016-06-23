package no.svitts.core.transaction;

import com.google.inject.Inject;
import no.svitts.core.exception.RepositoryException;
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
    public <R> R transaction(UnitOfWork<T, R> unitOfWork) {
        try {
            beginTransaction();
            R result = unitOfWork.execute(repository);
            commitTransaction();
            return result;
        } catch (HibernateException | RepositoryException e) {
            rollbackTransaction();
            LOGGER.error("Could not execute transaction", e);
            throw new TransactionException(e);
        }
    }

    @Override
    public void transactionWithoutResult(UnitOfWorkWithoutResult<T> unitOfWorkWithoutResult) {
        try {
            beginTransaction();
            unitOfWorkWithoutResult.execute(repository);
            commitTransaction();
        } catch (HibernateException | RepositoryException e) {
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
