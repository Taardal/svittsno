package no.svitts.core.service;

import no.svitts.core.movie.Movie;
import no.svitts.core.transaction.TransactionManager;
import no.svitts.core.transaction.UnitOfWork;
import no.svitts.core.transaction.UnitOfWorkWithoutResult;

abstract class CoreService<T> implements Service<Movie> {

    private TransactionManager<T> transactionManager;

    CoreService(TransactionManager<T> transactionManager) {
        this.transactionManager = transactionManager;
    }

    <R> R transaction(UnitOfWork<T, R> unitOfWork) {
        return transactionManager.transaction(unitOfWork);
    }

    void transactionWithoutResult(UnitOfWorkWithoutResult<T> unitOfWorkWithoutResult) {
        transactionManager.transactionWithoutResult(unitOfWorkWithoutResult);
    }

}
