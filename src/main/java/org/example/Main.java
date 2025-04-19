package org.example;
import com.sun.net.httpserver.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) {
        Path path = Path.of("C:\\Users\\johed\\Desktop\\Java\\functional-server-library\\src");

        InetSocketAddress address = new InetSocketAddress(8080);
        HttpServer server = SimpleFileServer.createFileServer(address, path, SimpleFileServer.OutputLevel.VERBOSE);
        System.out.println("Server started on port 8080, serving files from: " + path.toAbsolutePath());
        server.start();
        HttpHandler handler = SimpleFileServer.createFileHandler(path);
        server.createContext("/test", handler);
        HttpHandler allowedResponse = HttpHandlers.of(200, Headers.of("Allow", "GET"), "Welcome");
        HttpHandler deniedResponse = HttpHandlers.of(401, Headers.of("Deny", "GET"), "Denied");
        Predicate<Request> findAllowedPath = r -> r.getRequestURI().getPath().equals("/test/allowed");
        HttpHandler handler1 = HttpHandlers.handleOrElse(findAllowedPath, allowedResponse, deniedResponse);
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
    }
}