package no.svitts.core.repository;

import com.google.inject.Inject;
import no.svitts.core.criteria.Criteria;
import no.svitts.core.movie.Movie;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MovieRepository extends CoreRepository<Movie> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieRepository.class);

    @Inject
    public MovieRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Movie getOne(String id) {
        return getCurrentSession().get(Movie.class, id);
    }

    @Override
    public List<Movie> getMany(Criteria criteria) {
        return null;
    }

    @Override
    public String save(Movie movie)  {
        return (String) getCurrentSession().save(movie);
    }

    @Override
    public void delete(String  id)  {
        getCurrentSession().delete(getOne(id));
    }

}
