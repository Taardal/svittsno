package no.svitts.core.datasource;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class SqlDataSource implements DataSource {

    private HikariDataSource hikariDataSource;

    public SqlDataSource(DataSourceConfig dataSourceConfig) {
        hikariDataSource = new HikariDataSource(dataSourceConfig);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }
}
