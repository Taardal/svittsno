package no.svitts.core;

import no.svitts.core.application.ApplicationProperties;
import no.svitts.core.datasource.DataSource;
import no.svitts.core.datasource.SqlDataSource;
import no.svitts.core.movie.Movie;
import no.svitts.core.repository.MovieRepository;
import no.svitts.core.repository.Repository;
import no.svitts.core.resource.MovieResource;
import no.svitts.core.service.MovieService;
import org.glassfish.jersey.server.ResourceConfig;


public class CoreApplication extends ResourceConfig {

    public CoreApplication() {
        DataSource dataSource = new SqlDataSource(new ApplicationProperties());
        register(getMovieResource(dataSource));
    }

    private MovieResource getMovieResource(DataSource dataSource) {
        Repository<Movie> movieRepository = new MovieRepository(dataSource);
        MovieService movieService = new MovieService(movieRepository);
        return new MovieResource(movieService);
    }

}