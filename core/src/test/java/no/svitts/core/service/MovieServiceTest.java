package no.svitts.core.service;

import no.svitts.core.builder.MovieBuilder;
import no.svitts.core.id.Id;
import no.svitts.core.movie.Movie;
import no.svitts.core.repository.MovieRepository;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import static no.svitts.core.testkit.MovieTestKit.assertMovie;
import static org.mockito.Mockito.*;

public class MovieServiceTest {

    private MovieService movieService;
    private MovieBuilder movieBuilder;
    private MovieRepository movieRepositoryMock;

    @Before
    public void setUp() throws Exception {
        movieBuilder = new MovieBuilder();
        movieRepositoryMock = mock(MovieRepository.class);
        movieService = new MovieService(movieRepositoryMock);
    }

    @Test
    public void getMovie_ValidId_ShouldReturnExpectedMovie() throws Exception {
        Movie movie = movieBuilder.build();
        when(movieRepositoryMock.getById(movie.getId())).thenReturn(movie);
        Movie movieFromService = movieService.getMovie(movie.getId());
        assertMovie(movie, movieFromService);
        verify(movieRepositoryMock, times(1)).getById(movie.getId());
    }

    @Test(expected = BadRequestException.class)
    public void getMovie_IdIsNull_ShouldThrowBadRequestException() throws Exception {
        movieService.getMovie(null);
    }

    @Test(expected = BadRequestException.class)
    public void getMovie_IdIsEmpty_ShouldThrowBadRequestException() throws Exception {
        movieService.getMovie("");
    }

    @Test(expected = NotFoundException.class)
    public void getMovieIfExists_MovieDoesNotExist_ShouldThrowNotFoundException() throws Exception {
        when(movieRepositoryMock.getById(anyString())).thenReturn(null);
        movieService.getMovieIfItExists(Id.get());
    }
}
