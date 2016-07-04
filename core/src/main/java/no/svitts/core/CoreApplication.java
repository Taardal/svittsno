package no.svitts.core;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import no.svitts.core.configuration.ApplicationProperties;
import no.svitts.core.exception.mapper.ConstraintViolationExceptionMapper;
import no.svitts.core.exception.mapper.WebApplicationExceptionMapper;
import no.svitts.core.json.GsonMessageBodyReader;
import no.svitts.core.json.GsonMessageBodyWriter;
import no.svitts.core.module.PersistenceModule;
import no.svitts.core.module.WebModule;
import no.svitts.core.provider.SessionFactoryProvider;
import no.svitts.core.resource.MovieResource;
import org.glassfish.jersey.server.ResourceConfig;


public class CoreApplication extends ResourceConfig {

    public CoreApplication() {
        Injector injector = Guice.createInjector(new WebModule(), new PersistenceModule(new SessionFactoryProvider()));
        registerComponents(injector);
        createSwaggerBean(new ApplicationProperties());
    }

    private void registerComponents(Injector injector) {
        register(injector.getInstance(MovieResource.class));
        register(WebApplicationExceptionMapper.class);
        register(ConstraintViolationExceptionMapper.class);
        register(GsonMessageBodyReader.class);
        register(GsonMessageBodyWriter.class);
        register(ApiListingResource.class);
        register(SwaggerSerializers.class);
    }

    private void createSwaggerBean(ApplicationProperties applicationProperties) {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setTitle("Svitts API");
        beanConfig.setDescription("This is the API for the Svitts movie library application.");
        beanConfig.setContact("Torbjørn Årdal - torbjorn.aardal@gmail.com");
        beanConfig.setVersion(applicationProperties.getProperty("api.version"));
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/api");
        beanConfig.setResourcePackage("no.svitts.core.resource");
        beanConfig.setScan(true);
    }

}