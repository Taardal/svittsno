package no.svitts.core.repository;

import no.svitts.core.datasource.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class SqlRepository<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqlRepository.class);
    protected DataSource dataSource;

    protected SqlRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected List<T> executeQuery(PreparedStatement preparedStatement) {
        LOGGER.info("Executing query [{}]", preparedStatement.toString());
        try (ResultSet resultSet = preparedStatement.executeQuery()){
            return getResults(resultSet);
        } catch (SQLException e) {
            LOGGER.error("Could not execute query [{}]", preparedStatement.toString(), e);
            return new ArrayList<>();
        }
    }

    protected boolean executeUpdate(PreparedStatement preparedStatement) {
        LOGGER.info("Executing update [{}]", preparedStatement.toString());
        try {
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.error("Could not execute update [{}]", preparedStatement.toString(), e);
            return false;
        }
    }

    protected boolean executeBatch(PreparedStatement preparedStatement) {
        LOGGER.info("Executing batch [{}]", preparedStatement.toString());
        try {
            int[] results = preparedStatement.executeBatch();
            return Arrays.stream(results).allMatch(rowsAffected -> rowsAffected > 0);
        } catch (SQLException e) {
            LOGGER.error("Could not execute batch [{}]", preparedStatement.toString(), e);
            return false;
        }
    }


    protected abstract List<T> getResults(ResultSet resultSet);

    protected abstract boolean isRequiredFieldsValid(T t);

}
