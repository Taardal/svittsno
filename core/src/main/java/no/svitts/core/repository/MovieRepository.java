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

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String IMDB_ID = "imdb_id";
    public static final String TAGLINE = "tagline";
    public static final String OVERVIEW = "overview";
    public static final String RUNTIME = "runtime";
    public static final String RELEASE_DATE = "release_date";
    public static final String GENRES = "genres";
    private static final Logger LOGGER = LoggerFactory.getLogger(MovieRepository.class);

    public MovieRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Movie> getAll() {
        return getAllMovies();
    }

    @Override
    public List<Movie> getMultiple(List<Movie> movies) {
        return getMovies(movies);
    }

    @Override
    public Movie getById(String id) {
        return getMovie(id);
    }

    @Override
    public Movie getByAttributes(Object... attributes) {
        return getMovie(attributes);
    }

    @Override
    public boolean insertSingle(Movie movie) {
        if (isRequiredFieldsValid(movie)) {
            return insertMovie(movie) && insertMovieGenreRelations(movie);
        } else {
            LOGGER.warn("Required fields INVALID for movie [{}]", movie);
            return false;
        }
    }

    @Override
    public boolean updateSingle(Movie movie) {
        return updateMovie(movie);
    }

    @Override
    public boolean deleteSingle(String id) {
        return deleteMovie(id);
    }

    @Override
    protected List<Movie> getResults(ResultSet resultSet) throws SQLException {
        List<Movie> movies = new ArrayList<>();
        while (resultSet.next()) {
            String id = resultSet.getString(ID);
            String name = resultSet.getString(NAME);
            String imdbId = resultSet.getString(IMDB_ID);
            String tagline = resultSet.getString(TAGLINE);
            String overview = resultSet.getString(OVERVIEW);
            int runtime = resultSet.getInt(RUNTIME);
            KeyDate releaseDate = new KeyDate(resultSet.getDate(RELEASE_DATE));
            List<Genre> genres = Genre.fromString(resultSet.getString(GENRES));
            movies.add(new Movie(id, name, imdbId, tagline, overview, runtime, releaseDate, genres));
        }
        LOGGER.info("Got movie(s) [{}]", movies.toString());
        return movies;
    }

    @Override
    protected boolean isRequiredFieldsValid(Movie movie) {
        return movie.getId() != null && !movie.getId().isEmpty() && movie.getName() != null && !movie.getName().isEmpty();
    }

    private List<Movie> getAllMovies() {
        LOGGER.info("Getting all movies");
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = getSelectAllMoviesPreparedStatement(connection)) {
                return executeQuery(preparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not get all movies", e);
            return new ArrayList<>();
        }
    }

    private PreparedStatement getSelectAllMoviesPreparedStatement(Connection connection) throws SQLException {
        String query = "SELECT movie.*, genre.name AS genre FROM movie " +
                "JOIN movie_genre ON movie.id = movie_genre.movie_id " +
                "JOIN genre ON genre.id = movie_genre.genre_id;";
        return connection.prepareStatement(query);
    }

    private List<Movie> getMovies(List<Movie> movies) {
        LOGGER.info("Getting movies [{}]", movies.toString());
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = getSelectMoviesPreparedStatement(connection, movies)) {
                return executeQuery(preparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not get movies [{}]", movies.toString());
            return new ArrayList<>();
        }
    }

    private PreparedStatement getSelectMoviesPreparedStatement(Connection connection, List<Movie> movies) throws SQLException {
        String selectMoviesQuery = getSelectMoviesQuery(movies.size());
        PreparedStatement preparedStatement = connection.prepareStatement(selectMoviesQuery);
        int i = 1;
        for (Movie movie : movies) {
            preparedStatement.setString(i++, movie.getId());
        }
        return preparedStatement;
    }

    private String getSelectMoviesQuery(int numberOfMovies) {
        String parameters = "";
        for (int i = 0; i < numberOfMovies; i++) {
            if (i > 0) {
                parameters += ", ";
            }
            parameters += "?";
        }
        return "SELECT * from movie WHERE id IN (" + parameters + ")";
    }

    private Movie getMovie(String id) {
        LOGGER.info("Getting movie by ID [{}]", id);
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = getSelectMoviePreparedStatement(connection, id)) {
                return executeQuery(preparedStatement).get(0);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not get movie by ID [{}]", id, e);
            return new UnknownMovie();
        }
    }

    private PreparedStatement getSelectMoviePreparedStatement(Connection connection, String id) throws SQLException {
        String query = "SELECT movie.*, GROUP_CONCAT(DISTINCT genre.name) AS genres FROM movie " +
                "JOIN movie_genre ON movie.id = movie_genre.movie_id " +
                "JOIN genre ON genre.id = movie_genre.genre_id " +
                "WHERE movie.id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, id);
        return preparedStatement;
    }

    private Movie getMovie(Object[] attributes) {
        LOGGER.info("Getting movie by attributes");
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = getSelectMoviePreparedStatement(connection, attributes)) {
                List<Movie> movies = executeQuery(preparedStatement);
                return movies.size() > 0 ? movies.get(0) : new UnknownMovie();
            }
        } catch (SQLException e) {
            LOGGER.error("Could not get movie by attributes", e);
            return new UnknownMovie();
        }
    }

    private PreparedStatement getSelectMoviePreparedStatement(Connection connection, Object[] attributes) throws SQLException {
        String query = "SELECT movie.*, GROUP_CONCAT(DISTINCT genre.name) AS genres FROM movie " +
                "JOIN movie_genre ON movie.id = movie_genre.movie_id " +
                "JOIN genre ON genre.id = movie_genre.genre_id " +
                "WHERE movie.name = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, (String) attributes[0]);
        return preparedStatement;
    }

    private boolean insertMovie(Movie movie) {
        LOGGER.info("Inserting movie [{}]", movie.toString());
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = getInsertMoviePreparedStatement(connection, movie)) {
                return executeUpdate(preparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not insert movie [{}]", movie.toString(), e);
            return false;
        }
    }

    private PreparedStatement getInsertMoviePreparedStatement(Connection connection, Movie movie) throws SQLException {
        String statement = "INSERT INTO movie (id, imdb_id, name, tagline, overview, runtime, release_date) VALUES (?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        int i = 1;
        preparedStatement.setString(i++, movie.getId());
        preparedStatement.setString(i++, movie.getImdbId());
        preparedStatement.setString(i++, movie.getName());
        preparedStatement.setString(i++, movie.getTagline());
        preparedStatement.setString(i++, movie.getOverview());
        preparedStatement.setInt(i++, movie.getRuntime());
        preparedStatement.setDate(i, movie.getReleaseDate().toSqlDate());
        return preparedStatement;
    }

    private boolean insertMovieGenreRelations(Movie movie) {
        LOGGER.info("Inserting movie/genre relations for movie [{}]", movie.toString());
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = getInsertMovieGenreRelations(connection, movie)) {
                return executeBatch(preparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not insert movie/genre relations for movie [{}]", movie.toString(), e);
            return false;
        }
    }

    private PreparedStatement getInsertMovieGenreRelations(Connection connection, Movie movie) throws SQLException {
        String statement = "INSERT INTO movie_genre (movie_id, genre_id) VALUES (?, ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        for (Genre genre : movie.getGenres()) {
            int i = 1;
            preparedStatement.setString(i++, movie.getId());
            preparedStatement.setInt(i, genre.getId());
            preparedStatement.addBatch();
        }
        return preparedStatement;
    }

    private boolean updateMovie(Movie movie) {
        LOGGER.info("Updating movie [{}]", movie.toString());
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = getUpdateMoviePreparedStatement(connection, movie)) {
                return executeUpdate(preparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not update movie [{}]", movie.toString(), e);
            return false;
        }
    }

    private PreparedStatement getUpdateMoviePreparedStatement(Connection connection, Movie movie) throws SQLException {
        String statement = "UPDATE movie SET imdb_id = ?, name = ?, tagline = ?, overview = ?, runtime = ?, release_date = ? WHERE id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        int i = 1;
        preparedStatement.setString(i++, movie.getImdbId());
        preparedStatement.setString(i++, movie.getName());
        preparedStatement.setString(i++, movie.getTagline());
        preparedStatement.setString(i++, movie.getOverview());
        preparedStatement.setInt(i++, movie.getRuntime());
        preparedStatement.setDate(i++, movie.getReleaseDate().toSqlDate());
        preparedStatement.setString(i, movie.getId());
        return preparedStatement;
    }

    private boolean deleteMovie(String id) {
        LOGGER.info("Deleting movie with ID [{}]", id);
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = getDeleteMoviePreparedStatement(connection, id)) {
                return executeUpdate(preparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not delete movie with ID [{}]", id, e);
            return false;
        }
    }

    private PreparedStatement getDeleteMoviePreparedStatement(Connection connection, String id) throws SQLException {
        String statement = "DELETE FROM movie WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        preparedStatement.setString(1, id);
        return preparedStatement;
    }

}
