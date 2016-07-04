package no.svitts.core.service;

import no.svitts.core.builder.MovieBuilder;
import no.svitts.core.criteria.Criteria;
import no.svitts.core.exception.ServiceException;
import no.svitts.core.exception.TransactionException;
import no.svitts.core.movie.Movie;
import no.svitts.core.transaction.TransactionManager;
import no.svitts.core.transaction.UnitOfWork;
import no.svitts.core.transaction.UnitOfWorkWithoutResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@SuppressWarnings("unchecked")
public class MovieServiceTest {

    private MovieService movieService;
    private MovieBuilder movieBuilder;

    @Mock
    private TransactionManager<Movie> transactionManagerMock;

    @Before
    public void setUp() {
        initMocks(this);
        movieService = new MovieService(transactionManagerMock);
        movieBuilder = new MovieBuilder();
    }

    @Test
    public void getSingle_ValidId_ShouldMakeTransactionAndReturnMovie() {
        Movie movie = movieBuilder.build();
        when(transactionManagerMock.transaction(any(UnitOfWork.class))).thenReturn(movie);

        Movie movieFromService = movieService.getSingle(movie.getId());

        assertEquals(movie, movieFromService);
        verify(transactionManagerMock, times(1)).transaction(any(UnitOfWork.class));
    }

    @Test(expected = ServiceException.class)
    public void getSingle_ThrowsTransactionException_ShouldCatchTransactionExceptionAndThrowServiceException() {
        when(transactionManagerMock.transaction(any(UnitOfWork.class))).thenThrow(new TransactionException());
        movieService.getSingle("id");
    }

    @Test
    public void getMultiple_ValidCriteria_ShouldMakeTransactionAndReturnListOfMovies() {
        List<Movie> movies = new ArrayList<>();
        movies.add(movieBuilder.name("movie1").build());
        movies.add(movieBuilder.name("movie2").build());
        when(transactionManagerMock.transaction(any(UnitOfWork.class))).thenReturn(movies);

        List<Movie> moviesFromService = movieService.getMultiple(new Criteria());

        assertArrayEquals(movies.toArray(), moviesFromService.toArray());
        verify(transactionManagerMock, times(1)).transaction(any(UnitOfWork.class));
    }

    @Test(expected = ServiceException.class)
    public void getMultiple_ThrowsTransactionException_ShouldCatchTransactionExceptionAndThrowServiceException() {
        when(transactionManagerMock.transaction(any(UnitOfWork.class))).thenThrow(new TransactionException());
        movieService.getMultiple(new Criteria());
    }

    @Test
    public void saveSingle_ValidMovie_ShouldMakeTransactionAndReturnIdOfSavedMovie() {
        Movie movie = movieBuilder.build();
        when(transactionManagerMock.transaction(any(UnitOfWork.class))).thenReturn(movie.getId());

        String savedMovieId = movieService.saveSingle(movie);

        assertEquals(movie.getId(), savedMovieId);
        verify(transactionManagerMock, times(1)).transaction(any(UnitOfWork.class));
    }

    @Test(expected = ServiceException.class)
    public void saveSingle_ThrowsTransactionException_ShouldCatchTransactionExceptionAndThrowServiceException() {
        when(transactionManagerMock.transaction(any(UnitOfWork.class))).thenThrow(new TransactionException());
        movieService.saveSingle(movieBuilder.build());
    }

    @Test
    public void deleteSingle_ValidId_ShouldMakeTransaction() {
        Movie movie = movieBuilder.build();
        doNothing().when(transactionManagerMock).transactionWithoutResult(any(UnitOfWorkWithoutResult.class));

        movieService.deleteSingle(movie.getId());

        verify(transactionManagerMock, times(1)).transactionWithoutResult(any(UnitOfWorkWithoutResult.class));
    }

    @Test(expected = ServiceException.class)
    public void deleteSingle_ThrowsTransactionException_ShouldCatchTransactionExceptionAndThrowServiceException() {
        doThrow(new TransactionException()).when(transactionManagerMock).transactionWithoutResult(any(UnitOfWorkWithoutResult.class));
        movieService.deleteSingle("id");
    }

}
