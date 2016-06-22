package no.svitts.core.module;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import no.svitts.core.movie.Movie;
import no.svitts.core.service.MovieService;
import no.svitts.core.service.Service;

public class ResourceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<Service<Movie>>(){}).to(MovieService.class);
    }

}
