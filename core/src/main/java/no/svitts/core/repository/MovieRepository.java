package no.svitts.core.repository;

import no.svitts.core.datasource.DataSource;
import no.svitts.core.date.KeyDate;
import no.svitts.core.movie.Genre;
import no.svitts.core.movie.Movie;
import no.svitts.core.movie.UnknownMovie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieRepository extends SqlRepository<Movie> implements Repository<Movie> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieRepository.class);

    public MovieRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Movie> getAll() {
        LOGGER.info("Getting all movies");
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement selectAllMoviesPreparedStatement = getSelectAllMoviesPreparedStatement(connection)) {
                return executeQuery(selectAllMoviesPreparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not get all movies", e);
            return new ArrayList<>();
        }
    }

    @Override
    public Movie getById(String id) {
        LOGGER.info("Getting movie with ID {}", id);
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement selectMoviePreparedStatement = getSelectMoviePreparedStatement(id, connection)) {
                return executeQuery(selectMoviePreparedStatement).get(0);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not get movie with ID {}", id, e);
            return new UnknownMovie();
        }
    }

    @Override
    public Movie getByAttributes(Object... objects) {
        LOGGER.info("Getting movie by attributes");
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement selectMoviePreparedStatement = getSelectMoviePreparedStatement(objects, connection)) {
                return executeQuery(selectMoviePreparedStatement).get(0);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not get movie by attributes", e);
            return new UnknownMovie();
        }
    }

    @Override
    public boolean insertSingle(Movie movie) {
        LOGGER.info("Inserting movie {}", movie.toString());
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement insertMoviePreparedStatement = getInsertMoviePreparedStatement(movie, connection)) {
                return executeUpdate(insertMoviePreparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not insert movie {}", movie.toString(), e);
            return false;
        }
    }

    @Override
    public boolean updateSingle(Movie movie) {
        LOGGER.info("Updating movie {}", movie.toString());
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement updateMoviePreparedStatement = getUpdateMoviePreparedStatement(movie, connection)) {
                return executeUpdate(updateMoviePreparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not update movie {}", movie.toString(), e);
            return false;
        }
    }

    @Override
    public boolean deleteSingle(String id) {
        LOGGER.info("Deleting movie with ID {}", id);
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement deleteMoviePreparedStatement = getDeleteMoviePreparedStatement(id, connection)) {
                return executeUpdate(deleteMoviePreparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not delete movie with ID {}", id, e);
            return false;
        }
    }

    @Override
    protected List<Movie> getResults(ResultSet resultSet) throws SQLException {
        List<Movie> movies = new ArrayList<>();
        while (resultSet.next()) {
            String id = resultSet.getString("id");
            String name = resultSet.getString("name");
            String imdbId = resultSet.getString("imdb_id");
            String tagline = resultSet.getString("tagline");
            String overview = resultSet.getString("overview");
            int runtime = resultSet.getInt("runtime");
            KeyDate releaseDate = new KeyDate(resultSet.getDate("release_date"));
            List<Genre> genres = getGenres(resultSet.getString("genres"));
            movies.add(new Movie(id, name, imdbId, tagline, overview, runtime, releaseDate, genres));
        }
        LOGGER.info("Got movie(s) {}", movies.toString());
        return movies;
    }

    private List<Genre> getGenres(String genresString) throws SQLException {
        String[] genreStrings = genresString.split(",");
        List<Genre> genres = new ArrayList<>();
        for (String genreString : genreStrings) {
            genres.add(Genre.valueOf(genreString));
        }
        return genres;
    }

    private PreparedStatement getSelectAllMoviesPreparedStatement(Connection connection) throws SQLException {
        String query = "SELECT movie.*, genre.name AS genre FROM movie " +
                "JOIN movie_genre ON movie.id = movie_genre.movie_id " +
                "JOIN genre ON genre.id = movie_genre.genre_id;";
        return connection.prepareStatement(query);
    }

    private PreparedStatement getSelectMoviePreparedStatement(String id, Connection connection) throws SQLException {
        String query = "SELECT movie.*, GROUP_CONCAT(DISTINCT genre.name) AS genres FROM movie " +
                "JOIN movie_genre ON movie.id = movie_genre.movie_id " +
                "JOIN genre ON genre.id = movie_genre.genre_id " +
                "WHERE movie.id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, id);
        return preparedStatement;
    }

    private PreparedStatement getSelectMoviePreparedStatement(Object[] attributes, Connection connection) throws SQLException {
        String query = "SELECT movie.*, GROUP_CONCAT(DISTINCT genre.name) AS genres FROM movie " +
                "JOIN movie_genre ON movie.id = movie_genre.movie_id " +
                "JOIN genre ON genre.id = movie_genre.genre_id " +
                "WHERE movie.name = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, (String) attributes[0]);
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
        preparedStatement.setDate(7, movie.getReleaseDate().toJavaSqlDate());
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
        preparedStatement.setDate(6, movie.getReleaseDate().toJavaSqlDate());
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
