package no.svitts.core.service;

import no.svitts.core.search.Criteria;

import java.util.List;

public interface Service<T> {

    T getById(String id);

    List<T> getByCriteria(Criteria criteria);

    String save(T t);

    void delete(String id);

}
