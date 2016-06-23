package no.svitts.core.repository;

import com.google.inject.Inject;
import no.svitts.core.exception.RepositoryException;
import no.svitts.core.movie.Movie;
import no.svitts.core.criteria.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MovieRepository implements Repository<Movie> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieRepository.class);

    private SessionFactory sessionFactory;

    @Inject
    public MovieRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Movie getOne(String id) {
        return getCurrentSession().get(Movie.class, id);
    }

    @Override
    public List<Movie> getMany(Criteria criteria) {
        return null;
    }

    @Override
    public String save(Movie movie)  {
        return (String) getCurrentSession().save(movie);
    }

    @Override
    public void delete(String  id)  {
        getCurrentSession().delete(getOne(id));
    }

    private Session getCurrentSession() {
        try {
            return sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            LOGGER.error("Could not get current session", e);
            throw new RepositoryException(e);
        }
    }

}
