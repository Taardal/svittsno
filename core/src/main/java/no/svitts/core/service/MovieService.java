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

public class MovieService extends CoreService<Movie> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieService.class);

    @Inject
    public MovieService(TransactionManager<Movie> transactionManager) {
        super(transactionManager);
    }

    @Override
    public Movie getSingle(String id) {
        try {
            return transaction(repository -> repository.getSingle(id));
        } catch (TransactionException e) {
            LOGGER.error("Could not do transaction to get movie by ID [{}]", id, e);
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Movie> getMultiple(Criteria criteria) {
        try {
            return transaction(repository -> repository.getMultiple(criteria));
        } catch (TransactionException e) {
            LOGGER.error("Could not do transaction to get movies by criteria [{}]", criteria, e);
            throw new ServiceException(e);
        }
    }

    @Override
    public String saveSingle(Movie movie) {
        try {
            return transaction(repository -> repository.saveSingle(movie));
        } catch (TransactionException e) {
            LOGGER.error("Could not do transaction to save single movie [{}]", movie.toString(), e);
            throw new ServiceException(e);
        }
    }

    @Override
    public void updateSingle(Movie movie) {
        try {
            transactionWithoutResult(repository -> repository.updateSingle(movie));
        } catch (TransactionException e) {
            LOGGER.error("Could not do transaction without result to update single movie [{}]", movie.toString(), e);
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteSingle(String id) {
        try {
            transactionWithoutResult(repository -> {
                Movie movie = repository.getSingle(id);
                repository.deleteSingle(movie);
            });
        } catch (TransactionException e) {
            LOGGER.error("Could not do transaction without result to delete movie by ID [{}]", id, e);
            throw new ServiceException(e);
        }
    }

}
