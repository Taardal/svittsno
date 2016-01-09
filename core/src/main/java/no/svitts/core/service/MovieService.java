package no.svitts.core.service;

import no.svitts.core.database.DatabaseConnection;
import no.svitts.core.movie.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MovieService implements Service<Movie> {

    private static final Logger LOGGER = Logger.getLogger(MovieService.class.getName());

    private static final String SELECT_ALL_MOVIES = "SELECT * FROM movie;";

    private static final String ID = "id";
    private static final String IMDB_ID = "imdb_id";
    private static final String NAME = "name";
    private static final String TAGLINE = "tagline";
    private static final String OVERVIEW = "overview";
    private static final String RUNTIME = "runtime";
    private static final String RELEASE_DATE = "release_date";
    private static final String ADDED = "added";
    private static final String LAST_PLAYED = "last_played";

    private DatabaseConnection databaseConnection;

    public MovieService(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public List<Movie> getAll() {
        List<Movie> movies = new ArrayList<>();
        databaseConnection.connect();
        try {
            ResultSet resultSet = databaseConnection.executeQuery(SELECT_ALL_MOVIES);
            while (resultSet.next()) {
                Movie movie = getMovie(resultSet);
                movies.add(movie);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not get movie(s)", e);
        } finally {
            databaseConnection.disconnect();
        }
        return movies;
    }

    private Movie getMovie(ResultSet resultSet) throws SQLException {
        Movie movie = new Movie(resultSet.getInt(ID));
        movie.setImdbId(resultSet.getString(IMDB_ID));
        movie.setName(resultSet.getString(NAME));
        movie.setTagline(resultSet.getString(TAGLINE));
        movie.setOverview(resultSet.getString(OVERVIEW));
        movie.setRuntime(resultSet.getInt(RUNTIME));
        movie.setReleaseDate(resultSet.getDate(RELEASE_DATE));
        movie.setAdded(resultSet.getDate(ADDED));
        movie.setLastPlayed(resultSet.getDate(LAST_PLAYED));
        LOGGER.log(Level.INFO, "Got movie " + movie.toString());
        return movie;
    }

    private String buildPath(ResultSet resultSet) throws SQLException {
        String path = "";
        int i = 0;
        while (resultSet.next()) {
            path += resultSet.getString("name");
            if (!resultSet.isLast()) {
                path += "\\";
            }
        }
        return path;
    }
}
