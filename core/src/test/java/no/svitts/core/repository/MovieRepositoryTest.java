package no.svitts.core.repository;

import no.svitts.core.builder.MovieBuilder;
import org.junit.Before;

import java.sql.SQLException;

public class MovieRepositoryTest {

    private MovieBuilder movieBuilder;

    @Before
    public void setUp() throws SQLException {
        movieBuilder = new MovieBuilder();

    }


}
