package no.svitts.core.repository;

import java.util.List;

public interface Repository<T> {
    List<T> getAll();
    List<T> getMultiple(List<T> ts);
    T getById(String id);
    T getByAttributes(Object... attributes);
    boolean insertSingle(T t);
    boolean updateSingle(T t);
    boolean deleteSingle(String id);
}
