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

    private SessionFactory sessionFactory;

    @Inject
    public RepositoryTransactionManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public <R> R transaction(Repository<T> repository, UnitOfWork<T, R> unitOfWork) {
        Session currentSession = getCurrentSession();
        Transaction transaction = currentSession.beginTransaction();
        try {
            R result = unitOfWork.execute(repository);
            transaction.commit();
            return result;
        } catch (RepositoryException | IllegalStateException | RollbackException e) {
            rollbackTransaction(transaction);
            LOGGER.error("Could not execute transcation", e);
            throw new TransactionException(e);
        } finally {
            if (currentSession.isOpen()) {
                closeSession(currentSession);
            }
        }
    }

    @Override
    public void transactionWithoutResult(Repository<T> repository, UnitOfWorkWithoutResult<T> unitOfWorkWithoutResult) {
        Session currentSession = getCurrentSession();
        Transaction transaction = currentSession.beginTransaction();
        try {
            unitOfWorkWithoutResult.execute(repository);
            transaction.commit();
        } catch (RepositoryException | IllegalStateException | RollbackException e) {
            rollbackTransaction(transaction);
            LOGGER.error("Could not execute transcation", e);
            throw new TransactionException(e);
        } finally {
            if (currentSession.isOpen()) {
                closeSession(currentSession);
            }
        }
    }

    private Session getCurrentSession() {
        try {
            return sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            LOGGER.error("Could not get current session", e);
            throw new TransactionException(e);
        }
    }

    private void rollbackTransaction(Transaction transaction) {
        try {
            transaction.rollback();
        } catch (IllegalStateException | PersistenceException e) {
            LOGGER.error("Could not rollback transaction", e);
            throw new TransactionException(e);
        }
    }

    private void closeSession(Session session) {
        try {
            session.close();
        } catch (Exception e) {
            LOGGER.error("Could not close session", e);
            throw new TransactionException(e);
        }
    }

}
