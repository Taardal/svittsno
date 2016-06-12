package no.svitts.core.repository;

import no.svitts.core.datasource.DataSource;
import no.svitts.core.date.ReleaseDate;
import no.svitts.core.exception.RepositoryException;
import no.svitts.core.genre.Genre;
import no.svitts.core.movie.Movie;
import no.svitts.core.search.Criteria;
import no.svitts.core.search.CriteriaKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieRepository extends CoreRepository<Movie> implements Repository<Movie> {

    static final String ID = "id";
    static final String NAME = "name";
    static final String IMDB_ID = "imdb_id";
    static final String TAGLINE = "tagline";
    static final String OVERVIEW = "overview";
    static final String RUNTIME = "runtime";
    static final String RELEASE_DATE = "release_date";
    static final String GENRES = "genres";
    static final String VIDEO_FILE_PATH = "video_file_path";
    static final String POSTER_IMAGE_FILE_PATH = "poster_image_file_path";
    static final String BACKDROP_IMAGE_FILE_PATH = "backdrop_image_file_path";
    private static final Logger LOGGER = LoggerFactory.getLogger(MovieRepository.class);

    public MovieRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Movie getSingle(String id) {
        LOGGER.info("Getting movie by ID [{}]", id);
        try (Connection connection = dataSource.getConnection()) {
            return getMovie(id, connection);
        } catch (SQLException e) {
            String errorMessage = "Could not connect to data source to get single movie by ID [" + id + "]";
            LOGGER.error(errorMessage, e);
            throw new RepositoryException(errorMessage, e);
        }
    }

    @Override
    public List<Movie> getMultiple(Criteria criteria) {
        LOGGER.info("Getting multiple movies [{}]", criteria.toString());
        try (Connection connection = dataSource.getConnection()) {
            return getMultiple(criteria, connection);
        } catch (SQLException e) {
            String errorMessage = "Could not get connection from data source when asked to get multiple movies by search criteria";
            LOGGER.error(errorMessage + " [" + criteria.toString() + "]", e);
            throw new RepositoryException(errorMessage, e);
        }
    }

    @Override
    public String insertSingle(Movie movie)  {
        LOGGER.info("Inserting movie [{}]", movie.toString());
        try (Connection connection = dataSource.getConnection()) {
            insertMovie(movie, connection);
            if (!movie.getGenres().isEmpty()) {
                insertMovieGenreRelations(movie, connection);
            }
            return movie.getId();
        } catch (SQLException e) {
            String errorMessage = "Could not get connection from data source when asked to insert single movie";
            LOGGER.error(errorMessage + " [" + movie.toString() + "]", e);
            throw new RepositoryException(errorMessage, e);
        }
    }

    @Override
    public void updateSingle(Movie movie)  {
        LOGGER.info("Updating movie [{}]", movie.toString());
        try (Connection connection = dataSource.getConnection()) {
            updateMovie(movie, connection);
            updateMovieGenreRelations(movie, connection);
        } catch (SQLException e) {
            String errorMessage = "Could not get connection from data source when asked to update single movie";
            LOGGER.error(errorMessage + " [" + movie.toString() + "]", e);
            throw new RepositoryException(errorMessage, e);
        }
    }

    @Override
    public void deleteSingle(String id)  {
        LOGGER.info("Deleting movie with ID [{}]", id);
        try (Connection connection = dataSource.getConnection()) {
            deleteMovie(id, connection);
        } catch (SQLException e) {
            String errorMessage = "Could not get connection from data source when asked to delete single movie with ID [" + id + "]";
            LOGGER.error(errorMessage, e);
            throw new RepositoryException(errorMessage, e);
        }

    }

    @Override
    protected List<Movie> getResults(ResultSet resultSet) {
        List<Movie> movies = new ArrayList<>();
        try {
            while (resultSet.next()) {
                String id = resultSet.getString(ID);
                String name = resultSet.getString(NAME);
                String imdbId = resultSet.getString(IMDB_ID);
                String tagline = resultSet.getString(TAGLINE);
                String overview = resultSet.getString(OVERVIEW);
                int runtime = resultSet.getInt(RUNTIME);
                ReleaseDate releaseDate = new ReleaseDate(resultSet.getDate(RELEASE_DATE));
                List<Genre> genres = Genre.fromString(resultSet.getString(GENRES));
                File videoFile = new File(resultSet.getString(VIDEO_FILE_PATH));
                File posterImageFile = new File(resultSet.getString(POSTER_IMAGE_FILE_PATH));
                File backdropImageFile = new File(resultSet.getString(BACKDROP_IMAGE_FILE_PATH));
                movies.add(new Movie(id, name, imdbId, tagline, overview, runtime, releaseDate, genres, videoFile, posterImageFile, backdropImageFile));
            }
        } catch (SQLException e) {
            LOGGER.error("Could not get movie(s) from result set [{}]", resultSet.toString(), e);
        }
        return movies;
    }

    Movie getMovie(String id, Connection connection) {
        try (PreparedStatement preparedStatement = getSelectMoviePreparedStatement(connection, id)) {
            return getMovie(executeQuery(preparedStatement));
        } catch (SQLException e) {
            String errorMessage = "Could not prepare sql statement to get single movie by ID [" + id + "]";
            throw new RepositoryException(errorMessage, e);
        }
    }

    List<Movie> getMultiple(Criteria criteria, Connection connection) {
        try (PreparedStatement preparedStatement = getSelectMoviesPreparedStatement(criteria, connection)) {
            return executeQuery(preparedStatement);
        } catch (SQLException e) {
            String errorMessage = "Could not prepare sql statement to get multiple movies by criteria";
            LOGGER.error(errorMessage + " [" + criteria.toString() + "]");
            throw new RepositoryException(errorMessage, e);
        }
    }

    private PreparedStatement getSelectMoviesPreparedStatement(Criteria criteria, Connection connection) throws SQLException {
        String sql = "SELECT movie.name, GROUP_CONCAT(DISTINCT genre.name) AS genres FROM movie LEFT JOIN movie_genre ON movie.id = movie_genre.movie_id LEFT JOIN genre ON genre.id = movie_genre.genre_id WHERE movie.name LIKE ? GROUP BY movie.id HAVING genres LIKE ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        int i = 1;
        preparedStatement.setString(i++, "%" + criteria.getCriteria(CriteriaKey.NAME) + "%");
        preparedStatement.setString(i++, "%" + criteria.getCriteria(CriteriaKey.GENRE) + "%");
        preparedStatement.setInt(i, criteria.getLimit());
        return preparedStatement;
    }

    void insertMovie(Movie movie, Connection connection)  {
        try (PreparedStatement preparedStatement = getInsertMoviePreparedStatement(connection, movie)) {
            executeUpdate(preparedStatement);
        } catch (SQLException e) {
            String errorMessage = "Could not prepare sql statement when asked to insert single movie";
            LOGGER.error(errorMessage + " [" + movie.toString() + "]", e);
            throw new RepositoryException(errorMessage, e);
        }
    }

    void insertMovieGenreRelations(Movie movie, Connection connection)  {
        try (PreparedStatement preparedStatement = getInsertMovieGenreRelationsPreparedStatement(connection, movie)) {
            executeBatch(preparedStatement);
        } catch (SQLException e) {
            String errorMessage = "Could not prepare sql statement when asked to insert single movie/genre relations for movie";
            LOGGER.error(errorMessage + " [" + movie.toString() + "]", e);
            throw new RepositoryException(errorMessage, e);
        }
    }

    void updateMovie(Movie movie, Connection connection)  {
        try (PreparedStatement preparedStatement = getUpdateMoviePreparedStatement(connection, movie)) {
            executeUpdate(preparedStatement);
        } catch (SQLException e) {
            String errorMessage = "Could not prepare sql statement when asked to update single movie";
            LOGGER.error(errorMessage + " [" + movie.toString() + "]", e);
            throw new RepositoryException(errorMessage, e);
        }
    }

    void deleteMovieGenreRelations(Movie movie, Connection connection) {
        try (PreparedStatement preparedStatement = getDeleteMovieGenreRelationsPreparedStatement(connection, movie)) {
            executeUpdate(preparedStatement);
        } catch (SQLException e) {
            String errorMessage = "Could not prepare sql statement when asked to update single movie";
            LOGGER.error(errorMessage + " [" + movie.toString() + "]", e);
            throw new RepositoryException(errorMessage, e);
        }
    }

    void deleteMovie(String id, Connection connection)  {
        try (PreparedStatement preparedStatement = getDeleteMoviePreparedStatement(connection, id)) {
            executeUpdate(preparedStatement);
        } catch (SQLException e) {
            String errorMessage = "Could not prepare sql statement when asked to delete single movie with ID [" + id + "]";
            LOGGER.error(errorMessage, e);
            throw new RepositoryException(errorMessage, e);
        }
    }

    private PreparedStatement getSelectMoviePreparedStatement(Connection connection, String id) throws SQLException {
        String sql = "SELECT movie.*, GROUP_CONCAT(DISTINCT genre.name) AS genres, video_file.id AS video_file_id, GROUP_CONCAT(DISTINCT image_file.id) AS image_ids FROM movie JOIN movie_genre ON movie.id = movie_genre.movie_id JOIN genre ON genre.id = movie_genre.genre_id JOIN movie_video_file ON movie.id = movie_video_file.movie_id JOIN video_file ON video_file.id = movie_video_file.video_file_id JOIN movie_image_file ON movie.id = movie_image_file.movie_id JOIN image_file ON image_file.id = movie_image_file.image_file_id WHERE movie.id = ? GROUP BY movie.id;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, id);
        return preparedStatement;
    }

    private Movie getMovie(List<Movie> movies) {
        if (!movies.isEmpty()) {
            return movies.get(0);
        } else {
            return null;
        }
    }

    private PreparedStatement getInsertMoviePreparedStatement(Connection connection, Movie movie) throws SQLException {
        String sql = "INSERT INTO movie (id, name, imdb_id, tagline, overview, runtime, release_date) VALUES (?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        int i = 1;
        preparedStatement.setString(i++, movie.getId());
        preparedStatement.setString(i++, movie.getName());
        preparedStatement.setString(i++, movie.getImdbId());
        preparedStatement.setString(i++, movie.getTagline());
        preparedStatement.setString(i++, movie.getOverview());
        preparedStatement.setInt(i++, movie.getRuntime());
        preparedStatement.setDate(i, getDate(movie.getReleaseDate()));
        return preparedStatement;
    }

    private Date getDate(ReleaseDate releaseDate) {
        return releaseDate != null ? releaseDate.toSqlDate() : null;
    }

    private PreparedStatement getInsertMovieGenreRelationsPreparedStatement(Connection connection, Movie movie) throws SQLException {
        String sql = "INSERT INTO movie_genre (movie_id, genre_id) VALUES (?, ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (Genre genre : movie.getGenres()) {
            int i = 1;
            preparedStatement.setString(i++, movie.getId());
            preparedStatement.setInt(i, genre.getId());
            preparedStatement.addBatch();
        }
        return preparedStatement;
    }

    private PreparedStatement getUpdateMoviePreparedStatement(Connection connection, Movie movie) throws SQLException {
        String sql = "UPDATE movie SET imdb_id = ?, name = ?, tagline = ?, overview = ?, runtime = ?, release_date = ? WHERE id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        int i = 1;
        preparedStatement.setString(i++, movie.getImdbId());
        preparedStatement.setString(i++, movie.getName());
        preparedStatement.setString(i++, movie.getTagline());
        preparedStatement.setString(i++, movie.getOverview());
        preparedStatement.setInt(i++, movie.getRuntime());
        preparedStatement.setDate(i++, getDate(movie.getReleaseDate()));
        preparedStatement.setString(i, movie.getId());
        return preparedStatement;
    }

    private void updateMovieGenreRelations(Movie movie, Connection connection)  {
        deleteMovieGenreRelations(movie, connection);
        if (!movie.getGenres().isEmpty()) {
            insertMovieGenreRelations(movie, connection);
        }
    }

    private PreparedStatement getDeleteMovieGenreRelationsPreparedStatement(Connection connection, Movie movie) throws SQLException {
        String sql = "DELETE FROM movie_genre WHERE movie_id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, movie.getId());
        return preparedStatement;
    }

    private PreparedStatement getDeleteMoviePreparedStatement(Connection connection, String id) throws SQLException {
        String sql = "DELETE FROM movie WHERE movie.id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, id);
        return preparedStatement;
    }


}
