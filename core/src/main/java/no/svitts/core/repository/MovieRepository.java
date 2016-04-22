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
        return insertMovie(movie) && insertMovieGenreRelations(movie);
    }

    @Override
    public boolean insertMultiple(List<Movie> movies) {
        boolean allInserted = true;
        for (Movie movie : movies) {
            boolean inserted = insertMovie(movie);
            if (!inserted) {
                allInserted = false;
            }
        }
        return allInserted;
    }

    @Override
    public boolean updateSingle(Movie movie) {
        return updateMovie(movie);
    }

    @Override
    public boolean deleteSingle(Movie movie) {
        return deleteMovie(movie);
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

    private List<Movie> getAllMovies() {
        LOGGER.info("Getting all movies");
        try (PreparedStatement preparedStatement = getSelectAllMoviesPreparedStatement(dataSource.getConnection())) {
            return executeQuery(preparedStatement);
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
        LOGGER.info("Getting movies {}", movies.toString());
        try (PreparedStatement preparedStatement = getSelectMoviesPreparedStatement(dataSource.getConnection(), movies)) {
            return executeQuery(preparedStatement);
        } catch (SQLException e) {
            LOGGER.error("Could not get movies {}", movies.toString());
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
        LOGGER.info("Getting movie by ID {}", id);
        try (PreparedStatement preparedStatement = getSelectMoviePreparedStatement(dataSource.getConnection(), id)) {
            List<Movie> movies = executeQuery(preparedStatement);
            return movies.size() > 0 ? movies.get(0) : new UnknownMovie();
        } catch (SQLException e) {
            LOGGER.error("Could not get movie by ID {}", id, e);
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
        try (PreparedStatement preparedStatement = getSelectMoviePreparedStatement(dataSource.getConnection(), attributes)) {
            List<Movie> movies = executeQuery(preparedStatement);
            return movies.size() > 0 ? movies.get(0) : new UnknownMovie();
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
        LOGGER.info("Inserting movie {}", movie.toString());
        try (PreparedStatement preparedStatement = getInsertMoviePreparedStatement(dataSource.getConnection(), movie)) {
            return executeUpdate(preparedStatement);
        } catch (SQLException e) {
            LOGGER.error("Could not insert movie {}", movie.toString(), e);
            return false;
        }
    }

    private PreparedStatement getInsertMoviePreparedStatement(Connection connection, Movie movie) throws SQLException {
        String statement = "INSERT IGNORE INTO movie VALUES (?, ?, ?, ?, ?, ?, ?);";
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
        LOGGER.info("Inserting movie/genre relations for movie {}", movie.toString());
        try (PreparedStatement preparedStatement = getInsertMovieGenreRelations(dataSource.getConnection(), movie)) {
            return executeBatch(preparedStatement);
        } catch (SQLException e) {
            LOGGER.error("Could not insert movie/genre relations for movie {}", movie.toString(), e);
            return false;
        }
    }

    private PreparedStatement getInsertMovieGenreRelations(Connection connection, Movie movie) throws SQLException {
        String statement = "INSERT IGNORE INTO movie_genre VALUES (?, ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        for (Genre genre : movie.getGenres()) {
            int i = 1;
            preparedStatement.setString(i++, movie.getId());
            preparedStatement.setString(i, genre.getId());
            preparedStatement.addBatch();
        }
        return preparedStatement;
    }

    private boolean updateMovie(Movie movie) {
        LOGGER.info("Updating movie {}", movie.toString());
        try (PreparedStatement preparedStatement = getUpdateMoviePreparedStatement(dataSource.getConnection(), movie)) {
            return executeUpdate(preparedStatement);
        } catch (SQLException e) {
            LOGGER.error("Could not update movie {}", movie.toString(), e);
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

    private boolean deleteMovie(Movie movie) {
        LOGGER.info("Deleting movie {}", movie.toString());
        try (PreparedStatement preparedStatement = getDeleteMoviePreparedStatement(dataSource.getConnection(), movie)) {
            return executeUpdate(preparedStatement);
        } catch (SQLException e) {
            LOGGER.error("Could not delete movie {}", movie.toString(), e);
            return false;
        }
    }

    private PreparedStatement getDeleteMoviePreparedStatement(Connection connection, Movie movie) throws SQLException {
        String query = "DELETE FROM movie WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, movie.getId());
        return preparedStatement;
    }

    private List<Genre> getGenres(String genresString) throws SQLException {
        String[] genreStrings = genresString.split(",");
        List<Genre> genres = new ArrayList<>();
        for (String genreString : genreStrings) {
            genres.add(Genre.valueOf(genreString));
        }
        return genres;
    }

}
