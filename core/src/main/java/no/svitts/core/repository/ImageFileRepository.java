package no.svitts.core.repository;

import no.svitts.core.criteria.SearchCriteria;
import no.svitts.core.criteria.SearchKey;
import no.svitts.core.datasource.DataSource;
import no.svitts.core.file.ImageFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImageFileRepository extends CoreRepository<ImageFile> implements Repository<ImageFile> {

    public static final String UNKNOWN_IMAGE_FILE_ID = "Unknown-ImageFile-ID";
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageFileRepository.class);

    public ImageFileRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ImageFile getById(String id) {
        return getImageFile(id);
    }

    @Override
    public boolean insert(ImageFile imageFile) {
        if (isRequiredFieldsValid(imageFile)) {
            return insertImageFile(imageFile);
        } else {
            LOGGER.warn("Could not validate required fields when asked to insert imageFile [{}]", imageFile);
            return false;
        }
    }

    @Override
    public boolean update(ImageFile imageFile) {
        if (isRequiredFieldsValid(imageFile)) {
            return updateImageFile(imageFile);
        } else {
            LOGGER.warn("Could not validate required fields when asked to update imageFile [{}]", imageFile);
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        return id != null && !id.isEmpty() && deleteImageFile(id);
    }

    @Override
    public List<ImageFile> search(SearchCriteria searchCriteria) {
        if (searchCriteria.getKey() == SearchKey.NAME) {
            return searchImageFilesByName(searchCriteria);
        } else {
            LOGGER.warn("Could not resolve search criteria [{}] when asked to search image files", searchCriteria);
            return new ArrayList<>();
        }
    }

    @Override
    protected List<ImageFile> getResults(ResultSet resultSet) {
        List<ImageFile> imageFiles = new ArrayList<>();
        try {
            while (resultSet.next()) {
                imageFiles.add(new ImageFile(resultSet.getString("id"), resultSet.getString("path")));
            }
        } catch (SQLException e) {
            LOGGER.error("Could not get result(s) from result set [{}]", resultSet.toString(), e);
        }
        LOGGER.info("Got image file(s) [{}]", imageFiles.toString());
        return imageFiles;
    }

    @Override
    protected boolean isRequiredFieldsValid(ImageFile imageFile) {
        return imageFile.getId() != null && !imageFile.getId().isEmpty() && !imageFile.getPath().isEmpty();
    }

    private ImageFile getImageFile(String id) {
        LOGGER.info("Getting image file by ID [{}]", id);
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = getSelectImageFilePreparedStatement(connection, id)) {
                List<ImageFile> imageFiles = executeQuery(preparedStatement);
                return imageFiles.isEmpty() ? getUnknownImageFile() : imageFiles.get(0);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not get image file by ID", e);
            return getUnknownImageFile();
        }
    }

    private PreparedStatement getSelectImageFilePreparedStatement(Connection connection, String id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM image_file WHERE id = ?;");
        preparedStatement.setString(1, id);
        return preparedStatement;
    }

    private ImageFile getUnknownImageFile() {
        return new ImageFile(UNKNOWN_IMAGE_FILE_ID, "");
    }

    private boolean insertImageFile(ImageFile imageFile) {
        LOGGER.info("Inserting image file [" + imageFile.toString() + "]");
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = getInsertImageFilePreparedStatement(connection, imageFile)) {
                return executeUpdate(preparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not insert imageFile [{}]", imageFile.toString(), e);
            return false;
        }
    }

    private PreparedStatement getInsertImageFilePreparedStatement(Connection connection, ImageFile imageFile) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO image_file VALUES (?, ?);");
        int i = 1;
        preparedStatement.setString(i++, imageFile.getId());
        preparedStatement.setString(i, imageFile.getPath());
        return preparedStatement;
    }

    private boolean updateImageFile(ImageFile imageFile) {
        LOGGER.info("Updating image file [{}]", imageFile.toString());
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = getUpdateImageFilePreparedStatement(connection, imageFile)) {
                return executeUpdate(preparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not update imageFile [{}]", imageFile.toString(), e);
            return false;
        }
    }

    private PreparedStatement getUpdateImageFilePreparedStatement(Connection connection, ImageFile imageFile) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE image_file SET path = ? WHERE id = ?;");
        int i = 1;
        preparedStatement.setString(i++, imageFile.getPath());
        preparedStatement.setString(i, imageFile.getId());
        return preparedStatement;
    }

    private boolean deleteImageFile(String id) {
        LOGGER.info("Deleting image file with ID [{}]", id);
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = getDeleteImageFilePreparedStatement(connection, id)) {
                return executeUpdate(preparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not delete image file with ID [{}]", id, e);
            return false;
        }
    }

    private PreparedStatement getDeleteImageFilePreparedStatement(Connection connection, String id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM image_file WHERE id = ?;");
        preparedStatement.setString(1, id);
        return preparedStatement;
    }

    private List<ImageFile> searchImageFilesByName(SearchCriteria searchCriteria) {
        LOGGER.info("Searching image files by name [{}]", searchCriteria.getValue());
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = getSelectImageFilesByNamePreparedStatement(connection, searchCriteria)) {
                return executeQuery(preparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not search image files by name", e);
            return new ArrayList<>();
        }
    }

    private PreparedStatement getSelectImageFilesByNamePreparedStatement(Connection connection, SearchCriteria searchCriteria) throws SQLException {
        String sql = "SELECT * FROM image_file WHERE name LIKE ? LIMIT ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        int i = 1;
        preparedStatement.setString(i++, "%" + searchCriteria.getValue() + "%");
        preparedStatement.setInt(i, searchCriteria.getLimit());
        return preparedStatement;
    }
}
