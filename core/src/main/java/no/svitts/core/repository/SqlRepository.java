package no.svitts.core.repository;

import no.svitts.core.database.DataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class SqlRepository<T> {

    protected static final int UPDATE_ERROR_CODE = 0;
    private static final Logger LOGGER = Logger.getLogger(SqlRepository.class.getName());
    protected DataSource dataSource;

    protected SqlRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected int executeUpdate(PreparedStatement preparedStatement) throws SQLException {
        try {
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not execute update [" + preparedStatement.toString() + "]");
        } finally {
            preparedStatement.close();
            preparedStatement.getConnection().close();
        }
        return UPDATE_ERROR_CODE;
    }

    protected List<T> executeQuery(PreparedStatement preparedStatement) throws SQLException {
        try {
            return getResultsFromResultSet(preparedStatement.executeQuery());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not execute query [" + preparedStatement.toString() + "]");
        } finally {
            preparedStatement.close();
            preparedStatement.getConnection().close();
        }
        return new ArrayList<>();
    }

    private List<T> getResultsFromResultSet(ResultSet resultSet) throws SQLException {
        try {
            return getResults(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not get results from [" + resultSet.toString() + "]");
        } finally {
            resultSet.close();
        }
        return new ArrayList<>();
    }

    protected abstract List<T> getResults(ResultSet resultSet) throws SQLException;

}
