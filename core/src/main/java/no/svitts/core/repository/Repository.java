package no.svitts.core.repository;

import java.util.List;

public interface Repository<T> {
    List<T> getAll();
    T getById(int id);
    int update(int id);
    int delete(int id);

}
