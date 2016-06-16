package no.svitts.core.transaction;

import no.svitts.core.repository.Repository;

public interface TransactionManager<T> {

    <R> R executeTransaction(TransactionCallback<T, R> transactionCallback);
    void executeTransactionWithoutResult(TransactionCallbackWithoutResult<T> transactionCallbackWithoutResult);

    void setRepository(Repository<T> repository);
}
