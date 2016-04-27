package no.svitts.core.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import no.svitts.core.application.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class SqlDataSource implements DataSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqlDataSource.class);
    private HikariDataSource hikariDataSource;

    public SqlDataSource(ApplicationProperties applicationProperties) {
        hikariDataSource = new HikariDataSource(getHikariConfig(applicationProperties));
    }

    @Override
    public Connection getConnection() throws SQLException {
        LOGGER.info("Getting connection from data source [{}]", hikariDataSource.toString());
        return hikariDataSource.getConnection();
    }

    @Override
    public void close() {
        LOGGER.info("Closing data source");
        hikariDataSource.close();
    }

    private HikariConfig getHikariConfig(ApplicationProperties applicationProperties) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(applicationProperties.get("jdbc.driverClass"));
        hikariConfig.setJdbcUrl(applicationProperties.get("jdbc.url"));
        hikariConfig.setUsername(applicationProperties.get("jdbc.username"));
        hikariConfig.setPassword(applicationProperties.get("jdbc.password"));
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        return hikariConfig;
    }
}
