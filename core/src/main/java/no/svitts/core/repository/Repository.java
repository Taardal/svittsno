package no.svitts.core.repository;

import no.svitts.core.search.Search;

import java.util.List;

public interface Repository<T> {

    T getSingle(String id);

    List<T> getMultiple(Search search);

    String saveSingle(T entity);

    Void updateSingle(T entity);

    Void deleteSingle(T entity);
}
