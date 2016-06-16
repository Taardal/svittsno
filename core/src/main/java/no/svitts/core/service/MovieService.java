package no.svitts.core.service;

import no.svitts.core.exception.RepositoryException;
import no.svitts.core.exception.ServiceException;
import no.svitts.core.exception.TransactionException;
import no.svitts.core.movie.Movie;
import no.svitts.core.repository.Repository;
import no.svitts.core.search.Criteria;
import no.svitts.core.transaction.TransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MovieService implements Service<Movie> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieService.class);

    private TransactionManager<Movie> transactionManager;

    public MovieService(TransactionManager<Movie> transactionManager, Repository<Movie> movieRepository) {
        this.transactionManager = transactionManager;
        this.transactionManager.setRepository(movieRepository);
    }

    @Override
    public Movie getOne(String id) {
        try {
            return transactionManager.executeTransaction(repository -> repository.getOne(id));
        } catch (RepositoryException | TransactionException e) {
            LOGGER.error("Could not executeTransaction executeTransaction", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Movie> getByCriteria(Criteria criteria) {
        try {
            return transactionManager.executeTransaction(repository -> repository.getMany(criteria));
        } catch (RepositoryException | TransactionException e) {
            LOGGER.error("Could not executeTransaction executeTransaction", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public String save(Movie movie) {
        try {
            return transactionManager.executeTransaction(repository -> repository.save(movie));
        } catch (RepositoryException | TransactionException e) {
            LOGGER.error("Could not executeTransaction executeTransaction", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(String id) {
        try {
            transactionManager.executeTransactionWithoutResult(repository -> repository.delete(id));
        } catch (RepositoryException | TransactionException e) {
            LOGGER.error("Could not executeTransaction executeTransaction", e);
            throw new ServiceException(e);
        }
    }

}
