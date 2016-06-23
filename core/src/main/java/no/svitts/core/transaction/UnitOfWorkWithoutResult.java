package no.svitts.core.transaction;

import no.svitts.core.repository.Repository;

public interface UnitOfWorkWithoutResult<T> {

    void execute(Repository<T> repository);

}
