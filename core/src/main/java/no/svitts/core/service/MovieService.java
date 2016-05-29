package no.svitts.core.service;

import no.svitts.core.exception.RepositoryException;
import no.svitts.core.movie.Movie;
import no.svitts.core.repository.Repository;
import no.svitts.core.search.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MovieService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieService.class);

    private Repository<Movie> movieRepository;

    public MovieService(Repository<Movie> movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie getMovie(String id) throws RepositoryException {
        return movieRepository.getSingle(id);
    }

    public List<Movie> getMovies(SearchCriteria searchCriteria) {
        return movieRepository.getMultiple(searchCriteria);
    }

    public String createMovie(Movie movie) {
        return movieRepository.insertSingle(movie);
    }

    public void updateMovie(Movie movie) {
        movieRepository.updateSingle(movie);
    }

    public void deleteMovie(String id) {
        movieRepository.deleteSingle(id);
    }

    private boolean isRequiredFieldsValid(String... strings) {
        for (String string : strings) {
            if (string == null || string.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private boolean isRequiredFieldsValid(SearchCriteria searchCriteria) {
        return searchCriteria.getKey() != null && searchCriteria.getValue() != null && !searchCriteria.getValue().isEmpty() && searchCriteria.getLimit() > 0;
    }

}
