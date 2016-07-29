package no.svitts.core.service;

import com.google.inject.Inject;
import no.svitts.core.criteria.Criteria;
import no.svitts.core.movie.Movie;
import no.svitts.core.repository.Repository;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MovieService extends CoreService<Movie> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieService.class);

    @Inject
    public MovieService(Repository<Movie> movieRepository, SessionFactory sessionFactory) {
        super(movieRepository, sessionFactory);
    }

    @Override
    public Movie getSingle(String id) {
        LOGGER.info("Getting single movie with id [{}]", id);
        return transaction(repository -> repository.getSingle(id));
    }

    @Override
    public List<Movie> getMultiple(Criteria criteria) {
        LOGGER.info("Getting multiple movies with criteria [{}]", criteria.toString());
        return transaction(repository -> repository.getMultiple(criteria));
    }

    @Override
    public String saveSingle(Movie movie) {
        LOGGER.info("Saving single movie [{}]", movie.toString());
        return transaction(repository -> repository.saveSingle(movie));
    }

    @Override
    public void updateSingle(Movie movie) {
        LOGGER.info("Updating single movie [{}]", movie.toString());
        transactionWithoutResult(repository -> repository.updateSingle(movie));
    }

    @Override
    public void deleteSingle(String id) {
        LOGGER.info("Deleting single movie with id [{}]", id);
        transactionWithoutResult(repository -> {
            Movie movie = repository.getSingle(id);
            repository.deleteSingle(movie);
        });
    }

}
