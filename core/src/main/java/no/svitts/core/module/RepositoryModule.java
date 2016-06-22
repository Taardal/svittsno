package no.svitts.core.module;

import com.google.inject.AbstractModule;
import no.svitts.core.provider.SessionFactoryProvider;
import org.hibernate.SessionFactory;

public class RepositoryModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SessionFactory.class).toProvider(SessionFactoryProvider.class);
    }

}
