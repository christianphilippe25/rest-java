package org.example;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class ApiGateway {

    static ArrayList<String> items = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Rota para a API de items
        server.createContext("/items", new ItemsHandler());

        // Rota para a API de pessoas
        server.createContext("/persons", new PersonsHandler());

        server.setExecutor(null); // Usar o executor padrão
        server.start();
        System.out.println("API Gateway running on port 8080...");
    }

    static class ItemsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            String method = exchange.getRequestMethod();
            System.out.println("method: "+method);

            if(Objects.equals(method, "GET")){
                StringBuilder response = new StringBuilder("Items API");
                response.append("\n");

                for(String item: items){
                    response.append("item: ").append(item).append("\n");
                }

                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.toString().getBytes());
                os.close();
            }
            if(Objects.equals(method, "POST")){
                InputStream inputStream = exchange.getRequestBody();
                byte[] requestBodyBytes = inputStream.readAllBytes();
                String requestBody = new String(requestBodyBytes, StandardCharsets.UTF_8);
                System.out.println("requestBody: "+requestBody);
                items.add(requestBody);

                String response = "item added";

                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }

    static class PersonsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "Persons API";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
