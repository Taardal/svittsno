package no.svitts.core.repository;

import no.svitts.core.datasource.DataSource;
import no.svitts.core.movie.Genre;
import no.svitts.core.movie.Movie;
import no.svitts.core.movie.UnknownMovie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MovieRepository extends MySqlRepository<Movie> implements Repository<Movie> {

    private static final Logger LOGGER = Logger.getLogger(MovieRepository.class.getName());

    public MovieRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Movie> getAll() {
        LOGGER.log(Level.INFO, "Getting all movies");
        try (Connection connection = dataSource.getConnection()){
            try (PreparedStatement selectAllMoviesPreparedStatement = getSelectAllMoviesPreparedStatement(connection)) {
                return executeQuery(selectAllMoviesPreparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not get all movies", e);
        }
        return new ArrayList<>();
    }

    @Override
    public Movie getById(String id) {
        LOGGER.log(Level.INFO, "Getting movie with ID [" + id + "]");
        try (Connection connection = dataSource.getConnection()){
            try (PreparedStatement selectMoviePreparedStatement = getSelectMoviePreparedStatement(id, connection)) {
                return executeQuery(selectMoviePreparedStatement).get(0);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not get movie with ID [" + id + "]", e);
        }
        return new UnknownMovie();
    }

    @Override
    public boolean insertSingle(Movie movie) {
        LOGGER.log(Level.INFO, "Inserting movie [" + movie.toString() + "]");
        try (Connection connection = dataSource.getConnection()){
            try (PreparedStatement insertMoviePreparedStatement = getInsertMoviePreparedStatement(movie, connection)) {
                executeUpdate(insertMoviePreparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not insert movie [" + movie.toString() + "]", e);
        }
        return false;
    }

    @Override
    public boolean updateSingle(Movie movie) {
        LOGGER.log(Level.INFO, "Updating movie [" + movie.toString() + "]");
        try (Connection connection = dataSource.getConnection()){
            try (PreparedStatement updateMoviePreparedStatement = getUpdateMoviePreparedStatement(movie, connection)) {
                executeUpdate(updateMoviePreparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not update movie [" + movie.toString() + "]", e);
        }
        return false;
    }

    @Override
    public boolean deleteSingle(String id) {
        LOGGER.log(Level.INFO, "Deleting movie with ID [" + id + "]");
        try (Connection connection = dataSource.getConnection()){
            try (PreparedStatement deleteMoviePreparedStatement = getDeleteMoviePreparedStatement(id, connection)) {
                executeUpdate(deleteMoviePreparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not delete movie with ID [" + id + "]", e);
        }
        return false;
    }

    @Override
    protected List<Movie> getResults(ResultSet resultSet) throws SQLException {
        List<Movie> movies = new ArrayList<>();
        while (resultSet.next()) {
            Movie movie = new Movie(resultSet.getString("id"));
            movie.setName(resultSet.getString("name"));
            movie.setTagline(resultSet.getString("tagline"));
            movie.setOverview(resultSet.getString("overview"));
            movie.setImdbId(resultSet.getString("imdb_id"));
            movie.setRuntime(resultSet.getInt("runtime"));
            movie.setReleaseDate(resultSet.getDate("release_date"));
            movie.setGenre(Genre.valueOf(resultSet.getString("genre")));
            movies.add(movie);
        }
        LOGGER.log(Level.INFO, "Got movie(s) " + movies.toString());
        return movies;
    }

    private PreparedStatement getSelectAllMoviesPreparedStatement(Connection connection) throws SQLException {
        String query = "SELECT movie.*, genre.name AS genre FROM movie " +
                "JOIN movie_genre ON movie.id = movie_genre.movie_id " +
                "JOIN genre ON genre.id = movie_genre.genre_id;";
        return connection.prepareStatement(query);
    }

    private PreparedStatement getSelectMoviePreparedStatement(String id, Connection connection) throws SQLException {
        String query = "SELECT movie.*, genre.name AS genre FROM movie " +
                "JOIN movie_genre ON movie.id = movie_genre.movie_id " +
                "JOIN genre ON genre.id = movie_genre.genre_id " +
                "WHERE movie.id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, id);
        return preparedStatement;
    }

    private PreparedStatement getInsertMoviePreparedStatement(Movie movie, Connection connection) throws SQLException {
        String statement = "INSERT INTO movie VALUES(?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        preparedStatement.setString(1, movie.getId());
        preparedStatement.setString(2, movie.getImdbId());
        preparedStatement.setString(3, movie.getName());
        preparedStatement.setString(4, movie.getTagline());
        preparedStatement.setString(5, movie.getOverview());
        preparedStatement.setInt(6, movie.getRuntime());
        preparedStatement.setDate(7, movie.getReleaseDate());
        return preparedStatement;
    }

    private PreparedStatement getUpdateMoviePreparedStatement(Movie movie, Connection connection) throws SQLException {
        String statement = "UPDATE movie SET imdb_id = ?, name = ?, tagline = ?, overview = ?, runtime = ?, release_date = ? WHERE id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        preparedStatement.setString(1, movie.getImdbId());
        preparedStatement.setString(2, movie.getName());
        preparedStatement.setString(3, movie.getTagline());
        preparedStatement.setString(4, movie.getOverview());
        preparedStatement.setInt(5, movie.getRuntime());
        preparedStatement.setDate(6, movie.getReleaseDate());
        preparedStatement.setString(7, movie.getId());
        return preparedStatement;
    }

    private PreparedStatement getDeleteMoviePreparedStatement(String id, Connection connection) throws SQLException {
        String query = "DELETE FROM movie WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, id);
        return preparedStatement;
    }

}
