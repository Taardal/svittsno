package no.svitts.core.repository;

import no.svitts.core.builder.MovieBuilder;
import no.svitts.core.exception.RepositoryException;
import no.svitts.core.movie.Movie;
import no.svitts.core.search.MovieSearch;
import no.svitts.core.search.MovieSearchType;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@SuppressWarnings("unchecked")
public class MovieRepositoryTest {

    private MovieRepository movieRepository;
    private MovieBuilder movieBuilder;

    @Mock
    private SessionFactory sessionFactoryMock;

    @Mock
    private Session sessionMock;

    @Before
    public void setUp() {
        initMocks(this);
        when(sessionFactoryMock.getCurrentSession()).thenReturn(sessionMock);
        movieRepository = new MovieRepository(sessionFactoryMock);
        movieBuilder = new MovieBuilder();
    }

    @Test
    public void get_ValidId_ShouldReturnMovie() {
        Movie movie = movieBuilder.build();
        when(sessionMock.get(Movie.class, movie.getId())).thenReturn(movie);

        Movie movieFromRepository = movieRepository.get(movie.getId());

        assertEquals(movie, movieFromRepository);
        verify(sessionFactoryMock, times(1)).getCurrentSession();
        verify(sessionMock, times(1)).get(Movie.class, movie.getId());
    }

    @Test(expected = RepositoryException.class)
    public void get_ThrowsHibernateException_ShouldCatchHibernateExceptionAndThrowRepositoryException() {
        Movie movie = movieBuilder.build();
        when(sessionMock.get(Movie.class, movie.getId())).thenThrow(new HibernateException("Exception"));
        movieRepository.get(movie.getId());
    }

    @Test
    public void search_ValidCriteria_ShouldReturnMovies() {
        List<Movie> movies = Arrays.stream(new Movie[]{movieBuilder.build()}).collect(Collectors.toList());
        MovieSearch movieSearch = new MovieSearch("movie", MovieSearchType.TITLE);

        CriteriaBuilder criteriaBuilderMock = mock(CriteriaBuilder.class);
        when(sessionMock.getCriteriaBuilder()).thenReturn(criteriaBuilderMock);

        CriteriaQuery criteriaQueryMock = mock(CriteriaQuery.class);
        when(criteriaBuilderMock.createQuery(Movie.class)).thenReturn(criteriaQueryMock);

        Root rootMock = mock(Root.class);
        when(criteriaQueryMock.from(Movie.class)).thenReturn(rootMock);
        when(criteriaQueryMock.select(rootMock)).thenReturn(criteriaQueryMock);
        when(criteriaQueryMock.where(any(Predicate.class))).thenReturn(criteriaQueryMock);

        Query queryMock = mock(Query.class);
        when(sessionMock.createQuery(criteriaQueryMock)).thenReturn(queryMock);
        when(queryMock.setMaxResults(movieSearch.getLimit())).thenReturn(queryMock);
        when(queryMock.setFirstResult(movieSearch.getOffset())).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(movies);

        List<Movie> moviesFromRepository = movieRepository.search(movieSearch);

        assertArrayEquals(movies.toArray(), moviesFromRepository.toArray());
        verify(sessionFactoryMock, times(2)).getCurrentSession();
        verify(sessionMock, times(1)).getCriteriaBuilder();
        verify(sessionMock, times(1)).createQuery(criteriaQueryMock);
        verify(queryMock, times(1)).setMaxResults(movieSearch.getLimit());
        verify(queryMock, times(1)).setFirstResult(movieSearch.getOffset());
        verify(queryMock, times(1)).getResultList();
    }

    @Test(expected = RepositoryException.class)
    public void search_ThrowsHibernateException_ShouldCatchHibernateExceptionAndThrowRepositoryException() {
        when(sessionMock.getCriteriaBuilder()).thenThrow(new HibernateException("Exception"));
        movieRepository.search(new MovieSearch("query", MovieSearchType.TITLE));
    }

    @Test
    public void save_ValidMovie_ShouldReturnIdOfSavedMovie() {
        Movie movie = movieBuilder.build();
        when(sessionMock.save(movie)).thenReturn(movie.getId());

        String savedMovieId = movieRepository.save(movie);

        assertEquals(movie.getId(), savedMovieId);
        verify(sessionFactoryMock, times(1)).getCurrentSession();
        verify(sessionMock, times(1)).save(movie);
    }

    @Test(expected = RepositoryException.class)
    public void save_ThrowsHibernateException_ShouldCatchHibernateExceptionAndThrowRepositoryException() {
        Movie movie = movieBuilder.build();
        when(sessionMock.save(movie)).thenThrow(new HibernateException("Exception"));
        movieRepository.save(movie);
    }

    @Test
    public void update_ValidMovie_ShouldUpdateMovie() {
        Movie movie = movieBuilder.build();
        doNothing().when(sessionMock).update(movie);

        movieRepository.update(movie);

        verify(sessionFactoryMock, times(1)).getCurrentSession();
        verify(sessionMock, times(1)).update(movie);
    }

    @Test(expected = RepositoryException.class)
    public void update_ThrowsHibernateException_ShouldCatchHibernateExceptionAndThrowRepositoryException() {
        Movie movie = movieBuilder.build();
        doThrow(new HibernateException("Exception")).when(sessionMock).update(movie);
        movieRepository.update(movie);
    }

    @Test
    public void delete_ValidMovie_ShouldDeleteMovie() {
        Movie movie = movieBuilder.build();
        doNothing().when(sessionMock).delete(movie);

        movieRepository.delete(movie);

        verify(sessionFactoryMock, times(1)).getCurrentSession();
        verify(sessionMock, times(1)).delete(movie);
    }

    @Test(expected = RepositoryException.class)
    public void delete_ThrowsHibernateException_ShouldCatchHibernateExceptionAndThrowRepositoryException() {
        Movie movie = movieBuilder.build();
        doThrow(new HibernateException("Exception")).when(sessionMock).delete(movie);
        movieRepository.delete(movie);
    }

}
