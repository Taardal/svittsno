package no.svitts.core.module;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import no.svitts.core.movie.Movie;
import no.svitts.core.provider.MainSessionFactoryProvider;
import no.svitts.core.repository.MovieRepository;
import no.svitts.core.repository.Repository;
import no.svitts.core.transaction.RepositoryTransactionManager;
import no.svitts.core.transaction.TransactionManager;
import org.hibernate.SessionFactory;

public class PersistenceModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(SessionFactory.class).toProvider(MainSessionFactoryProvider.class);
//        binder.bind(SessionFactory.class).toProvider(ItestSessionFactoryProvider.class);
        binder.bind(new TypeLiteral<Repository<Movie>>(){}).to(MovieRepository.class);
        binder.bind(new TypeLiteral<TransactionManager<Movie>>(){}).to(new TypeLiteral<RepositoryTransactionManager<Movie>>(){});
    }

}
