package no.svitts.core;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
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
        ApplicationProperties applicationProperties = new ApplicationProperties();
        DataSource dataSource = new SqlDataSource(applicationProperties);
        register(getMovieResource(dataSource));
        initializeSwagger(applicationProperties);
    }

    private MovieResource getMovieResource(DataSource dataSource) {
        Repository<Movie> movieRepository = new MovieRepository(dataSource);
        MovieService movieService = new MovieService(movieRepository);
        return new MovieResource(movieService);
    }

    private void initializeSwagger(ApplicationProperties applicationProperties) {
        register(ApiListingResource.class);
        register(SwaggerSerializers.class);
        createSwaggerBean(applicationProperties);
    }

    private void createSwaggerBean(ApplicationProperties applicationProperties) {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setTitle("Svitts API");
        beanConfig.setDescription("This is the API for the Svitts movie library application.");
        beanConfig.setContact("Torbjørn Årdal - torbjorn.aardal@gmail.com");
        beanConfig.setVersion(applicationProperties.get("swagger.version"));
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/api");
        beanConfig.setResourcePackage("no.svitts.core.resource");
        beanConfig.setScan(true);
    }

}