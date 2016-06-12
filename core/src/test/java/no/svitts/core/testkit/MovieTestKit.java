package no.svitts.core.testkit;

import no.svitts.core.movie.Movie;

import static org.junit.Assert.assertEquals;

public class MovieTestKit {

    private MovieTestKit() {
    }

    public static void assertMovie(Movie expectedMovie, Movie actualMovie) {
        assertEquals(expectedMovie.getId(), actualMovie.getId());
        assertEquals(expectedMovie.getName(), actualMovie.getName());
        assertEquals(expectedMovie.getImdbId(), actualMovie.getImdbId());
        assertEquals(expectedMovie.getTagline(), actualMovie.getTagline());
        assertEquals(expectedMovie.getOverview(), actualMovie.getOverview());
        assertEquals(expectedMovie.getRuntime(), actualMovie.getRuntime());
        assertEquals(expectedMovie.getReleaseDate(), actualMovie.getReleaseDate());
        assertEquals(expectedMovie.getGenres(), actualMovie.getGenres());
        assertEquals(expectedMovie.getVideoFile(), actualMovie.getVideoFile());
        assertEquals(expectedMovie.getPosterImageFile(), actualMovie.getPosterImageFile());
        assertEquals(expectedMovie.getBackdropImageFile(),  actualMovie.getBackdropImageFile());
    }

}
