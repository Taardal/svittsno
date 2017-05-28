package no.svitts.core;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import no.svitts.core.exception.mapper.ConstraintViolationExceptionMapper;
import no.svitts.core.exception.mapper.WebApplicationExceptionMapper;
import no.svitts.core.json.GsonMessageBodyReader;
import no.svitts.core.json.GsonMessageBodyWriter;
import no.svitts.core.module.PersistenceModule;
import no.svitts.core.module.ResourceModule;
import no.svitts.core.provider.SessionFactoryProvider;
import no.svitts.core.resource.DiscoveryResource;
import no.svitts.core.resource.GenreResource;
import no.svitts.core.resource.MovieResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.hibernate.SessionFactory;

public class CoreTestKit {

    private CoreTestKit() {

    }

    public static ResourceConfig getTestApplication() {
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(new WebApplicationExceptionMapper());
        resourceConfig.register(new ConstraintViolationExceptionMapper());
        resourceConfig.register(new GsonMessageBodyReader());
        resourceConfig.register(new GsonMessageBodyWriter());
        return resourceConfig;
    }

    public static ResourceConfig getITestApplication() {
        Provider<SessionFactory> sessionFactoryProvider = new SessionFactoryProvider(SessionFactoryProvider.HIBERNATE_ITEST_PROPERTIES);
        Injector injector = Guice.createInjector(new ResourceModule(), new PersistenceModule(sessionFactoryProvider));
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(injector.getInstance(MovieResource.class));
        resourceConfig.register(injector.getInstance(GenreResource.class));
        resourceConfig.register(injector.getInstance(DiscoveryResource.class));
        resourceConfig.register(injector.getInstance(WebApplicationExceptionMapper.class));
        resourceConfig.register(injector.getInstance(ConstraintViolationExceptionMapper.class));
        resourceConfig.register(injector.getInstance(GsonMessageBodyReader.class));
        resourceConfig.register(injector.getInstance(GsonMessageBodyWriter.class));
        return resourceConfig;
    }

}
