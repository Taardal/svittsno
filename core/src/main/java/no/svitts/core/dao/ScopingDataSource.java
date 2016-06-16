package no.svitts.core.dao;

import no.svitts.core.datasource.DataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ScopingDataSource {

    private static DataSource dataSource;
    private static Connection connection;

    public static void beginConnectionScope() {
        connection = getConnection();
    }

    public static void endConnectionScope() {
        if (connection != null) {
            if (!isConnectionClosed()) {
                closeConnection();
            }
            connection = null;
        }
    }

    public static void beginTransactionScope() {
        if (connection != null) {
            disableAutoCommit();
        }
    }

    public static void endTransactionScope() {
        if (connection != null) {
            commitConnection();
        }
    }

    public static void abortTransactionScope() {
        if (connection != null) {
            rollbackConnection();
        }
    }

    private static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isConnectionClosed() {
        try {
            return connection.isClosed();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void disableAutoCommit() {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void commitConnection() {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void rollbackConnection() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
