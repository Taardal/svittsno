package no.svitts.core.service;

import no.svitts.core.dao.DaoManager;
import no.svitts.core.movie.Movie;
import no.svitts.core.repository.Repository;
import no.svitts.core.search.Criteria;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static no.svitts.core.util.HibernateUtil.*;

public class MovieService implements Service<Movie> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieService.class);

    private Repository<Movie> movieRepository;

    public MovieService(Repository<Movie> movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Movie getById(String id) {
        try {
            beginTransaction();
            Movie movie = movieRepository.getSingle(id);
            commitTransaction();
            return movie;
        } catch (HibernateException e) {
            rollbackTransaction();
            throw new HibernateException("Could not get movie by ID [" + id + "]", e);
        } finally {
            closeSession();
        }
    }

    @Override
    public List<Movie> getAll(Criteria criteria) {
        DaoManager.transaction(repository -> repository.getMultiple(null), movieRepository);
        DaoManager.transaction(movieRepository)
        return null;
    }

    @Override
    public String create(Movie movie) {
        return null;
    }

    @Override
    public void update(Movie movie) {

    }

    @Override
    public void delete(String id) {

    }
}
