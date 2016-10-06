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
    public Movie get(String id) {
        LOGGER.info("Getting movie with ID [{}]", id);
        return transaction(repository -> repository.get(id));
    }

    @Override
    public List<Movie> search(Search search) {
        LOGGER.info("Searching movies with search [{}]", search.toString());
        return transaction(repository -> repository.search(search));
    }

    @Override
    public String save(Movie movie) {
        LOGGER.info("Saving movie [{}]", movie.toString());
        return transaction(repository -> repository.save(movie));
    }

    @Override
    public void update(Movie movie) {
        LOGGER.info("Updating movie [{}]", movie.toString());
        transaction(repository -> repository.update(movie));
    }

    @Override
    public void delete(String id) {
        LOGGER.info("Deleting movie with ID [{}]", id);
        transaction(repository -> repository.delete(repository.get(id)));
    }

}
