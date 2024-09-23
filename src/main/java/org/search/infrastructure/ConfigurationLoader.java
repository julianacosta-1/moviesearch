package org.search.infrastructure;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class ConfigurationLoader {
    private static final Logger logger = Logger.getLogger(ConfigurationLoader.class.getName());
    private final ClassLoader classLoader;

    public ConfigurationLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public Properties loadProperties() {
        Properties prop = new Properties();
        try (InputStream input = classLoader.getResourceAsStream("application.properties")) {
            if (input == null) {
                logger.severe("Sorry, unable to find application.properties");
                return prop;  // return empty properties object
            }
            prop.load(input);
        } catch (IOException ex) {
            logger.severe("Error loading properties: " + ex.getMessage());
        }
        return prop;
    }
}
