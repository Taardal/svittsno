package no.svitts.core.repository;

import no.svitts.core.builder.MovieBuilder;
import no.svitts.core.exception.RepositoryException;
import no.svitts.core.movie.Movie;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static no.svitts.core.testkit.MovieTestKit.assertMovie;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MovieRepositoryTest {

    private SessionFactory sessionFactoryMock;
    private Session sessionMock;
    private MovieRepository movieRepository;
    private MovieBuilder movieBuilder;

    @Before
    public void setUp() throws SQLException {
        sessionFactoryMock = mock(SessionFactory.class);
        sessionMock = mock(Session.class);
        when(sessionFactoryMock.getCurrentSession()).thenReturn(sessionMock);

        movieRepository = new MovieRepository(sessionFactoryMock);
        movieBuilder = new MovieBuilder();
    }

    @Test
    public void getOne_ValidId_ShouldReturnMovie() throws Exception {
        Movie movie = movieBuilder.build();
        when(sessionMock.get(Movie.class, movie.getId())).thenReturn(movie);

        Movie movieFromRepository = movieRepository.getSingle(movie.getId());

        assertMovie(movie, movieFromRepository);
        verify(sessionFactoryMock, times(1)).getCurrentSession();
        verify(sessionMock, times(1)).get(Movie.class, movie.getId());
    }

    @Test(expected = RepositoryException.class)
    public void getOne_ThrowsHibernateException_ShouldCatchHibernateExceptionAndThrowRepositoryException() throws Exception {
        Movie movie = movieBuilder.build();
        when(sessionMock.get(Movie.class, movie.getId())).thenThrow(new HibernateException("Expected"));
        movieRepository.getSingle(movie.getId());
    }

    @Test
    public void saveOne_ValidMovie_ShouldReturnIdOfSavedMovie() throws Exception {
        Movie movie = movieBuilder.build();
        when(sessionMock.save(movie)).thenReturn("");

        String savedMovieId = movieRepository.saveSingle(movie);

        assertEquals(movie.getId(), savedMovieId);
        verify(sessionFactoryMock, times(1)).getCurrentSession();
        verify(sessionMock, times(1)).save(movie);
    }

    @Test(expected = RepositoryException.class)
    public void saveOne_ThrowsHibernateException_ShouldCatchHibernateExceptionAndThrowRepositoryException() throws Exception {
        Movie movie = movieBuilder.build();
        when(sessionMock.save(movie)).thenThrow(new HibernateException("Expected"));
        movieRepository.saveSingle(movie);
    }


}
