package no.svitts.core.service;

import no.svitts.core.search.SearchCriteria;
import no.svitts.core.exception.AlreadyExitstsException;
import no.svitts.core.exception.NoChangeException;
import no.svitts.core.exception.UnprocessableEntityException;
import no.svitts.core.movie.Movie;
import no.svitts.core.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.List;

public class MovieService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieService.class);

    private Repository<Movie> movieRepository;

    public MovieService(Repository<Movie> movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie getMovie(String id)  {
        if (isRequiredFieldsValid(id)) {
            return getMovieIfItExists(id);
         } else {
            String errorMessage = "Could not validate required field(s)";
            LOGGER.warn(errorMessage);
            throw new BadRequestException(errorMessage);
        }
    }

    Movie getMovieIfItExists(String id) {
        Movie movie = movieRepository.getById(id);
        if (movie != null) {
            return movie;
        } else {
            String errorMessage = "Could not find requested movie in the database.";
            LOGGER.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }
    }

    public List<Movie> getMovies(SearchCriteria searchCriteria)  {
        if (isRequiredFieldsValid(searchCriteria)) {
            return movieRepository.getMultiple(searchCriteria);
        } else {
            String errorMessage = "Could not validate required field(s)";
            LOGGER.warn(errorMessage);
            throw new UnprocessableEntityException(errorMessage);
        }
    }

    public String createMovie(Movie movie) {
        if (isRequiredFieldsValid(movie.getId(), movie.getName())) {
            if (movieRepository.getById(movie.getId()) == null) {
                return movieRepository.insert(movie);
            } else {
                String errorMessage = "Could not insert movie because it already exists.";
                LOGGER.warn(errorMessage);
                throw new AlreadyExitstsException(errorMessage);
            }
        } else {
            String errorMessage = "Could not validate required field(s)";
            LOGGER.warn(errorMessage);
            throw new UnprocessableEntityException(errorMessage);
        }
    }

    public void updateMovie(Movie movie) {
        if (isRequiredFieldsValid(movie.getId(), movie.getName())) {
            movieRepository.update(movie);
        } else {
            String errorMessage = "Could not validate required field(s)";
            LOGGER.warn(errorMessage);
            throw new UnprocessableEntityException(errorMessage);
        }
    }

    public void deleteMovie(String id) throws NoChangeException  {
        if (isRequiredFieldsValid(id)) {
            movieRepository.delete(id);
        } else {
            String errorMessage = "Could not validate required field(s)";
            LOGGER.warn(errorMessage);
            throw new UnprocessableEntityException(errorMessage);
        }
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
