package no.svitts.core.repository;

import no.svitts.core.criteria.SearchCriteria;
import no.svitts.core.status.ServerResponse;

import java.util.List;

public interface Repository<T> {

    ServerResponse<T> getById(String id);

    ServerResponse<T> insert(T t);

    ServerResponse<T> update(T t);

    ServerResponse<T> delete(String id);

    ServerResponse<List<T>> search(SearchCriteria searchCriteria);

}
