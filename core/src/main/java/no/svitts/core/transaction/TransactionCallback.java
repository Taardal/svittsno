package no.svitts.core.transaction;

import no.svitts.core.repository.Repository;

public interface TransactionCallback<T, R> {

    R execute(Repository<T> repository);

}
