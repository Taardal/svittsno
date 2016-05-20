package no.svitts.core.repository;

import no.svitts.core.datasource.DataSource;
import no.svitts.core.datasource.SqlDataSource;
import no.svitts.core.id.Id;
import no.svitts.core.movie.Genre;
import no.svitts.core.movie.Movie;
import no.svitts.core.testdatabuilder.MovieTestDataBuilder;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static no.svitts.core.repository.MovieRepository.UNKNOWN_MOVIE_ID;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class MovieRepositoryTest {

    private DataSource mockDataSource;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    private MovieRepository movieRepository;
    private MovieTestDataBuilder movieTestDataBuilder;

    @Before
    public void setUp() throws SQLException {
        mockDataSource = mock(SqlDataSource.class);
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockPreparedStatement.executeBatch()).thenReturn(new int[]{1});

        movieRepository = new MovieRepository(mockDataSource, videoFileRepository, imageFileRepository);
        movieTestDataBuilder = new MovieTestDataBuilder();
    }

    @Test
    public void getAll_ShouldExecuteQueryAndReturnListWithExpectedMovies() throws SQLException {
        Movie sherlockHolmes = movieTestDataBuilder.sherlockHolmes().build();
        setupMockResultSetForMovie(sherlockHolmes);

        List<Movie> movies = movieRepository.getAll();

        assertEquals(1, movies.size());
        assertMovie(sherlockHolmes, movies.get(0));
        verify(mockDataSource, times(1)).getConnection();
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).executeQuery();
        verify(mockResultSet, times(2)).next();
    }

    @Test
    public void getAll_ThrowsSQLException_ShouldHandleSQLExceptionAndReturnEmptyList() throws SQLException {
        when(mockDataSource.getConnection()).thenThrow(new SQLException());

        List<Movie> movies = movieRepository.getAll();

        assertEquals(0, movies.size());
        verify(mockDataSource, times(1)).getConnection();
    }

    @Test
    public void getById_ShouldExecuteQueryAndReturnExpectedMovie() throws SQLException {
        Movie sherlockHolmes = movieTestDataBuilder.sherlockHolmes().build();
        setupMockResultSetForMovie(sherlockHolmes);

        Movie movie = movieRepository.getById(sherlockHolmes.getId());

        assertMovie(sherlockHolmes, movie);
        verify(mockDataSource, times(1)).getConnection();
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).executeQuery();
        verify(mockResultSet, times(2)).next();
    }

    @Test
    public void getByID_ThrowsSQLException_ShouldHandleSQLExceptionAndReturnUnknownMovie() throws SQLException {
        when(mockDataSource.getConnection()).thenThrow(new SQLException());

        Movie movie = movieRepository.getById(Id.get());

        assertEquals(UNKNOWN_MOVIE_ID, movie.getId());
        verify(mockDataSource, times(1)).getConnection();
    }

    @Test
    public void insertSingle_ShouldExecuteStatementsAndReturnTrue() throws SQLException {
        Movie sherlockHolmes = movieTestDataBuilder.sherlockHolmes().build();

        boolean success = movieRepository.insert(sherlockHolmes);

        assertTrue(success);
        verify(mockDataSource, times(2)).getConnection();
        verify(mockConnection, times(2)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).executeUpdate();
        verify(mockPreparedStatement, times(1)).executeBatch();
    }

    @Test
    public void insertSingle_RequiredFieldsInvalid_ShouldNotExecuteStatementsAndReturnFalse() throws SQLException {
        Movie movie = movieTestDataBuilder.id("").name("").build();

        boolean success = movieRepository.insert(movie);

        assertFalse(success);
        verify(mockDataSource, times(0)).getConnection();
        verify(mockConnection, times(0)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(0)).executeUpdate();
        verify(mockPreparedStatement, times(0)).executeBatch();
    }

    @Test
    public void insertSingle_ThrowsSQLException_ShouldHandleSQLExceptionAndReturnFalse() throws SQLException {
        when(mockDataSource.getConnection()).thenThrow(new SQLException());

        boolean success = movieRepository.insert(movieTestDataBuilder.build());

        assertFalse(success);
        verify(mockDataSource, times(1)).getConnection();
    }

    @Test
    public void updateSingle_ShouldExecuteStatementsAndReturnTrue() throws SQLException {
        Movie movie = movieTestDataBuilder.build();
        
        boolean success = movieRepository.update(movie);

        assertTrue(success);
        verify(mockDataSource, times(1)).getConnection();
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void updateSingle_ThrowsSQLException_ShouldHandleSQLExceptionAndReturnFalse() throws SQLException {
        when(mockDataSource.getConnection()).thenThrow(new SQLException());

        boolean success = movieRepository.update(movieTestDataBuilder.build());

        assertFalse(success);
        verify(mockDataSource, times(1)).getConnection();
    }

    @Test
    public void deleteSingle_ShouldExecuteStatementsAndReturnTrue() throws SQLException {
        Movie movie = movieTestDataBuilder.build();

        boolean success = movieRepository.delete(movie.getId());

        assertTrue(success);
        verify(mockDataSource, times(1)).getConnection();
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void deleteSingle_ThrowsSQLException_ShouldHandleSQLExceptionAndReturnFalse() throws SQLException {
        when(mockDataSource.getConnection()).thenThrow(new SQLException());

        boolean success = movieRepository.delete(Id.get());

        assertFalse(success);
        verify(mockDataSource, times(1)).getConnection();
    }

    private void setupMockResultSetForMovie(Movie movie) throws SQLException {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("id")).thenReturn(movie.getId());
        when(mockResultSet.getString("name")).thenReturn(movie.getName());
        when(mockResultSet.getString("imdb_id")).thenReturn(movie.getImdbId());
        when(mockResultSet.getString("tagline")).thenReturn(movie.getTagline());
        when(mockResultSet.getString("overview")).thenReturn(movie.getOverview());
        when(mockResultSet.getInt("runtime")).thenReturn(movie.getRuntime());
        when(mockResultSet.getDate("release_date")).thenReturn(movie.getReleaseDate().toSqlDate());
        when(mockResultSet.getString("genres")).thenReturn(Genre.toString(movie.getGenres()));
    }

    private void assertMovie(Movie expectedMovie, Movie actualMovie) {
        assertEquals(expectedMovie.getId(), actualMovie.getId());
        assertEquals(expectedMovie.getName(), actualMovie.getName());
        assertEquals(expectedMovie.getImdbId(), actualMovie.getImdbId());
        assertEquals(expectedMovie.getTagline(), actualMovie.getTagline());
        assertEquals(expectedMovie.getOverview(), actualMovie.getOverview());
        assertEquals(expectedMovie.getRuntime(), actualMovie.getRuntime());
        assertEquals(expectedMovie.getReleaseDate().getTime(), actualMovie.getReleaseDate().getTime());
        assertEquals(expectedMovie.getGenres(), actualMovie.getGenres());
    }

}
