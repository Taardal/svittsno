package no.svitts.core.repository;

import no.svitts.core.criteria.SearchCriteria;

import java.util.List;

public interface Repository<T> {
    T getById(String id);
    String insert(T t);
    boolean update(T t);
    boolean delete(String id);
    List<T> search(SearchCriteria searchCriteria);
}
