package no.svitts.core.service;

import com.google.inject.Inject;
import no.svitts.core.exception.RepositoryException;
import no.svitts.core.exception.ServiceException;
import no.svitts.core.movie.Movie;
import no.svitts.core.repository.Repository;
import no.svitts.core.transaction.UnitOfWork;
import no.svitts.core.transaction.UnitOfWorkWithoutResult;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.RollbackException;

abstract class CoreService<T> implements Service<Movie> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoreService.class);

    private Repository<T> repository;
    private SessionFactory sessionFactory;

    @Inject
    public CoreService(Repository<T> repository, SessionFactory sessionFactory) {
        this.repository = repository;
        this.sessionFactory = sessionFactory;
    }

    <R> R transaction(UnitOfWork<T, R> unitOfWork) {
        LOGGER.info("Making transaction.");
        Session currentSession = sessionFactory.getCurrentSession();
        Transaction transaction = currentSession.beginTransaction();
        try {
            LOGGER.info("Executing unit of work.");
            R result = unitOfWork.execute(repository);
            transaction.commit();
            LOGGER.info("Transaction committed.");
            return result;
        } catch (RepositoryException | IllegalStateException | RollbackException e) {
            transaction.rollback();
            LOGGER.error("Could not do transcation.", e);
            throw new ServiceException(e);
        } finally {
            currentSession.close();
        }
    }

    void transactionWithoutResult(UnitOfWorkWithoutResult<T> unitOfWorkWithoutResult) {
        LOGGER.info("Making transaction without result.");
        Session currentSession = sessionFactory.getCurrentSession();
        Transaction transaction = currentSession.beginTransaction();
        try {
            LOGGER.info("Executing unit of work.");
            unitOfWorkWithoutResult.execute(repository);
            transaction.commit();
            LOGGER.info("Transaction committed.");
        } catch (RepositoryException | IllegalStateException | RollbackException e) {
            transaction.rollback();
            LOGGER.error("Could not do transcation.", e);
            throw new ServiceException(e);
        } finally {
            currentSession.close();
        }
    }

}
