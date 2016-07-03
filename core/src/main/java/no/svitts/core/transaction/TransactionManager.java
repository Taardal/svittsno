package no.svitts.core.transaction;

public interface TransactionManager<T> {

    <R> R transaction(UnitOfWork<T, R> unitOfWork);
    void transactionWithoutResult(UnitOfWorkWithoutResult<T> unitOfWorkWithoutResult);

}
