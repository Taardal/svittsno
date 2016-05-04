package no.svitts.core.datasource;

import com.zaxxer.hikari.HikariConfig;

public class DataSourceConfig extends HikariConfig {

    public DataSourceConfig(String driverClass, String username, String password, String url) {
        setDriverClassName(driverClass);
        setUsername(username);
        setPassword(password);
        setJdbcUrl(url);
        addDataSourceProperty("cachePrepStmts", "true");
        addDataSourceProperty("prepStmtCacheSize", "250");
        addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    }

}
