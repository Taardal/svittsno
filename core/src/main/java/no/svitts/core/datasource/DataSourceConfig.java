package no.svitts.core.datasource;

import com.zaxxer.hikari.HikariConfig;

public class DataSourceConfig extends HikariConfig {

    public DataSourceConfig(String url, String username, String password, String driverClass) {
        setJdbcUrl(url);
        setUsername(username);
        setPassword(password);
        setDriverClassName(driverClass);
        addDataSourceProperty("cachePrepStmts", "true");
        addDataSourceProperty("prepStmtCacheSize", "250");
        addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    }

}
