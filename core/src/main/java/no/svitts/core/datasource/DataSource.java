package no.svitts.core.datasource;

import java.sql.Connection;

public interface DataSource {

    Connection getConnection();
    void close();

}
