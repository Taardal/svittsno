package no.svitts.core.service;

import no.svitts.core.movie.Movie;
import no.svitts.core.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MovieService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieService.class);

    private Repository<Movie> movieRepository;

    public MovieService(Repository<Movie> movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie getMovie(String id) {
        return movieRepository.getSingle(id);
    }

    public List<Movie> getMovies(String name, String genre, int limit, int offset) {
        return movieRepository.getMultiple(null);
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

}
