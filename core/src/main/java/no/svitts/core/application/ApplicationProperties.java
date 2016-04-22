package no.svitts.core.application;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationProperties {

    private static final String APPLICATION_PROPERTIES_FILE = "application.properties";
    private Properties properties;

    public ApplicationProperties() {
        properties = getApplicationProperties();
    }

    public String get(String property) {
        return properties.getProperty(property);
    }

    private Properties getApplicationProperties() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(APPLICATION_PROPERTIES_FILE)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Could not load input stream for file [" + APPLICATION_PROPERTIES_FILE + "]");
        }
    }
}
