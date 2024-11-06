package com.puvn.bean.container.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PropertyConfigLoaderTest {

    private PropertyConfigLoader configLoader;

    @BeforeEach
    void setUp() {
        configLoader = new PropertyConfigLoader();
    }

    @Test
    void shouldLoadPropertyConfig() {
        var properties = configLoader.getProperties();
        assertNotNull(properties);
    }

    @Test
    void shouldGetExistingPropertyValue() {
        assertEquals(101, configLoader.getIntPropertyOrDefault("test.project.int.property", 102));
    }

    @Test
    void shouldReturnDefaultPropertyValue() {
        assertEquals(102, configLoader.getIntPropertyOrDefault("test.unknown.int.property", 102));
    }

    @Test
    void shouldReturnStringValue() {
        assertEquals("test_string_value", configLoader.getProperty("test.project.string.property"));
    }

}