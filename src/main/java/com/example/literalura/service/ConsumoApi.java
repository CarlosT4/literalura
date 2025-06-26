package com.example.literalura.service;

import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class ConsumoApi {
    
    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();
    
    public String obtenerDatos(String url) {
        System.out.println("Consultando URL: " + url); // Para debug
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(30))
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .GET()
                .build();
        
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            System.out.println("Código de respuesta: " + response.statusCode()); // Para debug
            
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new RuntimeException("Error HTTP: " + response.statusCode() + " - " + response.body());
            }
            
        } catch (IOException e) {
            throw new RuntimeException("Error de entrada/salida al consumir la API: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("La petición fue interrumpida: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al consumir la API: " + e.getMessage(), e);
        }
    }
}