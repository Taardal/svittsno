package no.svitts.core.service;

import no.svitts.core.exception.RepositoryException;
import no.svitts.core.exception.ServiceException;
import no.svitts.core.repository.Repository;
import no.svitts.core.transaction.UnitOfWork;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class CoreServiceTest {

    private CoreServiceMock coreServiceMock;

    @Mock
    private Repository<Object> repositoryMock;

    @Mock
    private SessionFactory sessionFactoryMock;

    @Mock
    private Session sessionMock;

    @Mock
    private Transaction transactionMock;

    @Mock
    private UnitOfWork<Object, Object> unitOfWorkMock;

    @Mock
    private UnitOfWork<Object, Void> unitOfWorkWithoutResultMock;

    @Before
    public void setUp() {
        initMocks(this);
        when(sessionFactoryMock.getCurrentSession()).thenReturn(sessionMock);
        when(sessionMock.beginTransaction()).thenReturn(transactionMock);
        doNothing().when(sessionMock).flush();
        when(sessionMock.isOpen()).thenReturn(true);
        coreServiceMock = new CoreServiceMock(repositoryMock, sessionFactoryMock);
    }

    @Test
    public void transaction_ValidUnitOfWork_ShouldExecuteUnitOfWorkAndReturnResult() {
        Object object = new Object();
        when(unitOfWorkMock.execute(repositoryMock)).thenReturn(object);
        Object objectFromUnitOfWork = coreServiceMock.transaction(unitOfWorkMock);
        assertEquals(object, objectFromUnitOfWork);
        verify(sessionFactoryMock, times(1)).getCurrentSession();
        verify(sessionMock, times(1)).beginTransaction();
        verify(transactionMock, times(1)).commit();
        verify(sessionMock, times(1)).close();
    }

    @Test(expected = ServiceException.class)
    public void transaction_ThrowsRepositoryException_ShouldRollbackTransactionAndThrowServiceException() {
        when(unitOfWorkMock.execute(repositoryMock)).thenThrow(new RepositoryException());
        try {
            coreServiceMock.transaction(unitOfWorkMock);
        } catch (ServiceException e) {
            verify(transactionMock, times(1)).rollback();
            throw e;
        }
    }

    @Test
    public void transactionWithoutResult_ValidUnitOfWorkWithoutResult_ShouldExecuteUnitOfWork() {
        coreServiceMock.transaction(unitOfWorkWithoutResultMock);
        verify(sessionFactoryMock, times(1)).getCurrentSession();
        verify(sessionMock, times(1)).beginTransaction();
        verify(transactionMock, times(1)).commit();
        verify(sessionMock, times(1)).close();
    }

    @Test(expected = ServiceException.class)
    public void transactionWithoutResult_ThrowsRepositoryException_ShouldRollbackTransactionAndThrowServiceException() {
        when(unitOfWorkWithoutResultMock.execute(repositoryMock)).thenThrow(new RepositoryException());
        try {
            coreServiceMock.transaction(unitOfWorkWithoutResultMock);
        } catch (ServiceException e) {
            verify(transactionMock, times(1)).rollback();
            throw e;
        }
    }

}
