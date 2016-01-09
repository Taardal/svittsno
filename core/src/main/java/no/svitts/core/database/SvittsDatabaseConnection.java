package no.svitts.core.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SvittsDatabaseConnection implements DatabaseConnection {

    private static final Logger LOGGER = Logger.getLogger(SvittsDatabaseConnection.class.getName());
    private static final String HOST = "localhost";
    private static final String DATABASE = "svitts";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "admin";
    private Connection connection;

    @Override
    public void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + HOST + "/" + DATABASE + "?user=" + USERNAME + "&password=" + PASSWORD);
            LOGGER.log(Level.INFO, "Connected to Svitts database");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not connect to Svitts database", e);
        }
    }

    @Override
    public void disconnect() {
        try {
            connection.close();
            LOGGER.log(Level.INFO, "Disconnected from Svitts database");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not disconnect from Svitts database", e);
        }
    }

    @Override
    public ResultSet executeQuery(String query) {
        LOGGER.log(Level.INFO, "Executing query [" + query + "] on Svitts database");
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
            return connection.prepareStatement(query).executeQuery();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not execute query [" + query + "] on Svitts database", e);
            return new DefaultResultSet();
        }
    }
}
