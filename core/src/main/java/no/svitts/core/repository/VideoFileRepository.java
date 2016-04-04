package no.svitts.core.repository;

import no.svitts.core.datasource.DataSource;
import no.svitts.core.file.UnknownVideoFile;
import no.svitts.core.file.VideoFile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VideoFileRepository extends MySqlRepository<VideoFile> implements Repository<VideoFile> {

    private static final Logger LOGGER = Logger.getLogger(VideoFile.class.getName());

    public VideoFileRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<VideoFile> getAll() {
        LOGGER.log(Level.INFO, "Getting all video files");
        try (Connection connection = dataSource.getConnection()){
            try (PreparedStatement selectAllVideoFilesPreparedStatement = getSelectAllVideoFilesPreparedStatement(connection)) {
                return executeQuery(selectAllVideoFilesPreparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not get all video files", e);
            return new ArrayList<>();
        }
    }

    @Override
    public VideoFile getById(String id) {
        LOGGER.log(Level.INFO, "Getting video file with ID [" + id + "]");
        try (Connection connection = dataSource.getConnection()){
            try (PreparedStatement selectVideoFilePreparedStatement = getSelectVideoFilePreparedStatement(id, connection)) {
                return executeQuery(selectVideoFilePreparedStatement).get(0);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not get video file with ID [" + id + "]", e);
            return new UnknownVideoFile();
        }
    }

    @Override
    public boolean insertSingle(VideoFile videoFile) {
        LOGGER.log(Level.INFO, "Inserting video file [" + videoFile.toString() + "]");
        try (Connection connection = dataSource.getConnection()){
            try (PreparedStatement insertVideoFilePreparedStatement = getInsertVideoFilePreparedStatement(videoFile, connection)) {
                executeUpdate(insertVideoFilePreparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not insert videoFile [" + videoFile.toString() + "]", e);
        }
        return false;
    }

    @Override
    public boolean updateSingle(VideoFile videoFile) {
        LOGGER.log(Level.INFO, "Updating video file [" + videoFile.toString() + "]");
        try (Connection connection = dataSource.getConnection()){
            try (PreparedStatement updateVideoFilePreparedStatement = getUpdateVideoFilePreparedStatement(videoFile, connection)) {
                executeUpdate(updateVideoFilePreparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not update videoFile [" + videoFile.toString() + "]", e);
        }
        return false;
    }

    @Override
    public boolean deleteSingle(String id) {
        LOGGER.log(Level.INFO, "Deleting video file with ID [" + id + "]");
        try (Connection connection = dataSource.getConnection()){
            try (PreparedStatement deleteVideoFilePreparedStatement = getDeleteVideoFilePreparedStatement(id, connection)) {
                executeUpdate(deleteVideoFilePreparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not delete video file with ID [" + id + "]", e);
        }
        return false;
    }

    @Override
    protected List<VideoFile> getResults(ResultSet resultSet) throws SQLException {
        List<VideoFile> videoFiles = new ArrayList<>();
        while (resultSet.next()) {
            String id = resultSet.getString("id");
            String movieId = resultSet.getString("movie_id");
            String name = resultSet.getString("name");
            String format = resultSet.getString("format");
            String quality = resultSet.getString("quality");
            String path = resultSet.getString("path");
            int size = resultSet.getInt("size");
            videoFiles.add(new VideoFile(path, id, movieId, name, format, quality, size));
        }
        LOGGER.log(Level.INFO, "Got video file(s) [" + videoFiles.toString() + "]");
        return videoFiles;
    }

    private PreparedStatement getSelectAllVideoFilesPreparedStatement(Connection connection) throws SQLException {
        return connection.prepareStatement("SELECT * FROM video_file;");
    }

    private PreparedStatement getSelectVideoFilePreparedStatement(String id, Connection connection) throws SQLException {
        String query = "SELECT * FROM video_file WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, id);
        return preparedStatement;
    }

    private PreparedStatement getInsertVideoFilePreparedStatement(VideoFile videoFile, Connection connection) throws SQLException {
        String statement = "INSERT INTO video_file VALUES (?, ?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        preparedStatement.setString(1, videoFile.getId());
        preparedStatement.setString(2, videoFile.getName());
        preparedStatement.setString(3, videoFile.getFormat());
        preparedStatement.setInt(4, videoFile.getSize());
        preparedStatement.setString(5, videoFile.getPath());
        preparedStatement.setString(6, videoFile.getQuality());
        return preparedStatement;
    }


    private PreparedStatement getUpdateVideoFilePreparedStatement(VideoFile videoFile, Connection connection) throws SQLException {
        String statement = "UPDATE video_file SET name = ?, format = ?, size = ?, path = ?, quality = ? WHERE id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        preparedStatement.setString(1, videoFile.getName());
        preparedStatement.setString(2, videoFile.getFormat());
        preparedStatement.setInt(3, videoFile.getSize());
        preparedStatement.setString(4, videoFile.getPath());
        preparedStatement.setString(5, videoFile.getQuality());
        preparedStatement.setString(6, videoFile.getId());
        return preparedStatement;
    }

    private PreparedStatement getDeleteVideoFilePreparedStatement(String id, Connection connection) throws SQLException {
        String statement = "DELETE FROM video_file WHERE id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        preparedStatement.setString(1, id);
        return preparedStatement;
    }

}
