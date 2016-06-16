package no.svitts.core.unitofwork;

import no.svitts.core.repository.Repository;

public interface UnitOfWork<T, R> {

    R execute(Repository<T> repository);

}
