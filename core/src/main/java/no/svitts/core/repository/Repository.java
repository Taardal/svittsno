package no.svitts.core.repository;

import java.util.List;

public interface Repository<T> {
    List<T> getAll();
    T getById(int id);
    void update(int id);
    void delete(int id);

}
