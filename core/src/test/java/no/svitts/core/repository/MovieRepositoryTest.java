package no.svitts.core.repository;

import no.svitts.core.builder.MovieBuilder;
import no.svitts.core.datasource.DataSource;
import no.svitts.core.datasource.SqlDataSource;
import no.svitts.core.movie.Genre;
import no.svitts.core.movie.Movie;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static no.svitts.core.repository.MovieRepository.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class MovieRepositoryTest {

    private MovieRepository movieRepository;
    private MovieBuilder movieBuilder;
    private DataSource mockDataSource;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

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

        movieRepository = new MovieRepository(mockDataSource);
        movieBuilder = new MovieBuilder();
    }

    @Test
    public void getAll_ShouldReturnExpectedMovies() throws SQLException {
        Movie sherlockHolmes = movieBuilder.sherlockHolmes().build();
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
    public void getById_ShouldReturnExpectedMovie() throws SQLException {
        Movie sherlockHolmes = movieBuilder.sherlockHolmes().build();
        setupMockResultSetForMovie(sherlockHolmes);

        Movie movie = movieRepository.getById(sherlockHolmes.getId());

        assertMovie(sherlockHolmes, movie);
        verify(mockDataSource, times(1)).getConnection();
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).executeQuery();
        verify(mockResultSet, times(2)).next();
    }

    @Test
    public void getByAttributes_ShouldReturnExpectedMovie() throws SQLException {
        Movie sherlockHolmes = movieBuilder.sherlockHolmes().build();
        setupMockResultSetForMovie(sherlockHolmes);

        Movie movie = movieRepository.getByAttributes(sherlockHolmes.getName());

        assertMovie(sherlockHolmes, movie);
        verify(mockDataSource, times(1)).getConnection();
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).executeQuery();
        verify(mockResultSet, times(2)).next();
    }

    @Test
    public void insertSingle_ShouldReturnTrue() throws SQLException {
        Movie sherlockHolmes = movieBuilder.sherlockHolmes().build();

        boolean success = movieRepository.insertSingle(sherlockHolmes);

        assertTrue(success);
        verify(mockDataSource, times(2)).getConnection();
        verify(mockConnection, times(2)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).executeUpdate();
        verify(mockPreparedStatement, times(1)).executeBatch();
    }

    @Test
    public void insertMultiple_ShouldReturnTrue() throws SQLException {
        List<Movie> movies = new ArrayList<>();
        movies.add(movieBuilder.sherlockHolmes().build());
        movies.add(movieBuilder.sherlockHolmesAGameOfShadows().build());

        boolean success = movieRepository.insertMultiple(movies);

        assertTrue(success);
        verify(mockDataSource, times(4)).getConnection();
        verify(mockConnection, times(4)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(2)).executeUpdate();
        verify(mockPreparedStatement, times(2)).executeBatch();
    }

    @Test
    public void updateSingle_ShouldReturnTrue() throws SQLException {
        Movie movie = movieBuilder.build();
        
        boolean success = movieRepository.updateSingle(movie);

        assertTrue(success);
        verify(mockDataSource, times(1)).getConnection();
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void deleteSingle_ShouldReturnTrue() throws SQLException {
        Movie movie = movieBuilder.build();

        boolean success = movieRepository.deleteSingle(movie.getId());

        assertTrue(success);
        verify(mockDataSource, times(1)).getConnection();
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    private void setupMockResultSetForMovie(Movie movie) throws SQLException {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString(ID)).thenReturn(movie.getId());
        when(mockResultSet.getString(NAME)).thenReturn(movie.getName());
        when(mockResultSet.getString(IMDB_ID)).thenReturn(movie.getImdbId());
        when(mockResultSet.getString(TAGLINE)).thenReturn(movie.getTagline());
        when(mockResultSet.getString(OVERVIEW)).thenReturn(movie.getOverview());
        when(mockResultSet.getInt(RUNTIME)).thenReturn(movie.getRuntime());
        when(mockResultSet.getDate(RELEASE_DATE)).thenReturn(movie.getReleaseDate().toSqlDate());
        when(mockResultSet.getString(GENRES)).thenReturn(Genre.toString(movie.getGenres()));
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
