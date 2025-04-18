package org.example;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.SimpleFileServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        try {
            Path path = Path.of("C:\\Users\\johed\\Desktop\\Java\\functional-server-library\\src");
            //path.toFile().mkdirs();

            InetSocketAddress address = new InetSocketAddress(8080);
            HttpServer server = SimpleFileServer.createFileServer(address, path, SimpleFileServer.OutputLevel.VERBOSE);
            System.out.println("Server started on port 8080, serving files from: " + path.toAbsolutePath());
            server.start();
            HttpHandler handler = SimpleFileServer.createFileHandler(path);
            server.createContext("/test", handler);
            server.createContext("/greet", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    String name = exchange.getRequestURI().getQuery(); // e.g., "/greet?name=John"
                    String response = "Hello, " + (name != null ? name : "Guest");

                    exchange.sendResponseHeaders(200, response.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            });
        } catch (Exception e) {
            System.err.println("Error starting server: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}