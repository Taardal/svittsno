package no.svitts.core.database;

import java.sql.ResultSet;

public interface DatabaseConnection {
    void connect();

    void disconnect();

    ResultSet executeQuery(String query);
}
