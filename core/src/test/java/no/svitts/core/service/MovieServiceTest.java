package no.svitts.core.service;

import no.svitts.core.builder.MovieBuilder;
import no.svitts.core.repository.MovieRepository;
import org.junit.Before;

import static org.mockito.Mockito.mock;

public class MovieServiceTest {

    private MovieService movieService;
    private MovieBuilder movieBuilder;
    private MovieRepository movieRepositoryMock;

    @Before
    public void setUp() throws Exception {
        movieBuilder = new MovieBuilder();
        movieRepositoryMock = mock(MovieRepository.class);
        movieService = new MovieService(movieRepositoryMock);
    }

}
