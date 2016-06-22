package no.svitts.core.module;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import no.svitts.core.movie.Movie;
import no.svitts.core.transaction.RepositoryTransactionManager;
import no.svitts.core.transaction.TransactionManager;

public class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<TransactionManager<Movie>>(){}).to(new TypeLiteral<RepositoryTransactionManager<Movie>>(){});
    }

}
