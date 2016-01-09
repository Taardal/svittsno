package no.svitts.core;

import no.svitts.core.controller.MovieController;
import no.svitts.core.service.MovieService;
import no.svitts.core.database.SvittsDatabaseConnection;
import org.glassfish.jersey.server.ResourceConfig;

public class CoreApplication extends ResourceConfig {

    public CoreApplication() {
        register(getMovieController());
    }

    private MovieController getMovieController() {
        SvittsDatabaseConnection svittsDatabaseConnection = new SvittsDatabaseConnection();
        MovieService movieService = new MovieService(svittsDatabaseConnection);
        return new MovieController(movieService);
    }

}