package no.svitts.core.service;

import no.svitts.core.builder.MovieBuilder;
import no.svitts.core.exception.ServiceException;
import no.svitts.core.exception.TransactionException;
import no.svitts.core.movie.Movie;
import no.svitts.core.transaction.TransactionManager;
import no.svitts.core.transaction.UnitOfWork;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

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
        when(transactionManagerMock.transaction(anyUnitOfWork())).thenReturn(movie);

        Movie movieFromService = movieService.getSingle(movie.getId());

        assertEquals(movie, movieFromService);
        verify(transactionManagerMock, times(1)).transaction(anyUnitOfWork());
    }

    @Test(expected = ServiceException.class)
    public void getSingle_ThrowsTransactionException_ShouldCatchTransactionExceptionAndThrowServiceException() {
        when(transactionManagerMock.transaction(anyUnitOfWork())).thenThrow(new TransactionException());
        movieService.getSingle("id");
    }

    private UnitOfWork anyUnitOfWork() {
        return any(UnitOfWork.class);
    }
}
