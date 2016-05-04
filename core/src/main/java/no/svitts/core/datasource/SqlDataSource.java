package no.svitts.core.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class SqlDataSource implements DataSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqlDataSource.class);
    private HikariDataSource hikariDataSource;

    public SqlDataSource(DataSourceConfig dataSourceConfig) {
        hikariDataSource = new HikariDataSource(dataSourceConfig);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }

    @Override
    public void close() {
        LOGGER.info("Closing data source");
        hikariDataSource.close();
    }

}
