package no.svitts.core.service;

import no.svitts.core.search.Search;

import java.util.List;

public interface Service<T> {

    T getSingle(String id);

    List<T> getMultiple(Search search);

    String saveSingle(T t);

    void updateSingle(T entity);

    void deleteSingle(String id);

}
