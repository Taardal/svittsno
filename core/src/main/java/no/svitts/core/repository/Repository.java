package no.svitts.core.repository;

import no.svitts.core.exception.RepositoryException;
import no.svitts.core.search.SearchCriteria;

import java.util.List;

public interface Repository<T> {
    T getSingle(String id) throws RepositoryException;

    List<T> getMultiple(SearchCriteria searchCriteria);

    String insertSingle(T t);

    void updateSingle(T t);

    void deleteSingle(String id);
}
