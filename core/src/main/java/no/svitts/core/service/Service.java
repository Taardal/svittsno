package no.svitts.core.service;

import no.svitts.core.criteria.Criteria;

import java.util.List;

public interface Service<T> {

    T getSingle(String id);

    List<T> getMultiple(Criteria criteria);

    String saveSingle(T t);

    void updateSingle(T entity);

    void deleteSingle(String id);

}
