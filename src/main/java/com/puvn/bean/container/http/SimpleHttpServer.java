package com.puvn.bean.container.http;

import com.puvn.bean.container.config.PropertyConfigLoader;
import com.puvn.bean.container.context.ContainerApplicationContext;
import com.puvn.bean.container.exception.http.HttpServerInitializeException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleHttpServer {

    private static SimpleHttpServer instance;

    private static final Logger LOGGER = Logger.getLogger(ContainerApplicationContext.class.getName());

    private final HttpServer server;

    private final Map<String, Method> handlerMapping;

    private static final int DEFAULT_HTTP_SERVER_PORT = 8080;

    public static final String HTTP_SERVER_PORT_KEY = "http.server.port";

    private final Executor virtualExecutor = Executors.newVirtualThreadPerTaskExecutor();

    public static void initialize(PropertyConfigLoader configLoader, Map<String, Class<?>> context) {
        Map<String, Method> handlerMapping = DispatcherHandler.handleControllers(context);
        if (handlerMapping.isEmpty()) {
            LOGGER.info("No HTTP Request mappings found, skipping server creation");
            return;
        }
        try {
            instance = new SimpleHttpServer(
                    configLoader.getIntPropertyOrDefault(HTTP_SERVER_PORT_KEY, DEFAULT_HTTP_SERVER_PORT),
                    handlerMapping
            );
            instance.start();
        } catch (IOException e) {
            throw new HttpServerInitializeException(e);
        }
    }

    public static void destroy() {
        if (instance != null) {
            instance.stop();
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

        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    private void start() {
        server.start();
        LOGGER.info("Server started on port " + server.getAddress().getPort());
    }

    private void stop() {
        LOGGER.info("Stopping http server... ");
        server.stop(0);
    }

    private void handleRequest(String path, HttpExchange exchange) {
        Method method = handlerMapping.get(path);
        if (method != null) {
            virtualExecutor.execute(() -> {
                try {
                    method.invoke(method.getDeclaringClass().getConstructors()[0].newInstance(), exchange);
                } catch (Exception e) {
                    LOGGER.severe(e.getMessage());
                }
            });
        } else {
            sendNotFoundResponse(exchange);
        }
    }

    private void sendNotFoundResponse(HttpExchange exchange) {
        String response = "HTTP/1.1 404 Not Found\r\nContent-Type: text/html\r\n\r\n";
        try (exchange) {
            exchange.sendResponseHeaders(404, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error sending 404 response", e);
        }
    }

}
