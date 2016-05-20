package no.svitts.core.repository;

import no.svitts.core.criteria.SearchCriteria;
import no.svitts.core.criteria.SearchKey;
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

public class VideoFileRepository extends CoreRepository<VideoFile> implements Repository<VideoFile> {

    public static final String UNKNOWN_VIDEO_FILE_ID = "Unknown-VideoFile-ID";
    private static final Logger LOGGER = LoggerFactory.getLogger(VideoFileRepository.class);

    public VideoFileRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public VideoFile getById(String id) {
        return getVideoFile(id);
    }

    @Override
    public boolean insert(VideoFile videoFile) {
        if (isRequiredFieldsValid(videoFile)) {
            return insertVideoFile(videoFile);
        } else {
            LOGGER.warn("Could not validate required fields when asked to insert videoFile [{}]", videoFile);
            return false;
        }
    }

    @Override
    public boolean update(VideoFile videoFile) {
        if (isRequiredFieldsValid(videoFile)) {
            return updateVideoFile(videoFile);
        } else {
            LOGGER.warn("Could not validate required fields when asked to update videoFile [{}]", videoFile);
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        return id != null && !id.isEmpty() && deleteVideoFile(id);
    }

    @Override
    public List<VideoFile> search(SearchCriteria searchCriteria) {
        if (searchCriteria.getKey() == SearchKey.NAME) {
            return searchVideoFilesByName(searchCriteria);
        } else {
            LOGGER.warn("Could not resolve search criteria [{}] when asked to search video files", searchCriteria);
            return new ArrayList<>();
        }
    }

    @Override
    protected List<VideoFile> getResults(ResultSet resultSet) {
        List<VideoFile> videoFiles = new ArrayList<>();
        try {
            while (resultSet.next()) {
                videoFiles.add(new VideoFile(resultSet.getString("id"), resultSet.getString("path")));
            }
        } catch (SQLException e) {
            LOGGER.error("Could not get result(s) from result set [{}]", resultSet.toString(), e);
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

    private VideoFile getVideoFile(String id) {
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

    private VideoFile getUnknownVideoFile() {
        return new VideoFile(UNKNOWN_VIDEO_FILE_ID, "");
    }

    private PreparedStatement getSelectVideoFilePreparedStatement(Connection connection, String id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM video_file WHERE id = ?");
        preparedStatement.setString(1, id);
        return preparedStatement;
    }

    private boolean insertVideoFile(VideoFile videoFile) {
        LOGGER.info("Inserting video file [{}]", videoFile.toString());
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
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO video_file VALUES (?, ?);");
        int i = 1;
        preparedStatement.setString(i++, videoFile.getId());
        preparedStatement.setString(i, videoFile.getPath());
        return preparedStatement;
    }

    private boolean updateVideoFile(VideoFile videoFile) {
        LOGGER.info("Updating video file [{}]", videoFile.toString());
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = getUpdateVideoFilePreparedStatement(connection, videoFile)) {
                return executeUpdate(preparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not update videoFile [{}]", videoFile.toString(), e);
            return false;
        }
    }

    private PreparedStatement getUpdateVideoFilePreparedStatement(Connection connection, VideoFile videoFile) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE video_file SET path = ? WHERE id = ?;");
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
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM video_file WHERE id = ?;");
        preparedStatement.setString(1, id);
        return preparedStatement;
    }

    private List<VideoFile> searchVideoFilesByName(SearchCriteria searchCriteria) {
        LOGGER.info("Searching video files by name [{}]", searchCriteria.getValue());
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = getSelectVideoFilesByNamePreparedStatement(connection, searchCriteria)) {
                return executeQuery(preparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not search video files by name", e);
            return new ArrayList<>();
        }
    }

    private PreparedStatement getSelectVideoFilesByNamePreparedStatement(Connection connection, SearchCriteria searchCriteria) throws SQLException {
        String sql = "SELECT * FROM video_file WHERE name LIKE ? LIMIT ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        int i = 1;
        preparedStatement.setString(i++, "%" + searchCriteria.getValue() + "%");
        preparedStatement.setInt(i, searchCriteria.getLimit());
        return preparedStatement;
    }

}
