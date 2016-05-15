package no.svitts.core.repository;

import no.svitts.core.datasource.DataSource;
import no.svitts.core.file.VideoFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VideoFileRepository extends SqlRepository<VideoFile> implements Repository<VideoFile> {

    public static final String UNKNOWN_VIDEO_FILE_ID = "Unknown-VideoFile-ID";
    private static final Logger LOGGER = LoggerFactory.getLogger(VideoFileRepository.class);

    public VideoFileRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<VideoFile> getAll() {
        LOGGER.info("Getting all video files");
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = getSelectAllVideoFilesPreparedStatement(connection)) {
                return executeQuery(preparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not get all video files", e);
            return new ArrayList<>();
        }
    }

    @Override
    public VideoFile getById(String id) {
        return getVideoFileById(id);
    }

    private VideoFile getVideoFileById(String id) {
        LOGGER.info("Getting video file with ID [{}]", id);
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = getSelectVideoFilePreparedStatement(connection, id)) {
                List<VideoFile> videoFiles = executeQuery(preparedStatement);
                return videoFiles.isEmpty() ? getUnknownVideoFile() : videoFiles.get(0);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not get video file with ID [{}]", id, e);
            return getUnknownVideoFile();
        }
    }

    @Override
    public boolean insert(VideoFile videoFile) {
        if (isRequiredFieldsValid(videoFile)) {
            return insertVideoFile(videoFile);
        } else {
            LOGGER.warn("Required fields INVALID for videoFile [{}]", videoFile);
            return false;
        }
    }

    @Override
    public boolean update(VideoFile videoFile) {
        LOGGER.info("Updating video file [" + videoFile.toString() + "]");
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = getUpdateVideoFilePreparedStatement(connection, videoFile)) {
                return executeUpdate(preparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not update videoFile [{}]", videoFile.toString(), e);
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        return id != null && !id.isEmpty() && deleteVideoFile(id);
    }

    @Override
    protected List<VideoFile> getResults(ResultSet resultSet) {
        List<VideoFile> videoFiles = new ArrayList<>();
        try {
            while (resultSet.next()) {
                videoFiles.add(new VideoFile(resultSet.getString("id"), resultSet.getString("path")));
            }
        } catch (SQLException e) {
            LOGGER.error("Could not get result(s) from result set [[{}]]", resultSet.toString(), e);
        }
        LOGGER.info("Got video file(s) [{}]", videoFiles.toString());
        return videoFiles;
    }

    @Override
    protected boolean isRequiredFieldsValid(VideoFile videoFile) {
        return videoFile.getId() != null && !videoFile.getId().isEmpty()
                && !videoFile.getName().isEmpty() && !videoFile.getName().equals("null")
                && !videoFile.getPath().isEmpty() && !videoFile.getName().equals("null");
    }

    private PreparedStatement getSelectAllVideoFilesPreparedStatement(Connection connection) throws SQLException {
        return connection.prepareStatement("SELECT * FROM video_file;");
    }

    private PreparedStatement getSelectVideoFilePreparedStatement(Connection connection, String id) throws SQLException {
        String query = "SELECT * FROM video_file WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, id);
        return preparedStatement;
    }

    private boolean insertVideoFile(VideoFile videoFile) {
        LOGGER.info("Inserting video file [" + videoFile.toString() + "]");
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = getInsertVideoFilePreparedStatement(connection, videoFile)) {
                return executeUpdate(preparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not insert videoFile [{}]", videoFile.toString(), e);
            return false;
        }
    }

    private PreparedStatement getInsertVideoFilePreparedStatement(Connection connection, VideoFile videoFile) throws SQLException {
        String statement = "INSERT INTO video_file VALUES (?, ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        int i = 1;
        preparedStatement.setString(i++, videoFile.getId());
        preparedStatement.setString(i, videoFile.getPath());
        return preparedStatement;
    }

    private PreparedStatement getUpdateVideoFilePreparedStatement(Connection connection, VideoFile videoFile) throws SQLException {
        String statement = "UPDATE video_file SET path = ? WHERE id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        int i = 1;
        preparedStatement.setString(i++, videoFile.getPath());
        preparedStatement.setString(i, videoFile.getId());
        return preparedStatement;
    }

    private boolean deleteVideoFile(String id) {
        LOGGER.info("Deleting video file with ID [{}]", id);
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = getDeleteVideoFilePreparedStatement(connection, id)) {
                return executeUpdate(preparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not delete video file with ID [{}]", id, e);
            return false;
        }
    }

    private PreparedStatement getDeleteVideoFilePreparedStatement(Connection connection, String id) throws SQLException {
        String statement = "DELETE FROM video_file WHERE id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        preparedStatement.setString(1, id);
        return preparedStatement;
    }

    private VideoFile getUnknownVideoFile() {
        return new VideoFile(UNKNOWN_VIDEO_FILE_ID, "");
    }

}
