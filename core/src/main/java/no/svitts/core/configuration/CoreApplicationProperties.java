package no.svitts.core.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CoreApplicationProperties extends Properties implements ApplicationProperties {

    private static final String APPLICATION_PROPERTIES_FILE = "application.properties";

    public CoreApplicationProperties() {
        loadApplicationProperties(APPLICATION_PROPERTIES_FILE);
    }

    public CoreApplicationProperties(String propertiesFile) {
        loadApplicationProperties(propertiesFile);
    }

    @Override
    public String get(String key) {
        return getProperty(key);
    }

    private void loadApplicationProperties(String applicationPropertiesFile) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(applicationPropertiesFile)) {
            load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Could not load input stream for file [" + applicationPropertiesFile + "]");
        }
    }
}
