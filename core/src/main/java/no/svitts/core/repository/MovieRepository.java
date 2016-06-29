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
    public Movie getOne(String id) {
        LOGGER.info("Getting movie with ID [{}] from database.", id);
        try {
            return getCurrentSession().get(Movie.class, id);
        } catch (Throwable e) {
            LOGGER.error("Could not get movie", e);
            throw new RepositoryException(e);
        }
    }

    @Override
    public List<Movie> getMany(Criteria criteria) {
        return null;
    }

    @Override
    public String save(Movie movie) {
        LOGGER.info("Inserting movie [{}] into database.", movie.toString());
        try {
            getCurrentSession().save(movie);
            return movie.getId();
        } catch (HibernateException e) {
            LOGGER.error("Could not save movie", e);
            throw new RepositoryException(e);
        }
    }

    @Override
    public void delete(String id) {
        getCurrentSession().delete(getOne(id));
    }

}
