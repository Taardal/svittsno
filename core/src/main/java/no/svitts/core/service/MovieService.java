package no.svitts.core.service;

import no.svitts.core.movie.Movie;
import no.svitts.core.movie.UnknownMovie;
import no.svitts.core.repository.Repository;

import java.util.List;

public class MovieService {

    private Repository<Movie> movieRepository;

    public MovieService(Repository<Movie> movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getAll() {
        return enrichMovies(movieRepository.getAll());
    }

    public Movie getMovieById(String id) {
        return movieRepository.getById(id);
    }

    public Movie getMovieByName(String name) {
        return movieRepository.getByAttributes(name);
    }

    public boolean createMovies(List<Movie> movies) {
        boolean allCreated = true;
        for (Movie movie : movies) {
            boolean created = createMovie(movie);
            if (!created) {
                allCreated = false;
            }
        }
        return allCreated;
    }

    public boolean createMovie(Movie movie) {
        return alreadyExists(movie) || movieRepository.insertSingle(movie);
    }

    public boolean updateMovie(Movie movie) {
        return movieRepository.updateSingle(movie);
    }

    public boolean deleteMovie(String id) {
        return movieRepository.deleteSingle(id);
    }

    private List<Movie> enrichMovies(List<Movie> movies) {
        movies.forEach(this::enrichMovie);
        return movies;
    }

    private Movie enrichMovie(Movie movie) {
        return movie;
    }

    private boolean alreadyExists(Movie movie) {
        Movie queriedMovie = getMovieById(movie.getId());
        if (!isUnknown(queriedMovie)) {
            return movie.getId().equals(queriedMovie.getId());
        } else {
            queriedMovie = getMovieByName(movie.getName());
            return !isUnknown(queriedMovie) && movie.getName().equals(queriedMovie.getName());
        }
    }

    private boolean isUnknown(Movie movie) {
        return movie instanceof UnknownMovie;
    }

}
