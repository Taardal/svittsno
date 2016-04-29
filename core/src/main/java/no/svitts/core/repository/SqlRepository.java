package no.svitts.core.repository;

import no.svitts.core.datasource.DataSource;
import no.svitts.core.sql.SqlExecutor;
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
    private SqlExecutor sqlExecutor;

    public SqlRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        this.sqlExecutor = new SqlExecutor();
    }

    protected abstract List<T> getResults(ResultSet resultSet) throws SQLException;

    protected abstract boolean isRequiredFieldsValid(T t);

    protected List<T> executeQuery(PreparedStatement preparedStatement) {
        ResultSet resultSet = sqlExecutor.executeQuery(preparedStatement);
        return getResultsFromResultSet(resultSet);
    }

    protected boolean executeUpdate(PreparedStatement preparedStatement) {
        return sqlExecutor.executeUpdate(preparedStatement);
    }

    protected boolean executeBatch(PreparedStatement preparedStatement) {
        return sqlExecutor.executeBatch(preparedStatement);
    }

    private List<T> getResultsFromResultSet(ResultSet resultSet) {
        try {
            return getResults(resultSet);
        } catch (SQLException e) {
            LOGGER.error("Could not get results from result set [{}]", resultSet.toString(), e);
            return new ArrayList<>();
        } finally {
            closeResultSet(resultSet);
        }
    }

    private void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                LOGGER.error("Could not close result set [{}]", resultSet.toString(), e);
            }
        } else {
            LOGGER.warn("Result set was null when trying to close it");
        }
    }

}
