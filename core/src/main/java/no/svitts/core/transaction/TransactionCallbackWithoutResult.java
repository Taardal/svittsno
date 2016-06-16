package no.svitts.core.transaction;

import no.svitts.core.repository.Repository;

public interface TransactionCallbackWithoutResult<T> {

    void execute(Repository<T> repository);

}
