package no.svitts.core.service;

import no.svitts.core.movie.Movie;
import no.svitts.core.repository.Repository;
import no.svitts.core.transaction.TransactionManager;
import no.svitts.core.transaction.UnitOfWork;
import no.svitts.core.transaction.UnitOfWorkWithoutResult;

abstract class CoreService<T> implements Service<Movie> {

    private Repository<T> repository;
    private TransactionManager<T> transactionManager;

    CoreService(Repository<T> repository, TransactionManager<T> transactionManager) {
        this.repository = repository;
        this.transactionManager = transactionManager;
    }

    <R> R transaction(UnitOfWork<T, R> unitOfWork) {
        return transactionManager.transaction(repository, unitOfWork);
    }

    void transactionWithoutResult(UnitOfWorkWithoutResult<T> unitOfWorkWithoutResult) {
        transactionManager.transactionWithoutResult(repository, unitOfWorkWithoutResult);
    }

}
