package no.svitts.core.service;

import no.svitts.core.movie.Movie;

import java.util.List;

public interface LocalDiscoveryService {

    List<Movie> discover(String path);

}
