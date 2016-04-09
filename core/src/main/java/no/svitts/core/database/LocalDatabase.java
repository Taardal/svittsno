package no.svitts.core.database;

import no.svitts.core.datasource.DataSource;
import no.svitts.core.reader.FileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class LocalDatabase {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalDatabase.class);
    private static final String DATABASE_SCHEMA_FILE_NAME = "database_schema.sql";

    public static void initialize(DataSource dataSource) {
        String sql = new FileReader().readResource(DATABASE_SCHEMA_FILE_NAME);
        List<String> statements = Arrays.asList(sql.split(";"));
        for (String statement : statements) {
            executeStatement(statement, dataSource);
        }
    }

    private static void executeStatement(String statement, DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
                LOGGER.info("Executing statement {}", preparedStatement.toString());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            LOGGER.error("Could not execute statement {}", statement, e);
        }
    }

}
