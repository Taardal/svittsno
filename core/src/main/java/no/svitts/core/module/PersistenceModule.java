package no.svitts.core.module;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import no.svitts.core.movie.Movie;
import no.svitts.core.repository.MovieRepository;
import no.svitts.core.repository.Repository;
import org.hibernate.SessionFactory;

public class PersistenceModule implements Module {

    private Provider<SessionFactory> sessionFactoryProvider;

    public PersistenceModule(Provider<SessionFactory> sessionFactoryProvider) {
        this.sessionFactoryProvider = sessionFactoryProvider;
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(SessionFactory.class).toProvider(sessionFactoryProvider);
        binder.bind(new TypeLiteral<Repository<Movie>>(){}).to(MovieRepository.class);
    }

}
