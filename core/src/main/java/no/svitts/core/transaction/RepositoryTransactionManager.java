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

import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;

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
        LOGGER.info("Making transaction.");
        Session currentSession = getCurrentSession();
        Transaction transaction = currentSession.beginTransaction();
        try {
            LOGGER.info("Executing unit of work.");
            R result = unitOfWork.execute(repository);
            transaction.commit();
            LOGGER.info("Transaction committed.");
            return result;
        } catch (RepositoryException | IllegalStateException | RollbackException e) {
            rollbackTransaction(transaction);
            LOGGER.error("Could not do transcation.", e);
            throw new TransactionException(e);
        } finally {
            closeSession(currentSession);
        }
    }

    @Override
    public void transactionWithoutResult(UnitOfWorkWithoutResult<T> unitOfWorkWithoutResult) {
        LOGGER.info("Making transaction without result.");
        Session currentSession = getCurrentSession();
        Transaction transaction = currentSession.beginTransaction();
        try {
            LOGGER.info("Executing unit of work.");
            unitOfWorkWithoutResult.execute(repository);
            transaction.commit();
            LOGGER.info("Transaction committed.");
        } catch (RepositoryException | IllegalStateException | RollbackException e) {
            rollbackTransaction(transaction);
            LOGGER.error("Could not do transcation.", e);
            throw new TransactionException(e);
        } finally {
            closeSession(currentSession);
        }
    }

    private Session getCurrentSession() {
        LOGGER.info("Getting current session.");
        try {
            return sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            LOGGER.error("Could not get current session.", e);
            throw new TransactionException(e);
        }
    }

    private void rollbackTransaction(Transaction transaction) {
        LOGGER.info("Rolling back transaction [{}].", transaction.toString());
        try {
            transaction.rollback();
        } catch (IllegalStateException | PersistenceException e) {
            LOGGER.error("Could not rollback transaction[{}].", transaction.toString(), e);
            throw new TransactionException(e);
        }
    }

    private void closeSession(Session session) {
        LOGGER.info("Closing session [{}].", session.toString());
        try {
            session.close();
        } catch (HibernateException e) {
            LOGGER.error("Could not close session [{}].", session.toString(), e);
            throw new TransactionException(e);
        }
    }

}
