package no.svitts.core.repository;

import no.svitts.core.search.Criteria;

import java.io.Serializable;
import java.util.List;

public interface Repository<T> {
    T getOne(String id);

    List<T> getMany(Criteria criteria);

    <S extends Serializable> S save(T entity);

    void delete(String id);
}
