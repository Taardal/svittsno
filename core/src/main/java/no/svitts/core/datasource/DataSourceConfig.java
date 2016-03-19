package no.svitts.core.datasource;

import com.zaxxer.hikari.HikariConfig;
import org.constretto.annotation.Configuration;
import org.constretto.annotation.Configure;

public class DataSourceConfig extends HikariConfig {

    @Configure
    public DataSourceConfig(
            @Configuration("jdbc.driverClass") String jdbcDriverClass,
            @Configuration("jdbc.url") String jdbcUrl,
            @Configuration("jdbc.username") String jdbcUsername,
            @Configuration("jdbc.password") String jdbcPassword
    ) {
        setDriverClassName(jdbcDriverClass);
        setJdbcUrl(jdbcUrl);
        setUsername(jdbcUsername);
        setPassword(jdbcPassword);
        addDataSourceProperty("cachePrepStmts", "true");
        addDataSourceProperty("prepStmtCacheSize", "250");
        addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    }


}
