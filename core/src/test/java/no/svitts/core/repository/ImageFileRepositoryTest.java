package no.svitts.core.repository;

import no.svitts.core.datasource.DataSource;
import no.svitts.core.datasource.SqlDataSource;
import no.svitts.core.file.ImageFile;
import no.svitts.core.id.Id;
import no.svitts.core.testdatabuilder.ImageFileTestDataBuilder;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class ImageFileRepositoryTest {

    private DataSource mockDataSource;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    private ImageFileRepository imageFileRepository;
    private ImageFileTestDataBuilder imageFileTestDataBuilder;

    @Before
    public void setUp() throws Exception {
        mockDataSource = mock(SqlDataSource.class);
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockPreparedStatement.executeBatch()).thenReturn(new int[]{1});

        imageFileRepository = new ImageFileRepository(mockDataSource);
        imageFileTestDataBuilder = new ImageFileTestDataBuilder();
    }

    @Test
    public void getById_ShouldExecuteQueryAndReturnExpectedImageFile() throws SQLException {
        ImageFile imageFile = imageFileTestDataBuilder.build();
        setupMockResultSet(imageFile);

        ImageFile imageFileFromRepository = imageFileRepository.getById(imageFile.getId());

        assertImageFile(imageFile, imageFileFromRepository);
        verify(mockDataSource, times(1)).getConnection();
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).executeQuery();
        verify(mockResultSet, times(2)).next();
    }

    @Test
    public void insertSingle_ThrowsSQLException_ShouldHandleSQLExceptionAndReturnFalse() throws SQLException {
        when(mockDataSource.getConnection()).thenThrow(new SQLException());

        String success = imageFileRepository.insert(imageFileTestDataBuilder.build());

        verify(mockDataSource, times(1)).getConnection();
    }

    @Test
    public void updateSingle_ShouldExecuteStatementsAndReturnTrue() throws SQLException {
        ImageFile imageFile = imageFileTestDataBuilder.build();

        boolean success = imageFileRepository.update(imageFile);

        assertTrue(success);
        verify(mockDataSource, times(1)).getConnection();
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void updateSingle_ThrowsSQLException_ShouldHandleSQLExceptionAndReturnFalse() throws SQLException {
        when(mockDataSource.getConnection()).thenThrow(new SQLException());

        boolean success = imageFileRepository.update(imageFileTestDataBuilder.build());

        assertFalse(success);
        verify(mockDataSource, times(1)).getConnection();
    }

    @Test
    public void deleteSingle_ShouldExecuteStatementsAndReturnTrue() throws SQLException {
        ImageFile imageFile = imageFileTestDataBuilder.build();

        boolean success = imageFileRepository.delete(imageFile.getId());

        assertTrue(success);
        verify(mockDataSource, times(1)).getConnection();
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void deleteSingle_ThrowsSQLException_ShouldHandleSQLExceptionAndReturnFalse() throws SQLException {
        when(mockDataSource.getConnection()).thenThrow(new SQLException());

        boolean success = imageFileRepository.delete(Id.get());

        assertFalse(success);
        verify(mockDataSource, times(1)).getConnection();
    }

    private void setupMockResultSet(ImageFile imageFile) throws SQLException {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("id")).thenReturn(imageFile.getId());
        when(mockResultSet.getString("path")).thenReturn(imageFile.getPath());
    }

    private void assertImageFile(ImageFile expectedImageFile, ImageFile actualImageFile) {
        assertEquals(expectedImageFile.getId(), actualImageFile.getId());
        assertEquals(expectedImageFile.getPath(), actualImageFile.getPath());
    }
}
