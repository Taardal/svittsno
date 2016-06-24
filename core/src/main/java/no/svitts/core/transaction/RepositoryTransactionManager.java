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
            String errorMessage = "Could not execute transaction";
            LOGGER.error(errorMessage, e);
            throw new TransactionException(errorMessage, e);
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
            String errorMessage = "Could not execute transaction without result";
            LOGGER.error(errorMessage, e);
            throw new TransactionException(errorMessage, e);
        }
    }

    private Transaction beginTransaction() {
        return getCurrentSession().beginTransaction();
    }

    private void commitTransaction() {
        getCurrentSession().getTransaction().commit();
    }

    private void rollbackTransaction() {
        try {
            getCurrentSession().getTransaction().rollback();
        } catch (HibernateException e) {
            String errorMessage = "Could not rollback transaction";
            LOGGER.error(errorMessage, e);
            throw new TransactionException(errorMessage, e);
        }
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

}
