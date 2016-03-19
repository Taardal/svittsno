package no.svitts.core.repository;

import java.util.List;

public interface Repository<T> {
    List<T> getAll();
    T getById(String id);
    boolean insertSingle(T object);
    boolean updateSingle(T object);
    boolean deleteSingle(String id);

}
