package no.svitts.core.repository;

import no.svitts.core.datasource.DataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class SqlRepository<T> {

    protected static final int UPDATE_ERROR_CODE = 0;
    protected DataSource dataSource;
    private static final Logger LOGGER = Logger.getLogger(SqlRepository.class.getName());

    protected SqlRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected int executeUpdate(PreparedStatement preparedStatement) {
        LOGGER.log(Level.INFO, "Executing update [" + preparedStatement.toString() + "]");
        try {
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not execute update [" + preparedStatement.toString() + "]");
            return UPDATE_ERROR_CODE;
        }
    }

    protected List<T> executeQuery(PreparedStatement preparedStatement) {
        LOGGER.log(Level.INFO, "Executing query [" + preparedStatement.toString() + "]");
        try (ResultSet resultSet = preparedStatement.executeQuery()){
            return getResults(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not execute query [" + preparedStatement.toString() + "]", e);
            return new ArrayList<>();
        }
    }

    protected abstract List<T> getResults(ResultSet resultSet) throws SQLException;

}
