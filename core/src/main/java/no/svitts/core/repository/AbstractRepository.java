package no.svitts.core.repository;

import no.svitts.core.database.DataSource;
import no.svitts.core.database.SvittsDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractRepository<T> {

    private static final Logger LOGGER = Logger.getLogger(SvittsDataSource.class.getName());
    protected DataSource dataSource;

    protected AbstractRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected List<T> executeQuery(String query) {
        LOGGER.log(Level.INFO, "Executing query [" + query + "]");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            return getResults(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not execute query [" + query + "]", e);
        } finally {
            closeResultSet(resultSet);
            closePreparedStatement(preparedStatement);
            closeConnection(connection);
        }
        return new ArrayList<>();
    }

    protected abstract List<T> getResults(ResultSet resultSet) throws SQLException;

    private void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Could not close result set", e);
            }
        } else {
            LOGGER.log(Level.WARNING, "Result set was null when trying to close it");
        }
    }

    private void closePreparedStatement(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Could not close prepared statement", e);
            }
        } else {
            LOGGER.log(Level.WARNING, "Prepared statement was null when trying to close it");
        }
    }

    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Could not close connection", e);
            }
        } else {
            LOGGER.log(Level.WARNING, "Connection was null when trying to close it");
        }
    }
}
