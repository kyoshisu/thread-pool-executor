package ru.example.threadpoolexecutor;

import java.net.http.HttpClient;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainApp {

    public static void main(String[] args) throws InterruptedException {
        List<String> urls = Arrays.asList(
                "https://api.github.com",
                "https://jsonplaceholder.typicode.com/posts/1",
                "https://httpbin.org/get",
                "https://catfact.ninja/fact",
                "https://official-joke-api.appspot.com/random_joke",
                "https://dog.ceo/api/breeds/image/random",
                "https://api.agify.io?name=michael",
                "https://api.genderize.io?name=luc",
                "https://api.nationalize.io?name=nathaniel",
                "https://api.ipify.org?format=json",
                "https://randomuser.me/api/",
                "https://api.spacexdata.com/v4/launches/latest",
                "https://api.coindesk.com/v1/bpi/currentprice.json",
                "https://api.publicapis.org/entries",
                "https://api.punkapi.com/v2/beers/random",
                "https://api.kanye.rest",
                "https://api.chucknorris.io/jokes/random",
                "https://v2.jokeapi.dev/joke/Any",
                "https://api.thecatapi.com/v1/images/search",
                "https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&current_weather=true"
        );

        System.out.println("Всего URL для обработки: " + urls.size());

        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(java.time.Duration.ofSeconds(5))
                .build();

        int corePoolSize = 5;
        int maximumPoolSize = 10;
        long keepAliveTime = 60;
        TimeUnit unit = TimeUnit.SECONDS;

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                new LinkedBlockingQueue<>(20),
                new ThreadPoolExecutor.AbortPolicy()
        );

        System.out.println("\nЗапуск задач в пуле потоков");
        long startTotal = System.currentTimeMillis();

        for (String url : urls) {
            executor.execute(new UrlTask(url, httpClient));
        }

        executor.shutdown();

        if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
            System.out.println("Не все задачи успели завершиться за 10 секунд. Принудительное завершение...");
            executor.shutdownNow();
        }

        long endTotal = System.currentTimeMillis();

        System.out.println("\nСводка");
        System.out.println("Общее время выполнения: " + (endTotal - startTotal) + " мс");
        System.out.println("Статус пула: " + (executor.isTerminated() ? "Завершен" : "Активен"));
    }
}