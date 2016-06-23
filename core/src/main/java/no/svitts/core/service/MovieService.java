package no.svitts.core.service;

import com.google.inject.Inject;
import no.svitts.core.criteria.Criteria;
import no.svitts.core.exception.ServiceException;
import no.svitts.core.exception.TransactionException;
import no.svitts.core.movie.Movie;
import no.svitts.core.transaction.TransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MovieService implements Service<Movie> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieService.class);

    private TransactionManager<Movie> transactionManager;

    @Inject
    public MovieService(TransactionManager<Movie> transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public Movie getById(String id) {
        try {
            return transactionManager.transaction(repository -> repository.getOne(id));
        } catch (TransactionException e) {
            LOGGER.error("Could not get movie by ID [{}]", id, e);
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Movie> getByCriteria(Criteria criteria) {
        try {
            return transactionManager.transaction(repository -> repository.getMany(criteria));
        } catch (TransactionException e) {
            LOGGER.error("Could not get movies by criteria [{}]", criteria, e);
            throw new ServiceException(e);
        }
    }

    @Override
    public String save(Movie movie) {
        try {
            return transactionManager.transaction(repository -> repository.save(movie));
        } catch (TransactionException e) {
            LOGGER.error("Could not save movie [{}]", movie.toString(), e);
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(String id) {
        try {
            transactionManager.transactionWithoutResult(repository -> repository.delete(id));
        } catch (TransactionException e) {
            LOGGER.error("Could not delete movie by ID [{}]", id, e);
            throw new ServiceException(e);
        }
    }

}
