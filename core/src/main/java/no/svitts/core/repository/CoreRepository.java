package no.svitts.core.repository;

import no.svitts.core.datasource.DataSource;
import no.svitts.core.exception.RepositoryException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

abstract class CoreRepository<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoreRepository.class);

    DataSource dataSource;

    private static SessionFactory sessionFactory;

    CoreRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = buildSessionFactory();
        }
        return sessionFactory;
    }

    private Session getSession() {
        return getSessionFactory().getCurrentSession();
    }

    private static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration().configure("hibernate_sp.cfg.xml");
        StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        return configuration.buildSessionFactory(ssrb.build());
    }

    protected abstract List<T> getResults(ResultSet resultSet);

    List<T> executeQuery(PreparedStatement preparedStatement) {
        LOGGER.info("Executing query [{}]", preparedStatement.toString());
        try (ResultSet resultSet = preparedStatement.executeQuery()){
            List<T> results = getResults(resultSet);
            LOGGER.info("Query got [{}] results", results.size());
            return results;
        } catch (SQLException e) {
            String errorMessage = "Could not execute query [" + preparedStatement.toString() + "]";
            LOGGER.error(errorMessage, e);
            throw new RepositoryException(errorMessage, e);
        }
    }

    void executeUpdate(PreparedStatement preparedStatement) {
        LOGGER.info("Executing updateSingle [{}]", preparedStatement.toString());
        try {
            int updateCount = preparedStatement.executeUpdate();
            LOGGER.debug("Update updated [{}] rows", updateCount);
        } catch (SQLException e) {
            String errorMessage = "Could not execute update [" + preparedStatement.toString() + "]";
            LOGGER.error(errorMessage, e);
            throw new RepositoryException(errorMessage, e);
        }
    }

    void executeBatch(PreparedStatement preparedStatement) {
        LOGGER.info("Executing batch [{}]", preparedStatement.toString());
        try {
            int[] updateCounts = preparedStatement.executeBatch();
            LOGGER.debug("Batch updated {} rows", updateCounts);
        } catch (SQLException e) {
            String errorMessage = "Could not execute batch [" + preparedStatement.toString() + "]";
            LOGGER.error(errorMessage, e);
            throw new RepositoryException(errorMessage, e);
        }
    }

}
