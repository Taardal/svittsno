package no.svitts.movie;

import no.svitts.CoreTestKit;
import no.svitts.core.movie.Movie;
import no.svitts.testdata.SherlockHolmes;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

public class MovieTest extends CoreTestKit {

    @Test
    public void testCreateMovies() {
        String json = getJson(new SherlockHolmes(), Movie.class);
        Response response = coreClient.request().path("/movie").path("/new").post(json).response();
        assertEquals(200, response.getStatus());
    }

}
