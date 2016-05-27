package no.svitts.core.repository;

import no.svitts.core.datasource.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.InternalServerErrorException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

abstract class CoreRepository<T> implements Repository<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoreRepository.class);

    DataSource dataSource;

    CoreRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected abstract List<T> getResults(ResultSet resultSet);

    List<T> executeQuery(PreparedStatement preparedStatement) {
        LOGGER.info("Executing query [{}]", preparedStatement.toString());
        try (ResultSet resultSet = preparedStatement.executeQuery()){
            List<T> results = getResults(resultSet);
            LOGGER.info("Query got [{}] results", results.size());
            return results;
        } catch (SQLException e) {
            String errorMessage = "Could not execute query [" + preparedStatement.toString() + "]";
            LOGGER.error(errorMessage, e);
            throw new InternalServerErrorException(errorMessage, e);
        }
    }

    void executeUpdate(PreparedStatement preparedStatement) {
        LOGGER.info("Executing update [{}]", preparedStatement.toString());
        try {
            int rowsAffected = preparedStatement.executeUpdate();
            LOGGER.info("Update affected [{}] rows", rowsAffected);
        } catch (SQLException e) {
            String errorMessage = "Could not execute update [" + preparedStatement.toString() + "]";
            LOGGER.error(errorMessage, e);
            throw new InternalServerErrorException(errorMessage, e);
        }
    }


    void executeBatch(PreparedStatement preparedStatement) {
        LOGGER.info("Executing batch [{}]", preparedStatement.toString());
        try {
            int[] batchResults = preparedStatement.executeBatch();
            LOGGER.info("Batch affected [{}] rows", batchResults);
        } catch (SQLException e) {
            String errorMessage = "Could not execute batch [" + preparedStatement.toString() + "]";
            LOGGER.error(errorMessage, e);
            throw new InternalServerErrorException(errorMessage, e);
        }
    }

}
