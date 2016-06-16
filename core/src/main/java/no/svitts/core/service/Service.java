package no.svitts.core.service;

import no.svitts.core.search.Criteria;

import java.util.List;

public interface Service<T> {

    T getById(String id);

    List<T> getAll(Criteria criteria);

    String create(T t);

    void update(T t);

    void delete(String id);

}
