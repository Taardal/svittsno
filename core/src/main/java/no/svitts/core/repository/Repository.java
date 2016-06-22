package no.svitts.core.repository;

import no.svitts.core.criteria.Criteria;

import java.util.List;

public interface Repository<T> {
    T getOne(String id);

    List<T> getMany(Criteria criteria);

    String save(T entity);

    void delete(String id);
}
