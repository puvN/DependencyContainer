package com.puvn.bean.container.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertyConfigLoader {
    private static final Logger logger = Logger.getLogger(PropertyConfigLoader.class.getName());

    private static final String APPLICATION_PROPERTIES_FILENAME = "application.properties";

    private final Properties properties = new Properties();

    public PropertyConfigLoader() {
        loadProperties();
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream(PropertyConfigLoader.APPLICATION_PROPERTIES_FILENAME)) {
            if (input == null) {
                logger.log(Level.WARNING,
                        "Configuration file \"{0}\" not found; using default settings.",
                        PropertyConfigLoader.APPLICATION_PROPERTIES_FILENAME);
                return;
            }
            properties.load(input);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading properties file: " +
                    PropertyConfigLoader.APPLICATION_PROPERTIES_FILENAME, e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public int getIntPropertyOrDefault(String key, int defaultValue) {
        return Integer.parseInt(properties.getProperty(key, String.valueOf(defaultValue)));
    }

}
