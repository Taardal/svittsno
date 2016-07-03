package no.svitts.core.transaction;

import no.svitts.core.exception.RepositoryException;
import no.svitts.core.exception.TransactionException;
import no.svitts.core.repository.Repository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class RepositoryTransactionManagerTest {

    private RepositoryTransactionManager<Object> repositoryTransactionManager;

    @Mock
    private Repository<Object> repositoryMock;

    @Mock
    private UnitOfWork<Object, Object> unitOfWorkMock;

    @Mock
    private UnitOfWorkWithoutResult<Object> unitOfWorkWithoutResultMock;

    @Mock
    private SessionFactory sessionFactoryMock;

    @Mock
    private Session sessionMock;

    @Mock
    private Transaction transactionMock;

    @Before
    public void setUp() {
        initMocks(this);
        when(sessionFactoryMock.getCurrentSession()).thenReturn(sessionMock);
        when(sessionMock.beginTransaction()).thenReturn(transactionMock);
        repositoryTransactionManager = new RepositoryTransactionManager<>(repositoryMock, sessionFactoryMock);
    }

    @Test
    public void transaction_ValidUnitOfWork_ShouldExecuteUnitOfWorkAndReturnResult() {
        Object object = new Object();
        when(unitOfWorkMock.execute(repositoryMock)).thenReturn(object);
        Object objectFromUnitOfWork = repositoryTransactionManager.transaction(unitOfWorkMock);
        assertEquals(object, objectFromUnitOfWork);
        verify(sessionFactoryMock, times(1)).getCurrentSession();
        verify(sessionMock, times(1)).beginTransaction();
        verify(transactionMock, times(1)).commit();
        verify(sessionMock, times(1)).close();
    }

    @Test(expected = TransactionException.class)
    public void transaction_ThrowsRepositoryException_ShouldRollbackTransactionAndThrowTransactionException() {
        RepositoryException repositoryException = new RepositoryException();
        when(unitOfWorkMock.execute(repositoryMock)).thenThrow(repositoryException);
        try {
            repositoryTransactionManager.transaction(unitOfWorkMock);
        } catch (TransactionException e) {
            verify(transactionMock, times(1)).rollback();
            throw e;
        }
    }

    @Test
    public void transactionWithoutResult_ValidUnitOfWorkWithoutResult_ShouldExecuteUnitOfWorkWithoutResult() {
        repositoryTransactionManager.transactionWithoutResult(unitOfWorkWithoutResultMock);
        verify(sessionFactoryMock, times(1)).getCurrentSession();
        verify(sessionMock, times(1)).beginTransaction();
        verify(transactionMock, times(1)).commit();
        verify(sessionMock, times(1)).close();
    }

    @Test(expected = TransactionException.class)
    public void transactionWithoutResult_ThrowsRepositoryException_ShouldRollbackTransactionAndThrowTransactionException() {
        doThrow(new RepositoryException()).when(unitOfWorkWithoutResultMock).execute(repositoryMock);
        try {
            repositoryTransactionManager.transactionWithoutResult(unitOfWorkWithoutResultMock);
        } catch (TransactionException e) {
            verify(transactionMock, times(1)).rollback();
            throw e;
        }
    }

}
