package no.svitts.core.service;

import no.svitts.core.exception.RepositoryException;
import no.svitts.core.exception.ServiceException;
import no.svitts.core.movie.Movie;
import no.svitts.core.repository.Repository;
import no.svitts.core.search.Criteria;
import no.svitts.core.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MovieService implements Service<Movie> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieService.class);

    private Repository<Movie> movieRepository;

    public MovieService(Repository<Movie> movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Movie getOne(String id) {
        try {
            return HibernateUtil.transaction(movieRepository, repository -> repository.getOne(id));
        } catch (RepositoryException | HibernateException e) {
            LOGGER.error("Could not execute transaction", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Movie> getByCriteria(Criteria criteria) {
        try {
            return HibernateUtil.transaction(movieRepository, repository -> repository.getMany(criteria));
        } catch (RepositoryException | HibernateException e) {
            LOGGER.error("Could not execute transaction", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public String save(Movie movie) {
        try {
            return HibernateUtil.transaction(movieRepository, repository -> repository.save(movie));
        } catch (RepositoryException | HibernateException e) {
            LOGGER.error("Could not execute transaction", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(String id) {
        try {
            HibernateUtil.transaction(movieRepository, repository -> {
                repository.delete(id);
                return null;
            });
        } catch (RepositoryException | HibernateException e) {
            LOGGER.error("Could not execute transaction", e);
            throw new ServiceException(e);
        }
    }

}
