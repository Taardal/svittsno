package no.svitts.core.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationProperties extends Properties {

    private static final String APPLICATION_PROPERTIES = "application.properties";

    public ApplicationProperties() {
        loadProperties(APPLICATION_PROPERTIES);
    }

    public ApplicationProperties(String propertiesFile) {
        loadProperties(propertiesFile);
    }

    private void loadProperties(String applicationPropertiesFile) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(applicationPropertiesFile)) {
            load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Could not load input stream for file [" + applicationPropertiesFile + "]");
        }
    }
}
