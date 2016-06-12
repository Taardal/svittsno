package no.svitts.core.repository;

import no.svitts.core.builder.MovieBuilder;
import no.svitts.core.datasource.CoreDataSource;
import no.svitts.core.datasource.DataSource;
import no.svitts.core.exception.RepositoryException;
import no.svitts.core.util.Id;
import no.svitts.core.genre.Genre;
import no.svitts.core.movie.Movie;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static no.svitts.core.repository.MovieRepository.*;
import static no.svitts.core.testkit.MovieTestKit.assertMovie;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class MovieRepositoryTest {

    private MovieRepository movieRepository;
    private MovieBuilder movieBuilder;
    private DataSource dataSourceMock;
    private Connection connectionMock;
    private PreparedStatement preparedStatementMock;
    private ResultSet resultSetMock;

    @Before
    public void setUp() throws SQLException {
        movieBuilder = new MovieBuilder();

        dataSourceMock = mock(CoreDataSource.class);
        connectionMock = mock(Connection.class);
        preparedStatementMock = mock(PreparedStatement.class);
        resultSetMock = mock(ResultSet.class);

        when(dataSourceMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1);
        when(preparedStatementMock.executeBatch()).thenReturn(new int[]{1, 1});

        movieRepository = new MovieRepository(dataSourceMock);
    }

    @Test
    public void getSingle_ValidId_ShouldTriggerExpectedSQLComponentsAndReturnExpectedMovie() throws SQLException {
        Movie movie = movieBuilder.build();
        setupMockResultSetForMovie(movie);

        Movie movieFromRepository = movieRepository.getSingle(movie.getId());

        assertMovie(movie, movieFromRepository);
        verify(dataSourceMock, times(1)).getConnection();
        verify(connectionMock, times(1)).prepareStatement(anyString());
        verify(preparedStatementMock, times(1)).executeQuery();
        verify(resultSetMock, times(2)).next();
    }

    @Test(expected = RepositoryException.class)
    public void getSingle_ThrowsSQLException_ShouldCatchSQLExceptionAndThrowRepositoryException() throws SQLException {
        when(dataSourceMock.getConnection()).thenThrow(new SQLException());
        movieRepository.getSingle(Id.get());
    }

    @Test(expected = RepositoryException.class)
    public void getMovie_ThrowsSQLException_ShouldCatchSQLExceptionAndThrowRepositoryException() throws SQLException {
        when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException());
        movieRepository.getMovie(Id.get(), connectionMock);
    }

    @Test
    public void getMultiple_NameAndGenreCriteria_ShouldReturnExpectedMovies() {


    }

    @Test
    public void insertSingle_ValidMovie_ShouldTriggerExpectedSQLComponentsAndReturnIdOfInsertedMovie() throws SQLException {
        Movie movie = movieBuilder.build();

        String insertedMovieId = movieRepository.insertSingle(movie);

        assertEquals(movie.getId(), insertedMovieId);
        verify(dataSourceMock, times(1)).getConnection();
        verify(connectionMock, times(2)).prepareStatement(anyString());
        verify(preparedStatementMock, times(1)).executeUpdate();
        verify(preparedStatementMock, times(1)).executeBatch();
    }

    @Test
    public void insertSingle_NoGenres_ShouldNotInsertMovieGenreRelations() throws SQLException {
        Movie movie = movieBuilder.genres(null).build();

        String insertedMovieId = movieRepository.insertSingle(movie);

        assertEquals(movie.getId(), insertedMovieId);
        verify(dataSourceMock, times(1)).getConnection();
        verify(connectionMock, times(1)).prepareStatement(anyString());
        verify(preparedStatementMock, times(1)).executeUpdate();
    }

    @Test(expected = RepositoryException.class)
    public void insertSingle_ThrowsSQLException_ShouldCatchSQLExceptionAndThrowRepositoryException() throws SQLException {
        when(dataSourceMock.getConnection()).thenThrow(new SQLException());
        movieRepository.insertSingle(movieBuilder.build());
    }

    @Test(expected = RepositoryException.class)
    public void insertMovie_ThrowsSQLException_ShouldCatchSQLExceptionAndThrowRepositoryException() throws SQLException {
        when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException());
        movieRepository.insertMovie(movieBuilder.build(), connectionMock);
    }

    @Test(expected = RepositoryException.class)
    public void insertMovieGenreRelations_ThrowsSQLException_ShouldCatchSQLExceptionAndThrowRepositoryException() throws SQLException {
        when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException());
        movieRepository.insertMovieGenreRelations(movieBuilder.build(), connectionMock);
    }

    @Test
    public void updateSingle_ValidMovie_ShouldTriggerExpectedSQLComponents() throws SQLException {
        Movie movie = movieBuilder.build();

        movieRepository.updateSingle(movie);

        verify(dataSourceMock, times(1)).getConnection();
        verify(connectionMock, times(3)).prepareStatement(anyString());
        verify(preparedStatementMock, times(2)).executeUpdate();
        verify(preparedStatementMock, times(1)).executeBatch();
    }

    @Test(expected = RepositoryException.class)
    public void updateSingle_ThrowsSQLException_ShouldCatchSQLExceptionAndThrowRepositoryException() throws SQLException {
        when(dataSourceMock.getConnection()).thenThrow(new SQLException());
        movieRepository.updateSingle(movieBuilder.build());
    }

    @Test(expected = RepositoryException.class)
    public void updateMovie_ThrowsSQLException_ShouldCatchSQLExceptionAndThrowRepositoryException() throws SQLException {
        when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException());
        movieRepository.updateMovie(movieBuilder.build(), connectionMock);
    }

    @Test(expected = RepositoryException.class)
    public void deleteMovieGenreRelations_ThrowsSQLException_ShouldCatchSQLExceptionAndThrowRepositoryException() throws SQLException {
        when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException());
        movieRepository.deleteMovieGenreRelations(movieBuilder.build(), connectionMock);
    }

    @Test
    public void deleteSingle_ValidId_ShouldTriggerExpectedSQLComponents() throws SQLException {
        Movie movie = movieBuilder.build();

        movieRepository.deleteSingle(movie.getId());

        verify(dataSourceMock, times(1)).getConnection();
        verify(connectionMock, times(1)).prepareStatement(anyString());
        verify(preparedStatementMock, times(1)).executeUpdate();
    }

    @Test(expected = RepositoryException.class)
    public void deleteSingle_ThrowsSQLException_ShouldCatchSQLExceptionAndThrowRepositoryException() throws SQLException {
        when(dataSourceMock.getConnection()).thenThrow(new SQLException());
        movieRepository.deleteSingle(Id.get());
    }

    @Test(expected = RepositoryException.class)
    public void deleteMovie_ThrowsSQLException_ShouldCatchSQLExceptionAndThrowRepositoryException() throws SQLException {
        when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException());
        movieRepository.deleteMovie(Id.get(), connectionMock);
    }

    private void setupMockResultSetForMovie(Movie movie) throws SQLException {
        when(resultSetMock.next()).thenReturn(true, false);
        when(resultSetMock.getString(ID)).thenReturn(movie.getId());
        when(resultSetMock.getString(NAME)).thenReturn(movie.getName());
        when(resultSetMock.getString(IMDB_ID)).thenReturn(movie.getImdbId());
        when(resultSetMock.getString(TAGLINE)).thenReturn(movie.getTagline());
        when(resultSetMock.getString(OVERVIEW)).thenReturn(movie.getOverview());
        when(resultSetMock.getInt(RUNTIME)).thenReturn(movie.getRuntime());
        when(resultSetMock.getDate(RELEASE_DATE)).thenReturn(movie.getReleaseDate().toSqlDate());
        when(resultSetMock.getString(GENRES)).thenReturn(Genre.toString(movie.getGenres()));
        when(resultSetMock.getString(VIDEO_FILE_PATH)).thenReturn(movie.getVideoFile().getPath());
        when(resultSetMock.getString(POSTER_IMAGE_FILE_PATH)).thenReturn(movie.getPosterImageFile().getPath());
        when(resultSetMock.getString(BACKDROP_IMAGE_FILE_PATH)).thenReturn(movie.getBackdropImageFile().getPath());
    }


}
