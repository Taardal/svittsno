package no.svitts.core.repository;

import no.svitts.core.datasource.DataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class MySqlRepository<T> {

    private static final Logger LOGGER = Logger.getLogger(MySqlRepository.class.getName());
    protected DataSource dataSource;

    protected MySqlRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected boolean executeUpdate(PreparedStatement preparedStatement) {
        LOGGER.log(Level.INFO, "Executing update [" + preparedStatement.toString() + "]");
        try {
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not execute update [" + preparedStatement.toString() + "]", e);
        }
        return false;
    }

    protected List<T> executeQuery(PreparedStatement preparedStatement) {
        LOGGER.log(Level.INFO, "Executing query [" + preparedStatement.toString() + "]");
        try (ResultSet resultSet = preparedStatement.executeQuery()){
            return getResults(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not execute query [" + preparedStatement.toString() + "]", e);
        }
        return new ArrayList<>();
    }

    protected abstract List<T> getResults(ResultSet resultSet) throws SQLException;

}
