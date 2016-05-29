package no.svitts.core.repository;

import no.svitts.core.datasource.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.InternalServerErrorException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

abstract class CoreRepository<T> {

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
            String errorMessage = "Could not execute query";
            LOGGER.error(errorMessage + " [" + preparedStatement.toString() + "]", e);
            throw new InternalServerErrorException(errorMessage, e);
        }
    }

    void executeUpdate(PreparedStatement preparedStatement) {
        LOGGER.info("Executing updateSingle [{}]", preparedStatement.toString());
        try {
            int updateCount = preparedStatement.executeUpdate();
            LOGGER.info("Update updated [{}] rows", updateCount);
        } catch (SQLException e) {
            String errorMessage = "Could not execute update";
            LOGGER.error(errorMessage + " [" + preparedStatement.toString() + "]", e);
            throw new InternalServerErrorException(errorMessage, e);
        }
    }


    void executeBatch(PreparedStatement preparedStatement) {
        LOGGER.info("Executing batch [{}]", preparedStatement.toString());
        try {
            int[] updateCounts = preparedStatement.executeBatch();
            LOGGER.info("Batch updated {} rows", updateCounts);
        } catch (SQLException e) {
            String errorMessage = "Could not execute batch [" + preparedStatement.toString() + "]";
            LOGGER.error(errorMessage, e);
            throw new InternalServerErrorException(errorMessage, e);
        }
    }

}
