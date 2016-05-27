package no.svitts.core.repository;

import no.svitts.core.criteria.SearchCriteria;

import java.util.List;

public interface Repository<T> {
    T getById(String id);

    List<T> getMultiple(SearchCriteria searchCriteria);

    String insert(T t);

    void update(T t);

    void delete(String id);
}
