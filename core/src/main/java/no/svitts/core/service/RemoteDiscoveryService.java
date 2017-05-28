package no.svitts.core.service;

import no.svitts.core.movie.Movie;
import no.svitts.core.serverinfo.ServerInfo;

import java.util.List;

public interface RemoteDiscoveryService {

    List<Movie> discover(String path, ServerInfo serverInfo);

}
