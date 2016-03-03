package no.svitts.core;

import no.svitts.core.database.LocalDataSource;
import no.svitts.core.file.VideoFile;
import no.svitts.core.movie.Movie;
import no.svitts.core.repository.MovieRepository;
import no.svitts.core.repository.Repository;
import no.svitts.core.repository.VideoFileRepository;
import no.svitts.core.resource.MovieResource;
import no.svitts.core.service.MovieService;
import no.svitts.core.service.Service;
import org.glassfish.jersey.server.ResourceConfig;

public class CoreApplication extends ResourceConfig {

    public CoreApplication() {
        register(getMovieResource());
    }

    private MovieResource getMovieResource() {
//        DataSource dataSource = new SvittsDataSource();
        LocalDataSource dataSource = new LocalDataSource();
        Repository<VideoFile> videoFileRepository = new VideoFileRepository(dataSource);
        Repository<Movie> movieRepository = new MovieRepository(dataSource);
        Service<Movie> movieService = new MovieService(movieRepository, videoFileRepository);
        return new MovieResource(movieService);
    }

}