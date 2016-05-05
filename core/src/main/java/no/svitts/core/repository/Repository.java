package no.svitts.core.repository;

import java.util.List;

public interface Repository<T> {
    List<T> getAll();
    T getById(String id);
    boolean insert(T t);
    boolean update(T t);
    boolean delete(String id);
}
