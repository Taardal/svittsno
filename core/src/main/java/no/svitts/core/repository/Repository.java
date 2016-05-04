package no.svitts.core.repository;

import java.util.List;

public interface Repository<T> {
    List<T> getAll();
    T getById(String id);
    boolean insertSingle(T t);
    boolean updateSingle(T t);
    boolean deleteSingle(String id);
}
