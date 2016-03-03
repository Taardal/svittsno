package no.svitts.core.repository;

import no.svitts.core.database.DataSource;
import no.svitts.core.file.VideoFile;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VideoFileRepository extends AbstractRepository<VideoFile> implements Repository<VideoFile> {

    private static final Logger LOGGER = Logger.getLogger(VideoFile.class.getName());

    public VideoFileRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<VideoFile> getAll() {
        String query = "SELECT * FROM video_file;";
        return executeQuery(query);
    }

    @Override
    public VideoFile getById(int id) {
        String query = "SELECT * FROM video_file v JOIN movie_video_file m ON v.id = m.video_file_id WHERE m.movie_id = " + id + ";";
        return executeQuery(query).get(0);
    }

    @Override
    public void update(int id) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    protected List<VideoFile> getResults(ResultSet resultSet) throws SQLException {
        List<VideoFile> videoFiles = new ArrayList<>();
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            String format = resultSet.getString("format");
            int movieId = resultSet.getInt("movie_id");
            int size = resultSet.getInt("size");
            int directoryId = resultSet.getInt("directory_id");
            String path = resultSet.getString("path");
            videoFiles.add(new VideoFile(path, movieId, name, format, directoryId, size));
        }
        LOGGER.log(Level.INFO, "Got video file(s) [" + videoFiles.toString() + "]");
        return videoFiles;
    }

}
