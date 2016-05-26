package no.svitts.core.service;

import no.svitts.core.movie.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MovieService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieService.class);

    void foo(Movie movie) {
        if (isRequiredFieldsValid(movie)) {
            String bar = "bar";
        } else {
            LOGGER.warn("Could not validate required fields when asked to insert movie [{}]", movie);
        }
    }

    private boolean isRequiredFieldsValid(Movie movie) {
        return false;
    }

}
