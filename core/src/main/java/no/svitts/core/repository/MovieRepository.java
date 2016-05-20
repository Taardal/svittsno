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

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieRepository extends CoreRepository<Movie> {

    public static final String UNKNOWN_MOVIE_ID = "Unknown-Movie-ID";
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
        return getMovie(id);
    }

    @Override
    public boolean insert(Movie movie) {
        if (isRequiredFieldsValid(movie)) {
            return insertMovie(movie) && insertMovieGenreRelations(movie);
        } else {
            LOGGER.warn("Could not validate required fields when asked to insert movie [{}]", movie);
            return false;
        }
    }

    @Override
    public boolean update(Movie movie) {
        if (isRequiredFieldsValid(movie)) {
            return updateMovie(movie);
        } else {
            LOGGER.warn("Could not validate required fields when asked to update movie [{}]", movie);
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        return deleteMovie(id);
    }

    @Override
    public List<Movie> search(SearchCriteria searchCriteria) {
        if (searchCriteria.getKey() == SearchKey.NAME) {
            return searchMoviesByName(searchCriteria);
        } else if (searchCriteria.getKey() == SearchKey.GENRE) {
            return searchMoviesByGenre(searchCriteria);
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
                VideoFile videoFile = videoFileRepository.getById(resultSet.getString("video_file_id"));

                String imagesString = resultSet.getString("images");
                Map<ImageType, ImageFile> images = getImages(imagesString);

                movies.add(new Movie(id, name, imdbId, tagline, overview, runtime, releaseDate, genres));
            }
        } catch (SQLException e) {
            LOGGER.error("Could not get movie(s) from result set [{}]", resultSet.toString(), e);
        }
        LOGGER.info("Got movie(s) [{}]", movies.toString());
        return movies;
    }

    private Map<ImageType, ImageFile> getImages(String imagesString) {
        if (imagesString != null && !imagesString.isEmpty()) {
            Map<ImageType, ImageFile> images = new HashMap<>();
            String[] split = imagesString.split(",");
            for (String imageId : split) {
                ImageFile imageFile = imageFileRepository.getById(imageId);
                if (imageFile.getImageType() == ImageType.POSTER) {
                    images.put(ImageType.POSTER, imageFile);
                } else if (imageFile.getImageType() == ImageType.BACKDROP) {
                    images.put(ImageType.BACKDROP, imageFile);
                } else {
                    LOGGER.warn("Could not validate image type for image file [{}]", imageFile);
                }
            }
            return images;
        } else {
            LOGGER.warn("Image string was null or empty");
            return new HashMap<>();
        }
    }

    @Override
    protected boolean isRequiredFieldsValid(Movie movie) {
        return movie.getId() != null && !movie.getId().isEmpty()
                && movie.getName() != null && !movie.getName().isEmpty() && !movie.getName().equals("null")
                && movie.getGenres() != null && !movie.getGenres().isEmpty();
    }

    private Movie getMovie(String id) {
        LOGGER.info("Getting movie by ID [{}]", id);
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = getSelectMoviePreparedStatement(connection, id)) {
                List<Movie> movies = executeQuery(preparedStatement);
                return movies.isEmpty() ? getUnknownMovie() : movies.get(0);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not get movie by ID [{}]", id, e);
            return getUnknownMovie();
        }
    }

    private PreparedStatement getSelectMoviePreparedStatement(Connection connection, String id) throws SQLException {
        String sql = "SELECT movie.*, GROUP_CONCAT(DISTINCT genre.name) AS genres, video_file.id AS video_file_id, GROUP_CONCAT(DISTINCT image_file.id) AS images FROM movie " +
                "JOIN movie_genre ON movie.id = movie_genre.movie_id JOIN genre ON genre.id = movie_genre.genre_id " +
                "JOIN movie_video_file ON movie.id = movie_video_file.movie_id JOIN video_file ON video_file.id = movie_video_file.video_file_id " +
                "JOIN movie_image_file ON movie.id = movie_image_file.movie_id JOIN image_file ON image_file.id = movie_image_file.image_file_id " +
                "WHERE movie.id = ? GROUP BY movie.id;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, id);
        return preparedStatement;
    }

    private Movie getUnknownMovie() {
        return new Movie(UNKNOWN_MOVIE_ID, "Unknown", "Unknown", "Unknown", "Unknown", 0, new KeyDate(), new ArrayList<>());
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
            try (PreparedStatement preparedStatement = getInsertMovieGenreRelations(connection, movie)) {
                return executeBatch(preparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not insert movie/genre relations for movie [{}]", movie.toString(), e);
            return false;
        }
    }

    private PreparedStatement getInsertMovieGenreRelations(Connection connection, Movie movie) throws SQLException {
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

    private List<Movie> searchMoviesByName(SearchCriteria searchCriteria) {
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

    private List<Movie> searchMoviesByGenre(SearchCriteria searchCriteria) {
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

}
