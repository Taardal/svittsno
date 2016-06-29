package no.svitts.core.provider;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import no.svitts.core.genre.Genre;
import no.svitts.core.movie.Movie;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class ItestSessionFactoryProvider implements SessionFactoryProvider {

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
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        configuration.setProperty("hibernate.hikari.dataSource.jdbcUrl", "jdbc:h2:file./target/svitts_itest");
        configuration.addAnnotatedClass(Movie.class);
        configuration.addAnnotatedClass(Genre.class);
        return configuration.buildSessionFactory(new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
//                .applySetting(Environment.DATASOURCE, new HikariDataSource(getHikariConfig()))
                .build());
    }

    private static HikariConfig getHikariConfig() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:h2:file./target/svitts_itest");
        hikariConfig.setUsername("svitts");
        hikariConfig.setPassword("11JhR5QL");
//        hikariConfig.setDriverClassName("org.h2.Driver");
        hikariConfig.setDataSourceClassName(HikariDataSource.class.getName());
        hikariConfig.addDataSourceProperty("minimumIdle", "5");
        hikariConfig.addDataSourceProperty("maximumPoolSize", "10");
        hikariConfig.addDataSourceProperty("idleTimeout", "3000");
        return hikariConfig;
    }

}
