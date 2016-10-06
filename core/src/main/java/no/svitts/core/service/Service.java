package no.svitts.core.service;

import no.svitts.core.search.Search;

import java.util.List;

public interface Service<T> {

    T get(String id);

    List<T> search(Search search);

    String save(T t);

    void update(T entity);

    void delete(String id);

}
