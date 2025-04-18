package org.example;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.SimpleFileServer;

import java.net.InetSocketAddress;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        try {
            Path path = Path.of("C:\\Users\\johed\\Desktop\\Java\\functional-server-library\\build");
            path.toFile().mkdirs();

            InetSocketAddress address = new InetSocketAddress(8080);
            HttpServer server = SimpleFileServer.createFileServer(address, path, SimpleFileServer.OutputLevel.VERBOSE);
            System.out.println("Server started on port 8080, serving files from: " + path.toAbsolutePath());
            server.start();
        } catch (Exception e) {
            System.err.println("Error starting server: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}