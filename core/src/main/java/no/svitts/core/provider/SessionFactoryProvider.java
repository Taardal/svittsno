package no.svitts.core.provider;

import com.google.inject.Provider;
import com.google.inject.Singleton;
import no.svitts.core.genre.Genre;
import no.svitts.core.movie.Movie;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;


public class SessionFactoryProvider implements Provider<SessionFactory> {

    private SessionFactory sessionFactory;

    public SessionFactoryProvider() {
        sessionFactory = buildSessionFactory(getConfiguration());
    }

    @Singleton
    @Override
    public SessionFactory get() {
        return sessionFactory;
    }

    Configuration getConfiguration() {
        return new Configuration();
    }

    private SessionFactory buildSessionFactory(Configuration configuration) {
        getAnnotatedClasses().forEach(configuration::addAnnotatedClass);
        return configuration.buildSessionFactory(new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build());
    }

    private List<Class<?>> getAnnotatedClasses() {
        List<Class<?>> annotatedClasses = new ArrayList<>();
        annotatedClasses.add(Movie.class);
        annotatedClasses.add(Genre.class);
        return annotatedClasses;
    }

}
