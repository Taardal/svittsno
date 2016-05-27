package no.svitts.core.repository;

import no.svitts.core.builder.MovieBuilder;
import no.svitts.core.datasource.CoreDataSource;
import no.svitts.core.datasource.DataSource;
import no.svitts.core.id.Id;
import no.svitts.core.movie.Genre;
import no.svitts.core.movie.Movie;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.InternalServerErrorException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static no.svitts.core.testkit.MovieTestKit.assertMovie;
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
        when(preparedStatementMock.executeBatch()).thenReturn(new int[]{1});

        movieRepository = new MovieRepository(dataSourceMock);
    }

    @Test
    public void getById_ValidId_ShouldReturnExpectedMovie() throws SQLException {
        Movie movie = movieBuilder.build();
        setupMockResultSetForMovie(movie);

        Movie movieFromRepository = movieRepository.getById(movie.getId());

        assertMovie(movie, movieFromRepository);
        verify(dataSourceMock, times(1)).getConnection();
        verify(connectionMock, times(1)).prepareStatement(anyString());
        verify(preparedStatementMock, times(1)).executeQuery();
        verify(resultSetMock, times(2)).next();
    }

    @Test(expected = InternalServerErrorException.class)
    public void getById_ThrowsSQLExceptionWhenGettingConnection_ShouldCatchSQLExceptionAndThrowInternalServerErrorException() throws SQLException {
        when(dataSourceMock.getConnection()).thenThrow(new SQLException());
        movieRepository.getById(Id.get());
        verify(dataSourceMock, times(1)).getConnection();
    }

    @Test(expected = InternalServerErrorException.class)
    public void getMovie_ThrowsSQLExceptionWhenGettingPreparedStatement_ShouldCatchSQLExceptionAndThrowInternalServerErrorException() throws SQLException {
        when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException());
        movieRepository.getMovie(Id.get(), connectionMock);
        verify(connectionMock, times(0)).prepareStatement(anyString());
    }

    @Test
    public void insertSingle_ShouldExecuteStatementsAndReturnTrue() throws SQLException {
        Movie movie = movieBuilder.build();

        String id = movieRepository.insert(movie);

        verify(dataSourceMock, times(2)).getConnection();
        verify(connectionMock, times(2)).prepareStatement(anyString());
        verify(preparedStatementMock, times(1)).executeUpdate();
        verify(preparedStatementMock, times(1)).executeBatch();
    }

    @Test
    public void insertSingle_RequiredFieldsInvalid_ShouldNotExecuteStatementsAndReturnFalse() throws SQLException {
        Movie movie = movieBuilder.id("").name("").build();

        String success = movieRepository.insert(movie);

        verify(dataSourceMock, times(0)).getConnection();
        verify(connectionMock, times(0)).prepareStatement(anyString());
        verify(preparedStatementMock, times(0)).executeUpdate();
        verify(preparedStatementMock, times(0)).executeBatch();
    }

    @Test
    public void insertSingle_ThrowsSQLException_ShouldHandleSQLExceptionAndReturnFalse() throws SQLException {
        when(dataSourceMock.getConnection()).thenThrow(new SQLException());

        String success = movieRepository.insert(movieBuilder.build());

        verify(dataSourceMock, times(1)).getConnection();
    }

    @Test
    public void updateSingle_ShouldExecuteStatementsAndReturnTrue() throws SQLException {
        Movie movie = movieBuilder.build();

        movieRepository.update(movie);

        verify(dataSourceMock, times(1)).getConnection();
        verify(connectionMock, times(1)).prepareStatement(anyString());
        verify(preparedStatementMock, times(1)).executeUpdate();
    }

    @Test
    public void updateSingle_ThrowsSQLException_ShouldHandleSQLExceptionAndReturnFalse() throws SQLException {
        when(dataSourceMock.getConnection()).thenThrow(new SQLException());

        movieRepository.update(movieBuilder.build());

        verify(dataSourceMock, times(1)).getConnection();
    }

    @Test
    public void deleteSingle_ShouldExecuteStatementsAndReturnTrue() throws SQLException {
        Movie movie = movieBuilder.build();

        movieRepository.delete(movie.getId());

        verify(dataSourceMock, times(1)).getConnection();
        verify(connectionMock, times(1)).prepareStatement(anyString());
        verify(preparedStatementMock, times(1)).executeUpdate();
    }

    @Test
    public void deleteSingle_ThrowsSQLException_ShouldHandleSQLExceptionAndReturnFalse() throws SQLException {
        when(dataSourceMock.getConnection()).thenThrow(new SQLException());

        movieRepository.delete(Id.get());

        verify(dataSourceMock, times(1)).getConnection();
    }

    private void setupMockResultSetForMovie(Movie movie) throws SQLException {
        when(resultSetMock.next()).thenReturn(true, false);
        when(resultSetMock.getString("id")).thenReturn(movie.getId());
        when(resultSetMock.getString("name")).thenReturn(movie.getName());
        when(resultSetMock.getString("imdb_id")).thenReturn(movie.getImdbId());
        when(resultSetMock.getString("tagline")).thenReturn(movie.getTagline());
        when(resultSetMock.getString("overview")).thenReturn(movie.getOverview());
        when(resultSetMock.getInt("runtime")).thenReturn(movie.getRuntime());
        when(resultSetMock.getDate("release_date")).thenReturn(movie.getReleaseDate().toSqlDate());
        when(resultSetMock.getString("genres")).thenReturn(Genre.toString(movie.getGenres()));
        when(resultSetMock.getString("video_file_path")).thenReturn(movie.getVideoFile().getPath());
        when(resultSetMock.getString("poster_image_file_path")).thenReturn(movie.getPosterImageFile().getPath());
        when(resultSetMock.getString("backdrop_image_file_path")).thenReturn(movie.getBackdropImageFile().getPath());
    }


}
