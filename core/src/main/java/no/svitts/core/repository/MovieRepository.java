package no.svitts.core.repository;

import no.svitts.core.criteria.SearchCriteria;
import no.svitts.core.criteria.SearchKey;
import no.svitts.core.datasource.DataSource;
import no.svitts.core.date.KeyDate;
import no.svitts.core.movie.Genre;
import no.svitts.core.movie.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieRepository extends CoreRepository<Movie> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieRepository.class);

    public MovieRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Movie getById(String id) {
        LOGGER.info("Getting movie by ID [{}]", id);
        try (Connection connection = dataSource.getConnection()) {
            return getMovie(id, connection);
        } catch (SQLException e) {
            String errorMessage = "Could not get connection from data source when asked to get movie by ID [" + id + "]";
            LOGGER.error(errorMessage, e);
            throw new InternalServerErrorException(errorMessage, e);
        }
    }

    @Override
    public List<Movie> getMultiple(SearchCriteria searchCriteria) {
        LOGGER.info("Getting multiple movies by search criteria [{}]", searchCriteria.toString());
        try (Connection connection = dataSource.getConnection()) {
            return getMultiple(searchCriteria, connection);
        } catch (SQLException e) {
            String errorMessage = "Could not get connection from data source when asked to get multiple movies by search criteria [" + searchCriteria.toString() + "]";
            LOGGER.error(errorMessage, e);
            throw new InternalServerErrorException(errorMessage, e);
        }

    }

    @Override
    public String insert(Movie movie)  {
        LOGGER.info("Inserting movie [{}]", movie.toString());
        try (Connection connection = dataSource.getConnection()) {
            insertMovie(movie, connection);
            if (movie.getGenres() != null && !movie.getGenres().isEmpty()) {
                insertMovieGenreRelations(movie, connection);
            }
            return movie.getId();
        } catch (SQLException e) {
            String errorMessage = "Could not get connection from data source when asked to insert movie [" + movie.toString() + "]";
            LOGGER.error(errorMessage, e);
            throw new InternalServerErrorException(errorMessage, e);
        }
    }

    @Override
    public void update(Movie movie)  {
        LOGGER.info("Updating movie [{}]", movie.toString());
        try (Connection connection = dataSource.getConnection()) {
            updateMovie(movie, connection);
            updateMovieGenreRelations(movie, connection);
        } catch (SQLException e) {
            String errorMessage = "Could not get connection from data source when asked to update movie [" + movie.toString() + "]";
            LOGGER.error(errorMessage, e);
            throw new InternalServerErrorException(errorMessage, e);
        }
    }

    private void updateMovie(Movie movie, Connection connection)  {
        try (PreparedStatement preparedStatement = getUpdateMoviePreparedStatement(connection, movie)) {
            executeUpdate(preparedStatement);
        } catch (SQLException e) {
            String errorMessage = "Could not prepare sql statement when asked to update movie [" + movie.toString() + "]";
            LOGGER.error(errorMessage, e);
            throw new InternalServerErrorException(errorMessage, e);
        }
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
        try (PreparedStatement preparedStatement = getUpdateMovieGenreRelationsPreparedStatement(connection, movie)) {
            executeUpdate(preparedStatement);
        } catch (SQLException e) {
            String errorMessage = "Could not prepare sql statement when asked to update movie [" + movie.toString() + "]";
            LOGGER.error(errorMessage, e);
            throw new InternalServerErrorException(errorMessage, e);
        }
    }

    private PreparedStatement getUpdateMovieGenreRelationsPreparedStatement(Connection connection, Movie movie) {
        return null;
    }

    @Override
    public void delete(String id)  {
        LOGGER.info("Deleting movie with ID [{}]", id);
        try (Connection connection = dataSource.getConnection()) {
            deleteMovie(id, connection);
        } catch (SQLException e) {
            String errorMessage = "Could not get connection from data source when asked to delete movie with ID [" + id + "]";
            LOGGER.error(errorMessage, e);
            throw new InternalServerErrorException(errorMessage, e);
        }

    }

    @Override
    protected List<Movie> getResults(ResultSet resultSet) {
        List<Movie> movies = new ArrayList<>();
        try {
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String imdbId = resultSet.getString("imdb_id");
                String tagline = resultSet.getString("tagline");
                String overview = resultSet.getString("overview");
                int runtime = resultSet.getInt("runtime");
                KeyDate releaseDate = new KeyDate(resultSet.getDate("release_date"));
                List<Genre> genres = Genre.fromString(resultSet.getString("genres"));
                File videoFile = new File(resultSet.getString("video_file_path"));
                File posterImageFile = new File(resultSet.getString("poster_image_file_path"));
                File backdropImageFile = new File(resultSet.getString("backdrop_image_file_path"));
                movies.add(new Movie(id, name, imdbId, tagline, overview, runtime, releaseDate, genres, videoFile, posterImageFile, backdropImageFile));
            }
        } catch (SQLException e) {
            LOGGER.error("Could not get movie(s) from result set [{}]", resultSet.toString(), e);
        }
        return movies;
    }

    private Movie getMovie(String id, Connection connection) {
        try (PreparedStatement preparedStatement = getSelectMoviePreparedStatement(connection, id)) {
            return getMovie(executeQuery(preparedStatement));
        } catch (SQLException e) {
            throw new InternalServerErrorException("Could not prepare sql statement when asked to get movie by ID [" + id + "]");
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
            String errorMessage = "Could not find requested movie in the database.";
            LOGGER.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }
    }

    private List<Movie> getMultiple(SearchCriteria searchCriteria, Connection connection) {
        if (searchCriteria.getKey() == SearchKey.NAME) {
            return getMoviesByName(searchCriteria, connection);
        } else if (searchCriteria.getKey() == SearchKey.GENRE) {
            return getMoviesByGenre(searchCriteria, connection);
        } else {
            String errorMessage = "Could not resolve search key [" + searchCriteria.getKey() + "] when asked to get multiple movies";
            LOGGER.error(errorMessage);
            throw new InternalServerErrorException(errorMessage);
        }
    }

    private List<Movie> getMoviesByName(SearchCriteria searchCriteria, Connection connection) {
        try (PreparedStatement preparedStatement = getSelectMoviesByNamePreparedStatement(searchCriteria, connection)) {
            return executeQuery(preparedStatement);
        } catch (SQLException e) {
            String errorMessage = "Could not prepare sql statement when asked to get movies by name [" + searchCriteria.getValue() + "]";
            LOGGER.error(errorMessage, e);
            throw new InternalServerErrorException(errorMessage, e);
        }
    }

    private PreparedStatement getSelectMoviesByNamePreparedStatement(SearchCriteria searchCriteria, Connection connection) throws SQLException {
        String sql = "SELECT movie.*, GROUP_CONCAT(genre.name) AS genres FROM movie JOIN movie_genre ON movie.id = movie_genre.movie_id " +
                "JOIN genre ON genre.id = movie_genre.genre_id WHERE movie.name LIKE ? GROUP BY movie.id LIMIT ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        int i = 1;
        preparedStatement.setString(i++, "%" + searchCriteria.getValue() + "%");
        preparedStatement.setInt(i, searchCriteria.getLimit());
        return preparedStatement;
    }

    private List<Movie> getMoviesByGenre(SearchCriteria searchCriteria, Connection connection) {
        try (PreparedStatement preparedStatement = getSelectMoviesByGenrePreparedStatement(searchCriteria, connection)) {
            return executeQuery(preparedStatement);
        } catch (SQLException e) {
            String errorMessage = "Could not prepare sql statement when asked to get movies by genre [" + searchCriteria.getValue() + "]";
            LOGGER.error(errorMessage, e);
            throw new InternalServerErrorException(errorMessage, e);
        }
    }

    private PreparedStatement getSelectMoviesByGenrePreparedStatement(SearchCriteria searchCriteria, Connection connection) throws SQLException {
        String sql = "SELECT movie.*, GROUP_CONCAT(genre.name) AS genres FROM movie JOIN movie_genre ON movie.id = movie_genre.movie_id " +
                "JOIN genre ON genre.id = movie_genre.genre_id WHERE genre.name LIKE ? GROUP BY movie.id LIMIT ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        int i = 1;
        preparedStatement.setString(i++, "%" + searchCriteria.getValue() + "%");
        preparedStatement.setInt(i, searchCriteria.getLimit());
        return preparedStatement;
    }

    private void insertMovie(Movie movie, Connection connection)  {
        try (PreparedStatement preparedStatement = getInsertMoviePreparedStatement(connection, movie)) {
            executeUpdate(preparedStatement);
        } catch (SQLException e) {
            String errorMessage = "Could not prepare sql statement when asked to insert movie [" + movie.toString() + "]";
            LOGGER.error(errorMessage, e);
            throw new InternalServerErrorException(errorMessage, e);
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

    private Date getDate(KeyDate releaseDate) {
        return releaseDate != null ? releaseDate.toSqlDate() : null;
    }

    private void insertMovieGenreRelations(Movie movie, Connection connection)  {
        try (PreparedStatement preparedStatement = getInsertMovieGenreRelationsPreparedStatement(connection, movie)) {
            executeBatch(preparedStatement);
        } catch (SQLException e) {
            String errorMessage = "Could not prepare sql statement when asked to insert movie/genre relations for movie [" + movie.toString() + "]";
            LOGGER.error(errorMessage, e);
            throw new InternalServerErrorException(errorMessage, e);
        }
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

    private void deleteMovie(String id, Connection connection)  {
        try (PreparedStatement preparedStatement = getDeleteMoviePreparedStatement(connection, id)) {
            executeUpdate(preparedStatement);
        } catch (SQLException e) {
            String errorMessage = "Could not prepare sql statement when asked to delete movie with ID [" + id + "]";
            LOGGER.error(errorMessage, e);
            throw new InternalServerErrorException(errorMessage, e);
        }
    }

    private PreparedStatement getDeleteMoviePreparedStatement(Connection connection, String id) throws SQLException {
        String sql = "DELETE FROM movie WHERE movie.id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, id);
        return preparedStatement;
    }


}
