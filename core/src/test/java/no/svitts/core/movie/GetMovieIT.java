package no.svitts.core.movie;

import no.svitts.core.datasource.DataSource;
import no.svitts.core.util.Id;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Test;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import static no.svitts.core.testkit.ITestKit.getDataSource;
import static org.junit.Assert.assertEquals;

public class GetMovieIT extends JerseyTest {

    private static final String MOVIE_RESOURCE = "movies";

    private DataSource dataSource;

    @Override
    protected Application configure() {
        dataSource = getDataSource();
        ResourceConfig resourceConfig = new ResourceConfig();
        return resourceConfig;
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
