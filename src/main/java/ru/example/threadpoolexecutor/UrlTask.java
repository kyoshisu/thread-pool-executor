package ru.example.threadpoolexecutor;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class UrlTask implements Runnable {
    private final String url;
    private final HttpClient httpClient;

    public UrlTask(String url, HttpClient httpClient) {
        this.url = url;
        this.httpClient = httpClient;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.printf("[%s] %s - %d (%d мс)%n",
                    Thread.currentThread().getName(), url, response.statusCode(), duration);

        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            System.out.printf("[%s] %s - ОШИБКА (%d мс): %s%n",
                    Thread.currentThread().getName(), url, (endTime - startTime), e.getMessage());
        }
    }
}