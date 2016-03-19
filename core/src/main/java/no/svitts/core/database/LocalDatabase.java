package no.svitts.core.database;

import no.svitts.core.datasource.DataSource;
import no.svitts.core.reader.FileReader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LocalDatabase {

    private static final Logger LOGGER = Logger.getLogger(LocalDatabase.class.getName());
    private static final String DATABASE_SCHEMA_FILE_NAME = "database_schema.sql";
    private DataSource dataSource;

    public LocalDatabase(DataSource dataSource) {
        this.dataSource = dataSource;
        createTables();
    }

    private void createTables() {
        String sql = new FileReader().readResource(DATABASE_SCHEMA_FILE_NAME);
        List<String> statements = Arrays.asList(sql.split(";"));
        statements.forEach(this::executeStatement);
    }

    private void executeStatement(String statement) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
                LOGGER.log(Level.INFO, "Executing statement [" + preparedStatement.toString() + "]");
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not execute statement [" + statement + "]", e);
        }
    }

}
