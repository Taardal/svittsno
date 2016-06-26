package no.svitts.core.transaction;

import no.svitts.core.repository.Repository;

public interface TransactionManager<T> {

    <R> R transaction(Repository<T> repository, UnitOfWork<T, R> unitOfWork);
    void transactionWithoutResult(Repository<T> repository, UnitOfWorkWithoutResult<T> unitOfWorkWithoutResult);

}
