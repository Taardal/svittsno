package no.svitts.core;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import no.svitts.core.application.ApplicationProperties;
import no.svitts.core.datasource.CoreDataSource;
import no.svitts.core.datasource.DataSource;
import no.svitts.core.datasource.DataSourceConfig;
import no.svitts.core.exception.WebApplicationExceptionMapper;
import no.svitts.core.repository.MovieRepository;
import no.svitts.core.resource.MovieResource;
import no.svitts.core.service.MovieService;
import org.glassfish.jersey.server.ResourceConfig;


public class CoreApplication extends ResourceConfig {

    public CoreApplication() {
        ApplicationProperties applicationProperties = new ApplicationProperties();
        DataSource dataSource = new CoreDataSource(getDataSourceConfig(applicationProperties));
        register(new MovieResource(new MovieService(new MovieRepository(dataSource))));
        register(new WebApplicationExceptionMapper());
        registerSwagger(applicationProperties);
    }

    private DataSourceConfig getDataSourceConfig(ApplicationProperties applicationProperties) {
        return new DataSourceConfig(
                applicationProperties.get("db.main.url"),
                applicationProperties.get("db.username"),
                applicationProperties.get("db.password"),
                applicationProperties.get("db.driver"));
    }

    private void registerSwagger(ApplicationProperties applicationProperties) {
        register(new ApiListingResource());
        register(new SwaggerSerializers());
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