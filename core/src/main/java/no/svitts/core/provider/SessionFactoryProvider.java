package no.svitts.core.provider;

import com.google.inject.Provider;
import com.google.inject.Singleton;
import no.svitts.core.configuration.ApplicationProperties;
import no.svitts.core.genre.Genre;
import no.svitts.core.movie.Movie;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;


public class SessionFactoryProvider implements Provider<SessionFactory> {

    public static final String HIBERNATE_PROPERTIES = "hibernate.properties";
    public static final String HIBERNATE_ITEST_PROPERTIES = "hibernate.itest.properties";

    private SessionFactory sessionFactory;

    public SessionFactoryProvider() {
        ApplicationProperties hibernateProperties = new ApplicationProperties(HIBERNATE_PROPERTIES);
        sessionFactory = buildSessionFactory(getConfiguration(hibernateProperties));
    }

    public SessionFactoryProvider(String hibernatePropertiesFile) {
        ApplicationProperties hibernateProperties = new ApplicationProperties(hibernatePropertiesFile);
        sessionFactory = buildSessionFactory(getConfiguration(hibernateProperties));
    }

    @Singleton
    @Override
    public SessionFactory get() {
        return sessionFactory;
    }

    private SessionFactory buildSessionFactory(Configuration configuration) {
        return configuration.buildSessionFactory(new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build());
    }

    private Configuration getConfiguration(ApplicationProperties hibernateProperties) {
        Configuration configuration = new Configuration();
        configuration.setProperties(hibernateProperties);
        getAnnotatedClasses().forEach(configuration::addAnnotatedClass);
        return configuration;
    }

    private List<Class<?>> getAnnotatedClasses() {
        List<Class<?>> annotatedClasses = new ArrayList<>();
        annotatedClasses.add(Movie.class);
        annotatedClasses.add(Genre.class);
        return annotatedClasses;
    }

}
