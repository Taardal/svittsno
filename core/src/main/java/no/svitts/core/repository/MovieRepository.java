package no.svitts.core.repository;

import no.svitts.core.database.DataSource;
import no.svitts.core.movie.Movie;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MovieRepository extends SqlRepository<Movie> implements Repository<Movie> {

    private static final Logger LOGGER = Logger.getLogger(MovieRepository.class.getName());

    public MovieRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Movie> getAll() {
        return executeQuery("SELECT * FROM movie;");
//        return executeQuery("SELECT movie.*, genre.name AS genre FROM movie " +
//                "JOIN movie_genre ON movie.id = movie_genre.movie_id " +
//                "JOIN genre ON genre.id = movie_genre.genre_id;");
    }

    @Override
    public Movie getById(int id) {
        List<Movie> movies = executeQuery("SELECT movie.*, genre.name AS genre FROM movie " +
                "JOIN movie_genre ON movie.id = movie_genre.movie_id " +
                "JOIN genre ON genre.id = movie_genre.genre_id " +
                "WHERE movie.id = " + id + ";");
        return movies.get(0);
    }

    @Override
    public int updateSingle(int id) {

        return id;
    }

    @Override
    public int deleteSingle(int id) {

        return id;
    }

    public void create() {

    }

    @Override
    protected List<Movie> getResults(ResultSet resultSet) throws SQLException {
        List<Movie> movies = new ArrayList<>();
        while (resultSet.next()) {
            Movie movie = new Movie(resultSet.getInt("id"));
            movie.setName(resultSet.getString("name"));
            movie.setTagline(resultSet.getString("tagline"));
            movie.setOverview(resultSet.getString("overview"));
            movie.setImdbId(resultSet.getString("imdb_id"));
            movie.setRuntime(resultSet.getInt("runtime"));
            movie.setReleaseDate(resultSet.getDate("release_date"));
//            movie.setGenre(Genre.valueOf(resultSet.getString("genre")));
            movies.add(movie);
        }
        LOGGER.log(Level.INFO, "Got movie(s) [" + movies.toString() + "]");
        return movies;
    }

}
