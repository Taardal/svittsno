package no.svitts.core.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public class SvittsDataSource implements DataSource {

    private static final Logger LOGGER = Logger.getLogger(SvittsDataSource.class.getName());
    private HikariDataSource hikariDataSource;

    public SvittsDataSource() {
        hikariDataSource = new HikariDataSource(getHikariConfig());
    }

    @Override
    public Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }

    private HikariConfig getHikariConfig() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://localhost:3306/svitts");
        hikariConfig.setUsername("root");
        hikariConfig.setPassword("admin");
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        return hikariConfig;
    }
}
