package no.svitts.core.repository;

import com.google.inject.Inject;
import no.svitts.core.criteria.Criteria;
import no.svitts.core.criteria.CriteriaKey;
import no.svitts.core.exception.RepositoryException;
import no.svitts.core.movie.Movie;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
            LOGGER.error("Could not get movie with ID [{}] from database.", id, e);
            throw new RepositoryException(e);
        }
    }

    @Override
    public List<Movie> getMultiple(Criteria criteria) {
        LOGGER.info("Getting multiple movies from database by criteria [{}].", criteria.toString());
        try {
            CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();
            CriteriaQuery<Movie> movieCriteriaQuery = getMovieCriteriaQuery(criteria, criteriaBuilder);
            return getCurrentSession().createQuery(movieCriteriaQuery).setMaxResults(criteria.getLimit()).setFirstResult(criteria.getOffset()).getResultList();
        } catch (HibernateException | IllegalStateException e) {
            LOGGER.error("Could not get multiple movies from database by criteria [{}]", criteria.toString());
            throw new RepositoryException(e);
        }
    }

    @Override
    public String saveSingle(Movie movie) {
        LOGGER.info("Inserting movie [{}] into database.", movie.toString());
        try {
            return (String) getCurrentSession().save(movie);
        } catch (HibernateException e) {
            LOGGER.error("Could not save movie [{}] to database.", movie.toString(), e);
            throw new RepositoryException(e);
        }
    }

    @Override
    public void deleteSingle(Movie movie) {
        LOGGER.info("Deleting movie [{}] from database.", movie.toString());
        try {
            getCurrentSession().delete(movie);
        } catch (HibernateException e) {
            LOGGER.error("Could not delete movie [{}] from database.", movie.toString(), e);
            throw new RepositoryException(e);
        }
    }

    private CriteriaQuery<Movie> getMovieCriteriaQuery(Criteria criteria, CriteriaBuilder criteriaBuilder) {
        CriteriaQuery<Movie> movieCriteriaQuery = criteriaBuilder.createQuery(Movie.class);
        Root<Movie> movieRoot = movieCriteriaQuery.from(Movie.class);
        movieCriteriaQuery.select(movieRoot);
        if (criteria.get(CriteriaKey.NAME) != null) {
            movieCriteriaQuery.where(criteriaBuilder.like(movieRoot.get("name"), criteria.get(CriteriaKey.NAME)));
        }
        if (criteria.get(CriteriaKey.GENRE) != null) {
            movieCriteriaQuery.having(criteriaBuilder.like(movieRoot.get("genre"), criteria.get(CriteriaKey.GENRE)));
        }
        return movieCriteriaQuery;
    }

}
