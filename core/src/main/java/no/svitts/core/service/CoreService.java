package no.svitts.core.service;

import com.google.inject.Inject;
import no.svitts.core.exception.RepositoryException;
import no.svitts.core.exception.ServiceException;
import no.svitts.core.movie.Movie;
import no.svitts.core.repository.Repository;
import no.svitts.core.transaction.UnitOfWork;
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
            LOGGER.info("Flushing current session.");
            currentSession.flush();
            LOGGER.info("Committing transaction.");
            transaction.commit();
            return result;
        } catch (RepositoryException | IllegalStateException | RollbackException e) {
            transaction.rollback();
            LOGGER.error("Could not make transcation.", e);
            throw new ServiceException(e);
        } finally {
            if (currentSession.isOpen()) {
                currentSession.close();
            }
        }
    }

}
