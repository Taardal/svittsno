package no.svitts.core;

import io.swagger.jaxrs.config.BeanConfig;
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
        createSwaggerBean(applicationProperties);
        DataSource dataSource = new SqlDataSource(applicationProperties);
        register(getMovieResource(dataSource));
        register(io.swagger.jaxrs.listing.ApiListingResource.class);
        register(io.swagger.jaxrs.listing.SwaggerSerializers.class);
    }

    private MovieResource getMovieResource(DataSource dataSource) {
        Repository<Movie> movieRepository = new MovieRepository(dataSource);
        MovieService movieService = new MovieService(movieRepository);
        return new MovieResource(movieService);
    }

    private void createSwaggerBean(ApplicationProperties applicationProperties) {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setTitle("Svitts API");
        beanConfig.setDescription("Such description...");
        beanConfig.setTermsOfServiceUrl("http://swagger.io/terms/");
        beanConfig.setContact("torbjorn.aardal@gmail.com");
        beanConfig.setLicense("Very license...");
        beanConfig.setVersion(applicationProperties.get("swagger.version"));
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/api");
        beanConfig.setResourcePackage("no.svitts.core.resource");
        beanConfig.setScan(true);
    }

}