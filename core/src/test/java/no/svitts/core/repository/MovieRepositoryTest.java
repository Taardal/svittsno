package no.svitts.core.repository;

import no.svitts.core.datasource.DataSource;
import no.svitts.core.datasource.SqlDataSource;
import no.svitts.core.id.Id;
import no.svitts.core.movie.Genre;
import no.svitts.core.movie.Movie;
import no.svitts.core.testdatabuilder.MovieTestDataBuilder;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.InternalServerErrorException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class MovieRepositoryTest {

    private MovieTestDataBuilder movieTestDataBuilder;
    private DataSource mockDataSource;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    private MovieRepository movieRepository;

    @Before
    public void setUp() throws SQLException {
        movieTestDataBuilder = new MovieTestDataBuilder();

        mockDataSource = mock(SqlDataSource.class);
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockPreparedStatement.executeBatch()).thenReturn(new int[]{1});

        movieRepository = new MovieRepository(mockDataSource);
    }

    @Test
    public void getById_ShouldReturnExpectedMovie() throws SQLException {
        Movie movie = movieTestDataBuilder.build();
        setupMockResultSetForMovie(movie);

        Movie movieFromRepository = movieRepository.getById(movie.getId());

        assertMovie(movie, movieFromRepository);
        verify(mockDataSource, times(1)).getConnection();
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).executeQuery();
        verify(mockResultSet, times(2)).next();
    }

    @Test(expected = InternalServerErrorException.class)
    public void getById_ThrowsSQLException_ShouldCatchSQLExceptionAndThrowInternalServerErrorException() throws SQLException {
        when(mockDataSource.getConnection()).thenThrow(new SQLException());
        movieRepository.getById(Id.get());
        verify(mockDataSource, times(1)).getConnection();
    }

    @Test
    public void insertSingle_ShouldExecuteStatementsAndReturnTrue() throws SQLException {
        Movie movie = movieTestDataBuilder.build();

        String id = movieRepository.insert(movie);

        verify(mockDataSource, times(2)).getConnection();
        verify(mockConnection, times(2)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).executeUpdate();
        verify(mockPreparedStatement, times(1)).executeBatch();
    }

    @Test
    public void insertSingle_RequiredFieldsInvalid_ShouldNotExecuteStatementsAndReturnFalse() throws SQLException {
        Movie movie = movieTestDataBuilder.id("").name("").build();

        String success = movieRepository.insert(movie);

        verify(mockDataSource, times(0)).getConnection();
        verify(mockConnection, times(0)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(0)).executeUpdate();
        verify(mockPreparedStatement, times(0)).executeBatch();
    }

    @Test
    public void insertSingle_ThrowsSQLException_ShouldHandleSQLExceptionAndReturnFalse() throws SQLException {
        when(mockDataSource.getConnection()).thenThrow(new SQLException());

        String success = movieRepository.insert(movieTestDataBuilder.build());

        verify(mockDataSource, times(1)).getConnection();
    }

    @Test
    public void updateSingle_ShouldExecuteStatementsAndReturnTrue() throws SQLException {
        Movie movie = movieTestDataBuilder.build();

        movieRepository.update(movie);

        verify(mockDataSource, times(1)).getConnection();
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void updateSingle_ThrowsSQLException_ShouldHandleSQLExceptionAndReturnFalse() throws SQLException {
        when(mockDataSource.getConnection()).thenThrow(new SQLException());

        movieRepository.update(movieTestDataBuilder.build());

        verify(mockDataSource, times(1)).getConnection();
    }

    @Test
    public void deleteSingle_ShouldExecuteStatementsAndReturnTrue() throws SQLException {
        Movie movie = movieTestDataBuilder.build();

        movieRepository.delete(movie.getId());

        verify(mockDataSource, times(1)).getConnection();
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void deleteSingle_ThrowsSQLException_ShouldHandleSQLExceptionAndReturnFalse() throws SQLException {
        when(mockDataSource.getConnection()).thenThrow(new SQLException());

        movieRepository.delete(Id.get());

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
        when(mockResultSet.getString("video_file_path")).thenReturn(movie.getVideoFile().getPath());
        when(mockResultSet.getString("poster_image_file_path")).thenReturn(movie.getPosterImageFile().getPath());
        when(mockResultSet.getString("backdrop_image_file_path")).thenReturn(movie.getBackdropImageFile().getPath());
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
