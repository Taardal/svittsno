package no.svitts.core.repository;

import no.svitts.core.search.Search;

import java.util.List;

public interface Repository<T> {

    T get(String id);

    List<T> search(Search search);

    String save(T entity);

    Void update(T entity);

    Void delete(T entity);
}
