package no.svitts.core.service;

import no.svitts.core.movie.Movie;
import no.svitts.core.repository.Repository;

import java.util.List;

public class MovieService {

    private Repository<Movie> movieRepository;

    public MovieService(Repository<Movie> movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getAll() {
        return movieRepository.getAll();
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
        return movieRepository.insertSingle(movie);
    }

    public boolean updateMovie(Movie movie) {
        return movieRepository.updateSingle(movie);
    }

    public boolean deleteMovie(String id) {
        return movieRepository.deleteSingle(id);
    }

}
