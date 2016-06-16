package no.svitts.core.transaction;

import no.svitts.core.repository.Repository;

public interface TransactionManager<T> {

    <R> R transaction(TransactionCallback<T, R> transactionCallback);
    void transactionWithoutResult(TransactionCallbackWithoutResult<T> transactionCallbackWithoutResult);

    void setRepository(Repository<T> repository);
}
