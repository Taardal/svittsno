package no.svitts.core.service;

import java.util.List;

public interface Service<T> {
    List<T> getAll();
    T getById(int id);
}
