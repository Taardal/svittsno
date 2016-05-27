package no.svitts.core.repository;

import no.svitts.core.datasource.CoreDataSource;
import org.junit.Test;

import javax.ws.rs.InternalServerErrorException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CoreRepositoryTest {

    private CoreRepositoryMock coreRepositoryMock;
    private PreparedStatement preparedStatementMock;

    public CoreRepositoryTest() throws SQLException {
        preparedStatementMock = mock(PreparedStatement.class);
        coreRepositoryMock = new CoreRepositoryMock(mock(CoreDataSource.class));
    }

    @Test
    public void executeQuery_ShouldReturnListWithResults() throws Exception {
        List<String> list = coreRepositoryMock.executeQuery(preparedStatementMock);
        assertEquals(1, list.size());
    }

    @Test(expected = InternalServerErrorException.class)
    public void executeQuery_ThrowsSQLException_ShouldCatchSQLExceptionAndThrowInternalServerException() throws Exception {
        when(preparedStatementMock.executeQuery()).thenThrow(new SQLException());
        coreRepositoryMock.executeQuery(preparedStatementMock);
    }

    @Test(expected = InternalServerErrorException.class)
    public void executeUpdate_ThrowsSQLException_ShouldCatchSQLExceptionAndThrowInternalServerException() throws Exception {
        when(preparedStatementMock.executeUpdate()).thenThrow(new SQLException());
        coreRepositoryMock.executeUpdate(preparedStatementMock);
    }

    @Test(expected = InternalServerErrorException.class)
    public void executeBatch_ThrowsSQLException_ShouldCatchSQLExceptionAndThrowInternalServerException() throws Exception {
        when(preparedStatementMock.executeBatch()).thenThrow(new SQLException());
        coreRepositoryMock.executeBatch(preparedStatementMock);
    }
}
