package no.svitts.core.datasource;

import com.zaxxer.hikari.HikariConfig;

import java.util.Properties;

public class DataSourceConfig extends HikariConfig {

    public DataSourceConfig(Properties properties) {
        setDriverClassName(properties.getProperty("jdbc.driverClass"));
        setJdbcUrl(properties.getProperty("jdbc.url"));
        setUsername(properties.getProperty("jdbc.username"));
        setPassword(properties.getProperty("jdbc.password"));
        addDataSourceProperty("cachePrepStmts", "true");
        addDataSourceProperty("prepStmtCacheSize", "250");
        addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    }

}
