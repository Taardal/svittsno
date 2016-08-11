package no.svitts.core.service;

import com.google.inject.Inject;
import no.svitts.core.movie.Movie;
import no.svitts.core.repository.Repository;
import no.svitts.core.search.Search;
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
    public List<Movie> getMultiple(Search search) {
        LOGGER.info("Getting multiple movies with search [{}]", search.toString());
        return transaction(repository -> repository.getMultiple(search));
    }

    @Override
    public String saveSingle(Movie movie) {
        LOGGER.info("Saving single movie [{}]", movie.toString());
        return transaction(repository -> repository.saveSingle(movie));
    }

    @Override
    public void updateSingle(Movie movie) {
        LOGGER.info("Updating single movie [{}]", movie.toString());
        transaction(repository -> repository.updateSingle(movie));
    }

    @Override
    public void deleteSingle(String id) {
        LOGGER.info("Deleting single movie with id [{}]", id);
        transaction(repository -> repository.deleteSingle(repository.getSingle(id)));
    }

}
