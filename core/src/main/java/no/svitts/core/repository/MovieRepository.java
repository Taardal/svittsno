package no.svitts.core.repository;

import com.google.inject.Inject;
import no.svitts.core.exception.RepositoryException;
import no.svitts.core.genre.Genre;
import no.svitts.core.movie.Movie;
import no.svitts.core.search.MovieSearch;
import no.svitts.core.search.MovieSearchType;
import no.svitts.core.search.Search;
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
    public List<Movie> getMultiple(Search search) {
        MovieSearch movieSearch = (MovieSearch) search;
        LOGGER.info("Getting multiple movies from database by search [{}].", movieSearch.toString());
        try {
            return getCurrentSession().createQuery(getSearchCriteriaQuery(movieSearch)).setMaxResults(movieSearch.getLimit()).setFirstResult(movieSearch.getOffset()).getResultList();
        } catch (HibernateException | IllegalStateException e) {
            LOGGER.error("Could not get multiple movies from database by search [{}]", movieSearch.toString());
            throw new RepositoryException(e);
        }
    }

    @Override
    public String saveSingle(Movie movie) {
        if (movie.getId() == null) {
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
    public Void updateSingle(Movie movie) {
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
    public Void deleteSingle(Movie movie) {
        LOGGER.info("Deleting movie [{}] from database.", movie.toString());
        try {
            getCurrentSession().delete(movie);
            return null;
        } catch (HibernateException e) {
            LOGGER.error("Could not delete movie [{}] from database.", movie.toString(), e);
            throw new RepositoryException(e);
        }
    }

    private CriteriaQuery<Movie> getSearchCriteriaQuery(MovieSearch movieSearch) {
        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Movie> movieCriteriaQuery = criteriaBuilder.createQuery(Movie.class);
        Root<Movie> movieRoot = movieCriteriaQuery.from(Movie.class);
        movieCriteriaQuery.select(movieRoot);
        if (movieSearch.getMovieSearchType() == MovieSearchType.TITLE) {
            return movieCriteriaQuery.where(criteriaBuilder.like(movieRoot.get("title"), "%" + movieSearch.getQuery() + "%"));
        } else if (movieSearch.getMovieSearchType() == MovieSearchType.GENRE) {
            Genre genre = Genre.valueOf(movieSearch.getQuery());
            return movieCriteriaQuery.where(criteriaBuilder.isMember(genre, movieRoot.get("genres")));
        } else {
            return movieCriteriaQuery;
        }
    }

}
