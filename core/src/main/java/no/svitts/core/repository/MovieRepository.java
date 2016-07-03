package no.svitts.core.repository;

import com.google.inject.Inject;
import no.svitts.core.criteria.Criteria;
import no.svitts.core.exception.RepositoryException;
import no.svitts.core.movie.Movie;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MovieRepository extends CoreRepository<Movie> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieRepository.class);

    @Inject
    public MovieRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Movie getSingle(String id) {
        LOGGER.info("Getting movie with ID [{}] from database.", id);
        try {
            return getCurrentSession().get(Movie.class, id);
        } catch (HibernateException e) {
            String errorMessage = "Could not get movie with ID [" + id + "] from database.";
            LOGGER.error(errorMessage, e);
            throw new RepositoryException(errorMessage, e);
        }
    }

    @Override
    public List<Movie> getMultiple(Criteria criteria) {
        return null;
    }

    @Override
    public String saveSingle(Movie movie) {
        LOGGER.info("Inserting movie [{}] into database.", movie.toString());
        try {
            getCurrentSession().save(movie);
            return movie.getId();
        } catch (HibernateException e) {
            String errorMessage = "Could not save movie to database.";
            LOGGER.error(errorMessage, e);
            throw new RepositoryException(errorMessage, e);
        }
    }

    @Override
    public void deleteSingle(String id) {
        getCurrentSession().delete(getSingle(id));
    }

}
