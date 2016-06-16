package no.svitts.core.dao;

import java.util.List;

public interface Dao<T> {

    List<T> getAll();
    T getSingle(String id);
    boolean update();

}
