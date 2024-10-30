package com.puvn.bean.container.http;

import com.puvn.bean.container.context.ContainerApplicationContext;
import com.puvn.bean.container.exception.http.HttpServerInitializeException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class SimpleHttpServer {

    private static final Logger LOGGER = Logger.getLogger(ContainerApplicationContext.class.getName());

    private final HttpServer server;

    private final Map<String, Method> handlerMapping;

    private final Executor virtualExecutor = Executors.newVirtualThreadPerTaskExecutor();

    public static void initialize(int port, Map<String, Class<?>> context) {
        Map<String, Method> handlerMapping = DispatcherHandler.handleControllers(context);
        if (handlerMapping.isEmpty()) {
            LOGGER.info("No HTTP Request mappings found, skipping server creation");
            return;
        }
        try {
            new SimpleHttpServer(port, handlerMapping);
        } catch (IOException e) {
            throw new HttpServerInitializeException(e);
        }
    }

    public SimpleHttpServer(int port, Map<String, Method> handlerMapping) throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        this.handlerMapping = handlerMapping;

        server.createContext("/", exchange -> {
            String path = exchange.getRequestURI().getPath();
            handleRequest(path, exchange);
        });

        server.setExecutor(virtualExecutor);
    }

    public void start() {
        server.start();
        LOGGER.info("Server started on port " + server.getAddress().getPort());
    }

    public void stop() {
        server.stop(0);
        System.out.println("Server stopped.");
    }

    private void handleRequest(String path, HttpExchange exchange) {
        Method method = handlerMapping.get(path);
        if (method != null) {
            virtualExecutor.execute(() -> {
                try {
                    method.invoke(method.getDeclaringClass().newInstance(), exchange);
                } catch (Exception e) {
                    LOGGER.severe(e.getMessage());
                }
            });
        } else {
            sendNotFoundResponse(exchange);
        }
    }

    private void sendNotFoundResponse(HttpExchange exchange) {
        // Handle 404 response here
    }

}
