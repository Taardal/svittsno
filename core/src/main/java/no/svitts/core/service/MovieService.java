package no.svitts.core.service;

import com.google.inject.Inject;
import no.svitts.core.criteria.Criteria;
import no.svitts.core.exception.RepositoryException;
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
            LOGGER.error("Could not get movie by ID [{}]", id, e);
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Movie> getMultiple(Criteria criteria) {
        try {
            return transaction(repository -> repository.getMultiple(criteria));
        } catch (TransactionException e) {
            LOGGER.error("Could not get movies by criteria [{}]", criteria, e);
            throw new ServiceException(e);
        }
    }

    @Override
    public String saveSingle(Movie movie) {
        try {
            return transaction(repository -> repository.saveSingle(movie));
        } catch (TransactionException | RepositoryException e) {
            LOGGER.error("Could not saveSingle movie [{}]", movie.toString(), e);
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteSingle(String id) {
        try {
            transactionWithoutResult(repository -> repository.deleteSingle(id));
        } catch (TransactionException e) {
            LOGGER.error("Could not delete movie by ID [{}]", id, e);
            throw new ServiceException(e);
        }
    }

}
