package no.svitts.core.provider;

import com.google.inject.Provider;
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
        Configuration configuration = new Configuration().configure("hibernate_sp.cfg.xml");
        StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        return configuration.buildSessionFactory(standardServiceRegistryBuilder.build());
    }

    private Properties getConnectionProviderProperties() {
        Properties properties = new Properties();
        properties.put(Environment.CONNECTION_PROVIDER, CoreDataSource.class.getName());
        return properties;
    }

}
