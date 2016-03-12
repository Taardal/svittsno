package no.svitts.core.repository;

import java.util.List;

public interface Repository<T> {
    List<T> getAll();
    T getById(String id);
    int updateSingle(String id);
    int deleteSingle(String id);

}
