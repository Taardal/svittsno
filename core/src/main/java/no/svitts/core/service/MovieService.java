package no.svitts.core.service;

import no.svitts.core.movie.Movie;
import no.svitts.core.repository.Repository;

import java.util.List;

public class MovieService {

    private Repository<Movie> movieRepository;
    private PersonService personService;

    public MovieService(Repository<Movie> movieRepository, PersonService personService) {
        this.movieRepository = movieRepository;
        this.personService = personService;
    }

    public List<Movie> getAll() {
        return enrichMovies(movieRepository.getAll());
    }

    public Movie getMovie(String id) {
        return enrichMovie(movieRepository.getById(id));
    }

    public boolean createMovie(Movie movie) {
        boolean movieCreated = movieRepository.insertSingle(movie);
        boolean personsCreated = personService.createPersons(movie.getPersons());
        return false;
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

}
