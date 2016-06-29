package no.svitts.core.provider;

import org.hibernate.cfg.Configuration;

public class ItestSessionFactoryProvider extends SessionFactoryProvider {

    @Override
    Configuration getConfiguration() {
        Configuration configuration = new Configuration();
//        configuration.setProperties(new ApplicationProperties("hibernate.itest.properties"));
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        configuration.setProperty("hibernate.hikari.dataSource.jdbcUrl", "jdbc:h2:file./target/svitts_itest");
        return configuration;
    }

}
