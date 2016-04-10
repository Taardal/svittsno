package no.svitts.core.repository;

import no.svitts.core.datasource.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class SqlRepository<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqlRepository.class);
    protected DataSource dataSource;

    protected SqlRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected boolean executeUpdate(PreparedStatement preparedStatement) {
        LOGGER.info("Executing update {}", preparedStatement.toString());
        try {
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.error("Could not execute update {}", preparedStatement.toString(), e);
            return false;
        }
    }

    protected List<T> executeQuery(PreparedStatement preparedStatement) {
        LOGGER.info("Executing query {}", preparedStatement.toString());
        try (ResultSet resultSet = preparedStatement.executeQuery()){
            return getResults(resultSet);
        } catch (SQLException e) {
            LOGGER.error("Could not execute query {}", preparedStatement.toString(), e);
            return new ArrayList<>();
        }
    }

    protected abstract List<T> getResults(ResultSet resultSet) throws SQLException;

}
