package no.svitts.core.module;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import no.svitts.core.movie.Movie;
import no.svitts.core.provider.SessionFactoryProvider;
import no.svitts.core.repository.MovieRepository;
import no.svitts.core.repository.Repository;
import org.hibernate.SessionFactory;

public class TransactionManagerModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(SessionFactory.class).toProvider(SessionFactoryProvider.class);
        binder.bind(new TypeLiteral<Repository<Movie>>() {}).to(MovieRepository.class);
    }
}
