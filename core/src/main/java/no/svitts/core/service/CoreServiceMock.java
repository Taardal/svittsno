package no.svitts.core.service;

import no.svitts.core.search.Search;
import no.svitts.core.movie.Movie;
import no.svitts.core.repository.Repository;
import org.hibernate.SessionFactory;

import java.util.List;

public class CoreServiceMock extends CoreService<Object> {

    public CoreServiceMock(Repository<Object> repository, SessionFactory sessionFactory) {
        super(repository, sessionFactory);
    }

    @Override
    public Movie get(String id) {
        return null;
    }

    @Override
    public List<Movie> search(Search search) {
        return null;
    }

    @Override
    public String save(Movie movie) {
        return null;
    }

    @Override
    public void update(Movie entity) {

    }

    @Override
    public void delete(String id) {

    }
}
