package no.svitts.core.service;

import no.svitts.core.search.Criteria;

import java.io.Serializable;
import java.util.List;

public interface Service<T> {

    T getOne(String id);

    List<T> getByCriteria(Criteria criteria);

    <S extends Serializable> S save(T t);

    void delete(String id);

}
