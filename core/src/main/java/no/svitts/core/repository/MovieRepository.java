package no.svitts.core.repository;

import no.svitts.core.criteria.SearchCriteria;
import no.svitts.core.criteria.SearchKey;
import no.svitts.core.datasource.DataSource;
import no.svitts.core.date.KeyDate;
import no.svitts.core.file.ImageFile;
import no.svitts.core.file.ImageType;
import no.svitts.core.file.VideoFile;
import no.svitts.core.movie.Genre;
import no.svitts.core.movie.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieRepository extends CoreRepository<Movie> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieRepository.class);

    private Repository<VideoFile> videoFileRepository;
    private Repository<ImageFile> imageFileRepository;

    public MovieRepository(DataSource dataSource, Repository<VideoFile> videoFileRepository, Repository<ImageFile> imageFileRepository) {
        super(dataSource);
        this.videoFileRepository = videoFileRepository;
        this.imageFileRepository = imageFileRepository;
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
    public String insert(Movie movie) {
        insertMovie(movie);
        insertMovieGenreRelations(movie);
        videoFileRepository.insert(movie.getVideoFile());
        insertMovieVideoFileRelations(movie);
        insertImageFiles(movie);
        insertMovieImageFilesRelations(movie);
        return movie.getId();
    }

    @Override
    public boolean update(Movie movie) {
        return updateMovie(movie);
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

    @Override
    public boolean delete(String id) {
        return deleteMovie(id);
    }

    @Override
    public List<Movie> search(SearchCriteria searchCriteria) {
        if (searchCriteria.getKey() == SearchKey.NAME) {
            return selectMoviesByName(searchCriteria);
        } else if (searchCriteria.getKey() == SearchKey.GENRE) {
            return selectMoviesByGenre(searchCriteria);
        } else {
            LOGGER.warn("Could not resolve search criteria [{}] when asked to search movies", searchCriteria);
            return new ArrayList<>();
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
                VideoFile videoFile = videoFileRepository.getById(resultSet.getString("video_file"));
                Map<ImageType, ImageFile> imageFiles = getImageFiles(resultSet.getString("image_files"));
                movies.add(new Movie(id, name, imdbId, tagline, overview, runtime, releaseDate, genres, videoFile, imageFiles));
            }
        } catch (SQLException e) {
            LOGGER.error("Could not get movie(s) from result set [{}]", resultSet.toString(), e);
        }
        LOGGER.info("Got movie(s) [{}]", movies.toString());
        return movies;
    }

    @Override
    protected boolean isRequiredFieldsValid(Movie movie) {
        return movie.getId() != null && !movie.getId().isEmpty()
                && movie.getName() != null && !movie.getName().isEmpty() && !movie.getName().equals("null")
                && movie.getGenres() != null && !movie.getGenres().isEmpty();
    }

    private Movie getMovie(String id, Connection connection) {
        try (PreparedStatement preparedStatement = getSelectMoviePreparedStatement(connection, id)) {
            return getMovie(executeQuery(preparedStatement));
        } catch (SQLException e) {
            throw new InternalServerErrorException("Could not prepare sql statement when asked to get movie by ID [" + id + "]");
        }
    }

    private Movie getMovie(List<Movie> movies) {
        if (!movies.isEmpty()) {
            return movies.get(0);
        } else {
            throw new NotFoundException("Could not find requested movie in the database.");
        }
    }

    private PreparedStatement getSelectMoviePreparedStatement(Connection connection, String id) throws SQLException {
        String sql = "SELECT movie.*, GROUP_CONCAT(DISTINCT genre.name) AS genres, video_file.id AS video_file_id, GROUP_CONCAT(DISTINCT image_file.id) AS image_ids FROM movie JOIN movie_genre ON movie.id = movie_genre.movie_id JOIN genre ON genre.id = movie_genre.genre_id JOIN movie_video_file ON movie.id = movie_video_file.movie_id JOIN video_file ON video_file.id = movie_video_file.video_file_id JOIN movie_image_file ON movie.id = movie_image_file.movie_id JOIN image_file ON image_file.id = movie_image_file.image_file_id WHERE movie.id = ? GROUP BY movie.id;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, id);
        return preparedStatement;
    }

    private boolean insertMovie(Movie movie) {
        LOGGER.info("Inserting movie [{}]", movie.toString());
        try (Connection connection = dataSource.getConnection()) {
            return insertMovie(movie, connection);
        } catch (SQLException e) {
            String errorMessage = "Could not get connection from data source when asked to insert movie [" + movie.toString() + "]";
            LOGGER.error(errorMessage, e);
            throw new InternalServerErrorException(errorMessage, e);
        }
    }

    private boolean insertMovie(Movie movie, Connection connection) {
        try (PreparedStatement preparedStatement = getInsertMoviePreparedStatement(connection, movie)) {
            return executeUpdate(preparedStatement);
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

    private boolean insertMovieGenreRelations(Movie movie) {
        LOGGER.info("Inserting movie/genre relations for movie [{}]", movie.toString());
        try (Connection connection = dataSource.getConnection()) {
            return insertMovieGenreRelations(movie, connection);
        } catch (SQLException e) {
            String errorMessage = "Could not get connection from data source when asked to insert movie/genre relations for movie [" + movie.toString() + "]";
            LOGGER.error(errorMessage, e);
            throw new InternalServerErrorException(errorMessage, e);
        }
    }

    private boolean insertMovieGenreRelations(Movie movie, Connection connection) {
        try (PreparedStatement preparedStatement = getInsertMovieGenreRelationsPreparedStatement(connection, movie)) {
            return executeBatch(preparedStatement);
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

    private boolean insertMovieVideoFileRelations(Movie movie) {
        LOGGER.info("Inserting movie/video file relations for movie [{}]", movie.toString());
        try (Connection connection = dataSource.getConnection()) {
            return insertMovieVideoFileRelations(movie, connection);
        } catch (SQLException e) {
            String errorMessage = "Could not get connection from data source when asked to insert movie/video file relations for movie [" + movie.toString() + "]";
            LOGGER.error(errorMessage, e);
            throw new InternalServerErrorException(errorMessage, e);
        }
    }

    private boolean insertMovieVideoFileRelations(Movie movie, Connection connection) {
        try (PreparedStatement preparedStatement = getInsertMovieVideoFileRelationsPreparedStatement(movie, connection)) {
            return executeUpdate(preparedStatement);
        } catch (SQLException e) {
            String errorMessage = "Could not prepare sql statement when asked to insert movie/video file relations for movie [" + movie.toString() + "]";
            LOGGER.error(errorMessage, e);
            throw new InternalServerErrorException(errorMessage, e);
        }
    }

    private PreparedStatement getInsertMovieVideoFileRelationsPreparedStatement(Movie movie, Connection connection) throws SQLException {
        String sql = "INSERT INTO movie_video_file (movie_id, video_file_id) VALUES (?, ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        int i = 1;
        preparedStatement.setString(i++, movie.getId());
        preparedStatement.setString(i, movie.getVideoFile().getId());
        return preparedStatement;
    }

    private void insertImageFiles(Movie movie) {
        for (ImageFile imageFile : movie.getImageFiles().values()) {
            imageFileRepository.insert(imageFile);
        }
    }

    private boolean insertMovieImageFilesRelations(Movie movie) {
        LOGGER.info("Inserting movie/image file relations for movie [{}]", movie.toString());
        try (Connection connection = dataSource.getConnection()) {
            return insertMovieImageFileRelations(movie, connection);
        } catch (SQLException e) {
            String errorMessage = "Could not get connection from data source when asked to insert movie/image file relations for movie [" + movie.toString() + "]";
            LOGGER.error(errorMessage, e);
            throw new InternalServerErrorException(errorMessage, e);
        }
    }

    private boolean insertMovieImageFileRelations(Movie movie, Connection connection) {
        try (PreparedStatement preparedStatement = getInsertMovieImageFilesRelations(connection, movie)) {
            return executeBatch(preparedStatement);
        } catch (SQLException e) {
            String errorMessage = "Could not prepare sql statement when asked to insert movie/image file relations for movie [" + movie.toString() + "]";
            LOGGER.error(errorMessage, e);
            throw new InternalServerErrorException(errorMessage, e);
        }
    }

    private PreparedStatement getInsertMovieImageFilesRelations(Connection connection, Movie movie) throws SQLException {
        String sql = "INSERT INTO movie_image_file (movie_id, image_file_id) VALUES (?, ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (ImageFile imageFile : movie.getImageFiles().values()) {
            int i = 1;
            preparedStatement.setString(i++, movie.getId());
            preparedStatement.setString(i, imageFile.getId());
            preparedStatement.addBatch();
        }
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
        String sql = "DELETE FROM movie WHERE movie.id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, id);
        return preparedStatement;
    }

    private List<Movie> selectMoviesByName(SearchCriteria searchCriteria) {
        LOGGER.info("Searching movies by name [{}]", searchCriteria.getValue());
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = getSelectMoviesByNamePreparedStatement(connection, searchCriteria)) {
                return executeQuery(preparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not search movies by name", e);
            return new ArrayList<>();
        }
    }

    private PreparedStatement getSelectMoviesByNamePreparedStatement(Connection connection, SearchCriteria searchCriteria) throws SQLException {
        String sql = "SELECT movie.*, GROUP_CONCAT(genre.name) AS genres FROM movie JOIN movie_genre ON movie.id = movie_genre.movie_id " +
                "JOIN genre ON genre.id = movie_genre.genre_id WHERE movie.name LIKE ? GROUP BY movie.id LIMIT ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        int i = 1;
        preparedStatement.setString(i++, "%" + searchCriteria.getValue() + "%");
        preparedStatement.setInt(i, searchCriteria.getLimit());
        return preparedStatement;
    }

    private List<Movie> selectMoviesByGenre(SearchCriteria searchCriteria) {
        LOGGER.info("Searching movies by genre [{}]", searchCriteria.getValue());
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = getSelectMoviesByGenrePreparedStatement(connection, searchCriteria)) {
                return executeQuery(preparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not search movies by genre", e);
            return new ArrayList<>();
        }
    }

    private PreparedStatement getSelectMoviesByGenrePreparedStatement(Connection connection, SearchCriteria searchCriteria) throws SQLException {
        String sql = "SELECT movie.*, GROUP_CONCAT(genre.name) AS genres FROM movie JOIN movie_genre ON movie.id = movie_genre.movie_id " +
                "JOIN genre ON genre.id = movie_genre.genre_id WHERE genre.name LIKE ? GROUP BY movie.id LIMIT ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        int i = 1;
        preparedStatement.setString(i++, "%" + searchCriteria.getValue() + "%");
        preparedStatement.setInt(i, searchCriteria.getLimit());
        return preparedStatement;
    }

    private Map<ImageType, ImageFile> getImageFiles(String imagesString) {
        if (imagesString != null && !imagesString.isEmpty()) {
            return getImageFiles(imagesString.split(","));
        } else {
            LOGGER.warn("Could not find any image files");
            return new HashMap<>();
        }
    }

    private Map<ImageType, ImageFile> getImageFiles(String[] imageFileIds) {
        Map<ImageType, ImageFile> imageFiles = new HashMap<>();
        for (String imageFileId : imageFileIds) {
            ImageFile imageFile = imageFileRepository.getById(imageFileId);
            imageFiles.put(imageFile.getImageType(), imageFile);
        }
        return imageFiles;
    }

}
