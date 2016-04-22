package no.svitts.core.datasource;

import com.zaxxer.hikari.HikariConfig;
import no.svitts.core.application.ApplicationProperties;

public class DataSourceConfig extends HikariConfig {

    public DataSourceConfig(ApplicationProperties applicationProperties) {
        setDriverClassName(applicationProperties.get("jdbc.driverClass"));
        setJdbcUrl(applicationProperties.get("jdbc.url"));
        setUsername(applicationProperties.get("jdbc.username"));
        setPassword(applicationProperties.get("jdbc.password"));
        addDataSourceProperty("cachePrepStmts", "true");
        addDataSourceProperty("prepStmtCacheSize", "250");
        addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    }

}
