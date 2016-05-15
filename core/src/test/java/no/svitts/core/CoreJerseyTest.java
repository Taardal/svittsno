package no.svitts.core;

import com.google.gson.Gson;
import no.svitts.core.application.ApplicationProperties;
import no.svitts.core.datasource.DataSource;
import no.svitts.core.datasource.DataSourceConfig;
import no.svitts.core.datasource.SqlDataSource;
import no.svitts.core.resource.CoreResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import java.util.Random;

public abstract class CoreJerseyTest extends JerseyTest {

    protected abstract Gson getGson();

    protected <T extends CoreResource> ResourceConfig getResourceConfig(T resource) {
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(resource);
        return resourceConfig;
    }

    protected DataSource getDataSource() {
        return new SqlDataSource(getDataSourceConfig(new ApplicationProperties()));
    }

    private DataSourceConfig getDataSourceConfig(ApplicationProperties applicationProperties) {
        return new DataSourceConfig(
                applicationProperties.get("db.driver"),
                applicationProperties.get("db.username"),
                applicationProperties.get("db.password"),
                applicationProperties.get("db.itest.url")
        );
    }

    protected String getRandomString(int length) {
        if (length > 0) {
            String alphabet = "abcdefghijklmnopqrstuvwxyz";
            Random random = new Random();
            String string = "";
            for (int i = 0; i < length; i++) {
                int j = random.nextInt(alphabet.length());
                string += alphabet.charAt(j);
            }
            return string;
        } else {
            throw new IllegalArgumentException("Could not get random string. Length of requested string must be greater than 0");
        }
    }

}
