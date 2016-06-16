package no.svitts.core.dao;

import no.svitts.core.repository.Repository;

public interface Transaction<T, R> {

   R execute(Repository<T> repository);

}
