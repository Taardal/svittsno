package no.svitts.core.testkit;

import no.svitts.core.application.ApplicationProperties;
import no.svitts.core.datasource.CoreDataSource;
import no.svitts.core.datasource.DataSource;
import no.svitts.core.datasource.DataSourceConfig;

public class ITestKit {

    private ITestKit() {
    }

    public static DataSource getDataSource() {
        return new CoreDataSource(getITestDataSourceConfig());
    }

    private static DataSourceConfig getITestDataSourceConfig() {
        ApplicationProperties applicationProperties = new ApplicationProperties();
        return new DataSourceConfig(
                applicationProperties.get("db.itest.url"),
                applicationProperties.get("db.username"),
                applicationProperties.get("db.password"),
                applicationProperties.get("db.driver"));
    }

}
