package no.svitts.core.provider;

import com.google.inject.Provider;
import com.zaxxer.hikari.HikariConfig;
import no.svitts.core.application.ApplicationProperties;
import no.svitts.core.datasource.CoreDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.util.Properties;


public class SessionFactoryProvider implements Provider<SessionFactory> {

    private static SessionFactory sessionFactory;

    @Override
    public SessionFactory get() {
        return getSessionFactory();
    }

    private static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = buildSessionFactory();
        }
        return sessionFactory;
    }

    private static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
        StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        return configuration.buildSessionFactory(standardServiceRegistryBuilder.build());
    }

    private Properties getConnectionProviderProperties() {
        Properties properties = new Properties();
        properties.put(Environment.CONNECTION_PROVIDER, CoreDataSource.class.getName());
        return properties;
    }

    private HikariConfig getHikariConfig(ApplicationProperties applicationProperties) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(applicationProperties.get("db.main.url"));
        hikariConfig.setUsername(applicationProperties.get("db.username"));
        hikariConfig.setPassword(applicationProperties.get("db.password"));
        hikariConfig.setDriverClassName(applicationProperties.get("db.driver"));
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        return hikariConfig;
    }

}
