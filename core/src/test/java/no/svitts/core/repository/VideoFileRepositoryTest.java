package no.svitts.core.repository;

import no.svitts.core.datasource.DataSource;
import no.svitts.core.datasource.SqlDataSource;
import no.svitts.core.file.VideoFile;
import no.svitts.core.id.Id;
import no.svitts.core.testdatabuilder.VideoFileTestDataBuilder;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class VideoFileRepositoryTest {

    private DataSource mockDataSource;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    private VideoFileRepository videoFileRepository;
    private VideoFileTestDataBuilder videoFileTestDataBuilder;

    @Before
    public void setUp() throws SQLException {
        mockDataSource = mock(SqlDataSource.class);
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockPreparedStatement.executeBatch()).thenReturn(new int[]{1});

        videoFileRepository = new VideoFileRepository(mockDataSource);
        videoFileTestDataBuilder = new VideoFileTestDataBuilder();
    }

    @Test
    public void getById_ShouldExecuteQueryAndReturnExpectedVideoFile() throws SQLException {
        VideoFile videoFile = videoFileTestDataBuilder.build();
        setupMockResultSet(videoFile);

        VideoFile videoFileFromRepository = videoFileRepository.getById(videoFile.getId());


        assertVideoFile(videoFile, videoFileFromRepository);
        verify(mockDataSource, times(1)).getConnection();
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).executeQuery();
        verify(mockResultSet, times(2)).next();
    }

    @Test
    public void getByID_ThrowsSQLException_ShouldHandleSQLExceptionAndReturnUnknownVideoFile() throws SQLException {
        when(mockDataSource.getConnection()).thenThrow(new SQLException());

        VideoFile videoFile = videoFileRepository.getById(Id.get());

        assertEquals(VideoFileRepository.UNKNOWN_VIDEO_FILE_ID, videoFile.getId());
        verify(mockDataSource, times(1)).getConnection();
    }

    @Test
    public void insertSingle_ShouldExecuteStatementsAndReturnTrue() throws SQLException {
        VideoFile videoFile = videoFileTestDataBuilder.path("test").build();

        boolean success = videoFileRepository.insert(videoFile);

        assertTrue(success);
        verify(mockDataSource, times(1)).getConnection();
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void insertSingle_RequiredFieldsInvalid_ShouldNotExecuteStatementsAndReturnFalse() throws SQLException {
        VideoFile videoFile = videoFileTestDataBuilder.id("").path("").build();

        boolean success = videoFileRepository.insert(videoFile);

        assertFalse(success);
        verify(mockDataSource, times(0)).getConnection();
        verify(mockConnection, times(0)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(0)).executeUpdate();
        verify(mockPreparedStatement, times(0)).executeBatch();
    }

    @Test
    public void insertSingle_ThrowsSQLException_ShouldHandleSQLExceptionAndReturnFalse() throws SQLException {
        when(mockDataSource.getConnection()).thenThrow(new SQLException());

        boolean success = videoFileRepository.insert(videoFileTestDataBuilder.build());

        assertFalse(success);
        verify(mockDataSource, times(1)).getConnection();
    }

    @Test
    public void updateSingle_ShouldExecuteStatementsAndReturnTrue() throws SQLException {
        VideoFile videoFile = videoFileTestDataBuilder.build();

        boolean success = videoFileRepository.update(videoFile);

        assertTrue(success);
        verify(mockDataSource, times(1)).getConnection();
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void updateSingle_ThrowsSQLException_ShouldHandleSQLExceptionAndReturnFalse() throws SQLException {
        when(mockDataSource.getConnection()).thenThrow(new SQLException());

        boolean success = videoFileRepository.update(videoFileTestDataBuilder.build());

        assertFalse(success);
        verify(mockDataSource, times(1)).getConnection();
    }

    @Test
    public void deleteSingle_ShouldExecuteStatementsAndReturnTrue() throws SQLException {
        VideoFile videoFile = videoFileTestDataBuilder.build();

        boolean success = videoFileRepository.delete(videoFile.getId());

        assertTrue(success);
        verify(mockDataSource, times(1)).getConnection();
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void deleteSingle_ThrowsSQLException_ShouldHandleSQLExceptionAndReturnFalse() throws SQLException {
        when(mockDataSource.getConnection()).thenThrow(new SQLException());

        boolean success = videoFileRepository.delete(Id.get());

        assertFalse(success);
        verify(mockDataSource, times(1)).getConnection();
    }

    private void setupMockResultSet(VideoFile videoFile) throws SQLException {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("id")).thenReturn(videoFile.getId());
        when(mockResultSet.getString("path")).thenReturn(videoFile.getPath());
    }

    private void assertVideoFile(VideoFile expectedVideoFile,  VideoFile actualVideoFile) {
        assertEquals(expectedVideoFile.getId(), actualVideoFile.getId());
        assertEquals(expectedVideoFile.getPath(), actualVideoFile.getPath());
    }

}
