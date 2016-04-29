package no.svitts.core.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class SqlExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqlExecutor.class);

    public ResultSet executeQuery(PreparedStatement preparedStatement) {
        LOGGER.info("Executing query [{}]", preparedStatement.toString());
        try {
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            LOGGER.error("Could not execute query [{}]", preparedStatement.toString(), e);
            return new FailedQueryResultSet();
        }
    }

    public boolean executeUpdate(PreparedStatement preparedStatement) {
        LOGGER.info("Executing update [{}]", preparedStatement.toString());
        try {
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.error("Could not execute update [{}]", preparedStatement.toString(), e);
            return false;
        }
    }

    public boolean executeBatch(PreparedStatement preparedStatement) {
        LOGGER.info("Executing batch [{}]", preparedStatement.toString());
        try {
            int[] results = preparedStatement.executeBatch();
            return Arrays.stream(results).allMatch(rowsAffected -> rowsAffected > 0);
        } catch (SQLException e) {
            LOGGER.error("Could not execute batch [{}]", preparedStatement.toString(), e);
            return false;
        }
    }
    
}
