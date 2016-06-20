package no.svitts.core.movie;

import com.google.gson.Gson;
import no.svitts.core.datasource.DataSource;
import org.glassfish.jersey.test.JerseyTest;

public class DeleteMovieIT extends JerseyTest {

    private static final String MOVIE_RESOURCE = "movies";

    private DataSource dataSource;
    private Gson gson;

//    @Override
//    protected Application configure() {
//        dataSource = getDataSource();
//        ResourceConfig resourceConfig = new ResourceConfig();
//        return resourceConfig;
//    }
//
//    @Override
//    @Before
//    public void setUp() throws Exception {
//        gson = new Gson();
//        super.setUp();
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
//    public void deleteMovie_MovieDoesNotExist_ShouldAbortAndReturnServerError() {
//        Response response = target(MOVIE_RESOURCE).path("delete").path(Id.get()).request().delete();
//        assertEquals(500, response.getStatus());
//        response.close();
//    }

}
