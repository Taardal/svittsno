package no.svitts.core.movie;

import no.svitts.core.datasource.DataSource;
import org.glassfish.jersey.test.JerseyTest;

public class GetMovieIT extends JerseyTest {

    private static final String MOVIE_RESOURCE = "movies";

    private DataSource dataSource;

//    @Override
//    protected Application configure() {
//        dataSource = getDataSource();
//        ResourceConfig resourceConfig = new ResourceConfig();
//        return resourceConfig;
//    }
//
//    @Override
//    @After
//    public void tearDown() throws Exception {
//        dataSource.close();
//        super.tearDown();
//    }
//
//    @Test
//    public void getMovieById_MovieDoesNotExist_ShouldReturnNotFoundResponse() {
//        Response response = target(MOVIE_RESOURCE).path(Id.get()).request().get();
//        assertEquals(200, response.getStatus());
//        response.close();
//    }

}
