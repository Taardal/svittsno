package no.svitts.core.transaction;

public interface TransactionManager<T> {

    <R> R transaction(TransactionCallback<T, R> transactionCallback);
    void transactionWithoutResult(TransactionCallbackWithoutResult<T> transactionCallbackWithoutResult);

}
