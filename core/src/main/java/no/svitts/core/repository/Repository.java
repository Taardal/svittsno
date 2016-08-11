package no.svitts.core.repository;

import no.svitts.core.criteria.Criteria;

import java.util.List;

public interface Repository<T> {

    T getSingle(String id);

    List<T> getMultiple(Criteria criteria);

    String saveSingle(T entity);

    Void updateSingle(T entity);

    Void deleteSingle(T entity);
}
