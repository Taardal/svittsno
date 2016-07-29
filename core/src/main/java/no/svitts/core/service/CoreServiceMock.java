package no.svitts.core.service;

import no.svitts.core.criteria.Criteria;
import no.svitts.core.movie.Movie;
import no.svitts.core.repository.Repository;
import org.hibernate.SessionFactory;

import java.util.List;

public class CoreServiceMock extends CoreService<Object> {

    public CoreServiceMock(Repository<Object> repository, SessionFactory sessionFactory) {
        super(repository, sessionFactory);
    }

    @Override
    public Movie getSingle(String id) {
        return null;
    }

    @Override
    public List<Movie> getMultiple(Criteria criteria) {
        return null;
    }

    @Override
    public String saveSingle(Movie movie) {
        return null;
    }

    @Override
    public void updateSingle(Movie entity) {

    }

    @Override
    public void deleteSingle(String id) {

    }
}
