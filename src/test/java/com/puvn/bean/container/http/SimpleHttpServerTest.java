package com.puvn.bean.container.http;

import com.puvn.bean.container.config.PropertyConfigLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static com.puvn.bean.container.http.ContextUtil.prepareEmptyControllerContext;
import static com.puvn.bean.container.http.ContextUtil.prepareFullControllerContext;
import static com.puvn.bean.container.http.ContextUtil.prepareInvalidControllerContext;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleHttpServerTest {

    Map<String, Class<?>> context;
    PropertyConfigLoader configLoader;

    @BeforeEach
    void setUp() {
        context = new HashMap<>();
        configLoader = new PropertyConfigLoader();
    }

    @AfterEach
    void tearDown() {
        SimpleHttpServer.destroy();
    }

    @Test
    public void httpServerShouldStart() {
        context = prepareFullControllerContext();
        assertFalse(isPortInUse(configLoader.getIntPropertyOrDefault(SimpleHttpServer.HTTP_SERVER_PORT_KEY, 8080)));
        SimpleHttpServer.initialize(configLoader, context);
        assertTrue(isPortInUse(configLoader.getIntPropertyOrDefault(SimpleHttpServer.HTTP_SERVER_PORT_KEY, 8080)));
    }

    @Test
    public void httpServerShouldNotStartForEmptyContext() {
        context = prepareEmptyControllerContext();
        assertFalse(isPortInUse(configLoader.getIntPropertyOrDefault(SimpleHttpServer.HTTP_SERVER_PORT_KEY, 8080)));
        SimpleHttpServer.initialize(configLoader, context);
        assertFalse(isPortInUse(configLoader.getIntPropertyOrDefault(SimpleHttpServer.HTTP_SERVER_PORT_KEY, 8080)));
    }

    @Test
    public void httpServerShouldNotStartForInvalidContext() {
        context = prepareInvalidControllerContext();
        assertFalse(isPortInUse(configLoader.getIntPropertyOrDefault(SimpleHttpServer.HTTP_SERVER_PORT_KEY, 8080)));
        SimpleHttpServer.initialize(configLoader, context);
        assertFalse(isPortInUse(configLoader.getIntPropertyOrDefault(SimpleHttpServer.HTTP_SERVER_PORT_KEY, 8080)));
    }

    private boolean isPortInUse(int port) {
        try (Socket ignored = new Socket("localhost", port)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}