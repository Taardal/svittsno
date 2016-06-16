package no.svitts.core.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.engine.jdbc.connections.internal.UserSuppliedConnectionProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class CoreDataSource extends UserSuppliedConnectionProviderImpl implements DataSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoreDataSource.class);

    private HikariDataSource hikariDataSource;

    public CoreDataSource(DataSourceConfig dataSourceConfig) {
        hikariDataSource = new HikariDataSource(dataSourceConfig);
    }

    @Override
    public Connection getConnection() throws SQLException {
        LOGGER.debug("Getting connection.");
        return hikariDataSource.getConnection();
    }

    @Override
    public void closeConnection(Connection connection) throws SQLException {
        LOGGER.debug("Closing connection [{}].", connection);
        connection.close();
    }

    @Override
    public void close() {
        LOGGER.debug("Closing data source.");
        hikariDataSource.close();
    }

}
