package no.svitts.core.repository;

import com.google.inject.Inject;
import no.svitts.core.exception.RepositoryException;
import no.svitts.core.genre.Genre;
import no.svitts.core.movie.Movie;
import no.svitts.core.search.Search;
import no.svitts.core.search.SearchKey;
import no.svitts.core.util.Id;
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
    public Movie get(String id) {
        LOGGER.info("Getting movie with ID [{}] from database.", id);
        try {
            return getCurrentSession().get(Movie.class, id);
        } catch (HibernateException e) {
            LOGGER.error("Could not get movie with ID [{}] from database.", id, e);
            throw new RepositoryException(e);
        }
    }

    @Override
    public List<Movie> search(Search search) {
        LOGGER.info("Getting multiple movies from database by search [{}].", search.toString());
        try {
            List<Movie> movies = getCurrentSession()
                    .createQuery(getSearchCriteriaQuery(search))
                    .setMaxResults(search.getLimit())
                    .setFirstResult(search.getOffset())
                    .getResultList();
            LOGGER.info("Search found [{}] movies -> [{}]", movies.size(), movies);
            return movies;
        } catch (HibernateException | IllegalStateException e) {
            LOGGER.error("Could not get multiple movies from database by search [{}]", search.toString());
            throw new RepositoryException(e);
        }
    }

    @Override
    public String save(Movie movie) {
        if (movie.getId() == null || movie.getId().equals("")) {
            movie.setId(Id.get());
        }
        LOGGER.info("Inserting movie [{}] into database.", movie.toString());
        try {
            return (String) getCurrentSession().save(movie);
        } catch (HibernateException e) {
            LOGGER.error("Could not save movie [{}] to database.", movie.toString(), e);
            throw new RepositoryException(e);
        }
    }

    @Override
    public Void update(Movie movie) {
        LOGGER.info("Updating movie [{}] in database.", movie.toString());
        try {
            getCurrentSession().update(movie);
            return null;
        } catch (HibernateException e) {
            LOGGER.error("Could not update movie [{}] in database.", movie.toString(), e);
            throw new RepositoryException(e);
        }
    }

    @Override
    public Void delete(Movie movie) {
        LOGGER.info("Deleting movie [{}] from database.", movie.toString());
        try {
            getCurrentSession().delete(movie);
            return null;
        } catch (HibernateException e) {
            LOGGER.error("Could not delete movie [{}] from database.", movie.toString(), e);
            throw new RepositoryException(e);
        }
    }

    private CriteriaQuery<Movie> getSearchCriteriaQuery(Search search) {
        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Movie> movieCriteriaQuery = criteriaBuilder.createQuery(Movie.class);
        Root<Movie> movieRoot = movieCriteriaQuery.from(Movie.class);
        movieCriteriaQuery.select(movieRoot);
        if (search.getSearchKey() == SearchKey.TITLE) {
            return movieCriteriaQuery.where(criteriaBuilder.like(criteriaBuilder.upper(movieRoot.get("title")), "%" + search.getQuery().toUpperCase() + "%"));
        } else if (search.getSearchKey() == SearchKey.GENRE) {
            Genre genre = Genre.valueOf(search.getQuery());
            return movieCriteriaQuery.where(criteriaBuilder.isMember(genre, movieRoot.get("genres")));
        } else if (search.getSearchKey() == SearchKey.VIDEO_FILE_PATH) {
            return movieCriteriaQuery.where(criteriaBuilder.equal(criteriaBuilder.upper(movieRoot.get("videoFile").get("path")), search.getQuery().toUpperCase()));
        } else {
            return movieCriteriaQuery;
        }
    }

}
