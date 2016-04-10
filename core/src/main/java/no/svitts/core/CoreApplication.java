package no.svitts.core;

import no.svitts.core.datasource.DataSource;
import no.svitts.core.datasource.DataSourceConfig;
import no.svitts.core.datasource.SqlDataSource;
import no.svitts.core.movie.Movie;
import no.svitts.core.repository.MovieRepository;
import no.svitts.core.repository.Repository;
import no.svitts.core.resource.MovieResource;
import no.svitts.core.service.MovieService;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class CoreApplication extends ResourceConfig {

    private static final String APPLICATION_PROPERTIES_FILE = "application.properties";

    public CoreApplication() {
        DataSourceConfig dataSourceConfig = new DataSourceConfig(getApplicationProperties());
        DataSource dataSource = new SqlDataSource(dataSourceConfig);
        register(getMovieResource(dataSource));
    }

    private MovieResource getMovieResource(DataSource dataSource) {
        Repository<Movie> movieRepository = new MovieRepository(dataSource);
        MovieService movieService = new MovieService(movieRepository);
        return new MovieResource(movieService);
    }

    private Properties getApplicationProperties() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(APPLICATION_PROPERTIES_FILE)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Could not load input stream for file [" + APPLICATION_PROPERTIES_FILE + "]");
        }
    }

}