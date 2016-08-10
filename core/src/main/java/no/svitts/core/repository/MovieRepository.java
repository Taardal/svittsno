package no.svitts.core.repository;

import com.google.inject.Inject;
import no.svitts.core.criteria.Criteria;
import no.svitts.core.criteria.CriteriaKey;
import no.svitts.core.exception.RepositoryException;
import no.svitts.core.genre.Genre;
import no.svitts.core.movie.Movie;
import no.svitts.core.util.Id;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
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
            CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
            CriteriaQuery<Movie> movieCriteriaQuery = getMultipleMoviesCriteriaQuery(criteria, criteriaBuilder);
            return getCurrentSession().createQuery(movieCriteriaQuery).setMaxResults(criteria.getLimit()).setFirstResult(criteria.getOffset()).getResultList();
        } catch (HibernateException | IllegalStateException e) {
            LOGGER.error("Could not get multiple movies from database by criteria [{}]", criteria.toString());
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
    public void updateSingle(Movie movie) {
        LOGGER.info("Updating movie [{}] in database.", movie.toString());
        try {
            getCurrentSession().update(movie);
        } catch (HibernateException e) {
            LOGGER.error("Could not update movie [{}] in database.", movie.toString(), e);
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

    public List<Movie> search(String query, int limit, int offset) {
        CriteriaQuery<Movie> movieCriteriaQuery = getSelectMoviesBySimilarTitleCriteriaQuery(query);
        return getCurrentSession().createQuery(movieCriteriaQuery).setMaxResults(limit).setFirstResult(offset).getResultList();
    }

    private CriteriaQuery<Movie> getSelectMoviesBySimilarTitleCriteriaQuery(String query) {
        CriteriaQuery<Movie> movieCriteriaQuery = getCriteriaBuilder().createQuery(Movie.class);
        Root<Movie> movieRoot = movieCriteriaQuery.from(Movie.class);
        return movieCriteriaQuery.select(movieRoot).where(getCriteriaBuilder().like(movieRoot.get("title"), "%" + query + "%"));
    }

    private CriteriaQuery<Movie> getSelectMoviesByGenreCriteriaQuery(String genre) {
        CriteriaQuery<Movie> movieCriteriaQuery = getCriteriaBuilder().createQuery(Movie.class);
        Root<Movie> movieRoot = movieCriteriaQuery.from(Movie.class);
        return movieCriteriaQuery.select(movieRoot).where(getCriteriaBuilder().isMember(Genre.valueOf(genre.toUpperCase()), movieRoot.get("genres")));
    }

    private CriteriaQuery<Movie> getMultipleMoviesCriteriaQuery(Criteria criteria, CriteriaBuilder criteriaBuilder) {
        CriteriaQuery<Movie> movieCriteriaQuery = getCriteriaBuilder().createQuery(Movie.class);
        Root<Movie> movieRoot = movieCriteriaQuery.from(Movie.class);
        movieCriteriaQuery.select(movieRoot);

        String name = criteria.get(CriteriaKey.NAME);
        String genre = criteria.get(CriteriaKey.GENRE);
        Predicate titleLikeTitle = null;
        Predicate genreIsMemberOfGenres = null;

        if (isNotNullOrEmpty(name) && isNotNullOrEmpty(genre)) {
            titleLikeTitle = getCriteriaBuilder().like(movieRoot.get("title"), "%" + name + "%");
            genreIsMemberOfGenres = getCriteriaBuilder().isMember(Genre.valueOf(genre.toUpperCase()), movieRoot.get("genres"));
            movieCriteriaQuery.where(getCriteriaBuilder().and(titleLikeTitle, genreIsMemberOfGenres));
        } else if (isNotNullOrEmpty(name)) {
            titleLikeTitle = getCriteriaBuilder().like(movieRoot.get("title"), "%" + name + "%");
            movieCriteriaQuery.where(titleLikeTitle);
        } else if (isNotNullOrEmpty(genre)) {
            genreIsMemberOfGenres = getCriteriaBuilder().isMember(Genre.valueOf(genre.toUpperCase()), movieRoot.get("genres"));
            movieCriteriaQuery.where(genreIsMemberOfGenres);
        }

        return movieCriteriaQuery;
    }

    private boolean isNotNullOrEmpty(String criteria) {
        return criteria != null && !criteria.equals("");
    }

}
