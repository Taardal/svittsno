package no.svitts.core;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import no.svitts.core.exception.mapper.ConstraintViolationExceptionMapper;
import no.svitts.core.exception.mapper.WebApplicationExceptionMapper;
import no.svitts.core.json.GsonMessageBodyReader;
import no.svitts.core.json.GsonMessageBodyWriter;
import no.svitts.core.module.PersistenceModule;
import no.svitts.core.module.ResourceModule;
import no.svitts.core.provider.SessionFactoryProvider;
import no.svitts.core.resource.GenreResource;
import no.svitts.core.resource.MovieResource;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

import static no.svitts.core.CoreApplication.APPLICATION_PATH;

@ApplicationPath(value = APPLICATION_PATH)
public class CoreApplication extends ResourceConfig {

    static final String APPLICATION_NAME = "Svitts API";
    static final String APPLICATION_VERSION = "v1";
    static final String APPLICATION_PATH = "api/" + APPLICATION_VERSION;

    public CoreApplication() {
        Injector injector = Guice.createInjector(new ResourceModule(), new PersistenceModule(new SessionFactoryProvider()));
        registerComponents(injector);
        setApplicationName(APPLICATION_NAME);
        createSwaggerBean();
    }

    private void registerComponents(Injector injector) {
        register(injector.getInstance(MovieResource.class));
        register(injector.getInstance(GenreResource.class));
        register(injector.getInstance(WebApplicationExceptionMapper.class));
        register(injector.getInstance(ConstraintViolationExceptionMapper.class));
        register(injector.getInstance(GsonMessageBodyReader.class));
        register(injector.getInstance(GsonMessageBodyWriter.class));
        register(injector.getInstance(ApiListingResource.class));
        register(injector.getInstance(SwaggerSerializers.class));
    }

    private void createSwaggerBean() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setTitle(getApplicationName());
        beanConfig.setResourcePackage("no.svitts.core.resource");
        beanConfig.setScan(true);
        beanConfig.setBasePath("svitts/" + APPLICATION_PATH);
        beanConfig.setVersion(APPLICATION_VERSION);
        beanConfig.setDescription("This is the API for the Svitts movie library application.");
        beanConfig.setContact("Torbjørn Årdal\ntorbjorn.aardal@gmail.com");
    }

}