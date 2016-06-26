package no.svitts.core.module;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import no.svitts.core.movie.Movie;
import no.svitts.core.service.MovieService;
import no.svitts.core.service.Service;

public class ResourceModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(new TypeLiteral<Service<Movie>>(){}).to(MovieService.class);
    }

}
