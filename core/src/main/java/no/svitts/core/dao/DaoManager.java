package no.svitts.core.dao;

import no.svitts.core.datasource.DataSource;
import no.svitts.core.repository.Repository;

import java.sql.Connection;
import java.sql.SQLException;

import static javafx.scene.input.KeyCode.R;
import static javafx.scene.input.KeyCode.T;

public class DaoManager<T> {

    private static DataSource dataSource;
    private static Connection connection;

    public static DaoManager beginTransaction(Repository<T> repository) {

    }

    public static <R, T> R transaction(Repository<T> repository) {
        try {
            beginConnectionScope();
            beginTransactionScope();
            R result = execute(repository);
            endTransactionScope();
            return result;
        } catch (Exception e) {
            abortTransactionScope();
            throw new RuntimeException(e);
        } finally {
            endConnectionScope();
        }
    }

    public static <R> R execute(Transaction<R> transaction) {
        return transaction.execute();
    }

    private static void beginConnectionScope() {
        connection = getConnection();
    }

    private static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void beginTransactionScope() {
        if (connection != null) {
            disableAutoCommit();
        }
    }

    private static void disableAutoCommit() {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void endTransactionScope() {
        if (connection != null) {
            commitConnection();
        }
    }

    private static void commitConnection() {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void abortTransactionScope() {
        if (connection != null) {
            rollbackConnection();
        }
    }

    private static void rollbackConnection() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void endConnectionScope() {
        if (connection != null) {
            if (!isConnectionClosed()) {
                closeConnection();
            }
            connection = null;
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

}
