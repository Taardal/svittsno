package no.svitts.core.repository;

import no.svitts.core.exception.RepositoryException;
import no.svitts.core.movie.Movie;
import no.svitts.core.search.Criteria;
import no.svitts.core.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MovieRepository implements Repository<Movie> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieRepository.class);

    @Override
    public Movie getOne(String id) {
        try {
            return HibernateUtil.getCurrentSession().get(Movie.class, id);
        } catch (HibernateException e) {
            LOGGER.error("Could not get movie with ID [{}]", id, e);
            throw new RepositoryException(e);
        }
    }

    @Override
    public List<Movie> getMany(Criteria criteria) {
        return null;
    }

    @Override
    public String save(Movie movie)  {
        try {
            return (String) HibernateUtil.getCurrentSession().save(movie);
        } catch (HibernateException e) {
            LOGGER.error("Could not save movie [{}]", movie.toString(), e);
            throw new RepositoryException(e);
        }
    }

    @Override
    public void delete(String  id)  {
        try {
            HibernateUtil.getCurrentSession().delete(getOne(id));
        } catch (HibernateException e) {
            LOGGER.error("Could not delete movie with ID [{}]", id, e);
            throw new RepositoryException(e);
        }
    }

}
