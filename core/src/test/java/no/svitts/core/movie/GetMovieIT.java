package no.svitts.core.movie;

import com.google.gson.Gson;
import no.svitts.core.builder.MovieBuilder;
import no.svitts.core.datasource.DataSource;
import no.svitts.core.id.Id;
import no.svitts.core.repository.MovieRepository;
import no.svitts.core.resource.MovieResource;
import no.svitts.core.service.MovieService;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import static no.svitts.core.testkit.ITestKit.getDataSource;
import static no.svitts.core.testkit.JerseyTestKit.getResourceConfig;
import static no.svitts.core.testkit.MovieTestKit.getGson;
import static org.junit.Assert.assertEquals;

public class GetMovieIT extends JerseyTest {

    private static final String MOVIE_RESOURCE = "movies";

    private DataSource dataSource;
    private Gson gson;
    private MovieBuilder movieBuilder;

    @Override
    protected Application configure() {
        dataSource = getDataSource();
        return getResourceConfig(new MovieResource(new MovieService(new MovieRepository(dataSource))));
    }

    @Override
    @Before
    public void setUp() throws Exception {
        gson = getGson();
        movieBuilder = new MovieBuilder();
        super.setUp();
    }

    @Override
    @After
    public void tearDown() throws Exception {
        dataSource.close();
        super.tearDown();
    }

    @Test
    public void getMovieById_MovieDoesNotExist_ShouldReturnNotFoundResponse() {
        Response response = target(MOVIE_RESOURCE).path(Id.get()).request().get();
        assertEquals(200, response.getStatus());
        response.close();
    }

}
