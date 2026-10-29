# Отчёт по заданию: Пул потоков (Thread Pool)

## 1. Цель работы
Научиться работать с различными типами пулов потоков. Получить навыки настройки пула потоков, управления задачами и мониторинга состояния пула.

## 2. Этапы выполнения задания
1. **Подготовка списка URL**: Создан список из 20 публичных API (GitHub API, JSONPlaceholder, httpbin и др.).
2. **Реализация задачи (UrlTask)**: Создан класс, реализующий `Runnable`. Внутри метода `run()` он использует `HttpClient` для отправки HTTP GET-запроса, замеряет время выполнения и выводит статус-код и время ответа в консоль.
3. **Настройка ThreadPoolExecutor**: Создан собственный пул потоков с параметрами:
   - `corePoolSize = 5` (базовое количество потоков).
   - `maximumPoolSize = 10` (максимальное количество).
   - `keepAliveTime = 60` секунд (время жизни лишних потоков).
   - `LinkedBlockingQueue(20)` (очередь задач).
   - `AbortPolicy` (политика поведения при переполнении).
4. **Запуск и мониторинг**: Задачи отправлены в пул с помощью метода `execute()`. После отправки вызван `shutdown()`, который запрещает добавление новых задач, но позволяет завершиться текущим. С помощью `awaitTermination()` основной поток ожидает завершения работы пула.

## 3. Промежуточные результаты (вывод консоли)
*Всего URL для обработки: 20

 Запуск задач в пуле потоков
 [pool-1-thread-1] https://api.github.com - 200 (418 мс)
 [pool-1-thread-2] https://jsonplaceholder.typicode.com/posts/1 - 200 (491 мс)
 [pool-1-thread-3] https://httpbin.org/get - 200 (565 мс)
 [pool-1-thread-4] https://catfact.ninja/fact - 200 (772 мс)
 [pool-1-thread-5] https://official-joke-api.appspot.com/random_joke - 200 (1140 мс)
 [pool-1-thread-2] https://api.agify.io?name=michael - 200 (677 мс)
 [pool-1-thread-3] https://api.genderize.io?name=luc - 200 (649 мс)
 [pool-1-thread-1] https://dog.ceo/api/breeds/image/random - 200 (893 мс)
 [pool-1-thread-5] https://api.ipify.org?format=json - 200 (289 мс)
 [pool-1-thread-4] https://api.nationalize.io?name=nathaniel - 200 (659 мс)
 [pool-1-thread-1] https://api.coindesk.com/v1/bpi/currentprice.json - ОШИБКА (125 мс): null
 [pool-1-thread-4] https://api.punkapi.com/v2/beers/random - ОШИБКА (119 мс): null
 [pool-1-thread-5] https://api.publicapis.org/entries - ОШИБКА (311 мс): null
 [pool-1-thread-2] https://randomuser.me/api/ - 200 (690 мс)
 [pool-1-thread-1] https://api.kanye.rest - 200 (472 мс)
 [pool-1-thread-5] https://v2.jokeapi.dev/joke/Any - 200 (656 мс)
 [pool-1-thread-2] https://api.thecatapi.com/v1/images/search - 200 (771 мс)
 [pool-1-thread-4] https://api.chucknorris.io/jokes/random - 200 (1299 мс)
 [pool-1-thread-1] https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&current_weather=true - ОШИБКА (5001 мс): HTTP connect timed out
 Не все задачи успели завершиться за 10 секунд. Принудительное завершение...

 Сводка
 Общее время выполнения: 10003 мс
 Статус пула: Активен
 [pool-1-thread-3] https://api.spacexdata.com/v4/launches/latest - ОШИБКА (8788 мс): null

 Process finished with exit code 0*

## 4. Объяснение использования ThreadPoolExecutor
`ThreadPoolExecutor` — это гибкий механизм управления потоками. В отличие от фабричных методов (`Executors.newFixedThreadPool()`), он позволяет явно задать все параметры:
- **corePoolSize**: Количество потоков, которые поддерживаются в пуле постоянно.
- **maximumPoolSize**: Верхняя граница потоков, которая создается при заполнении очереди.
- **Queue**: Очередь для задач, ожидающих выполнения. Мы использовали `LinkedBlockingQueue`, которая идеально подходит для передачи задач между потоками.
- **RejectedExecutionHandler**: Определяет, что делать, если пул и очередь переполнены (в нашем случае задача отклоняется с исключением).

## 5. Выводы
Использование пула потоков — это стандартная практика для задач с сетью, вводом-выводом или большим количеством независимых операций. Пул решает проблему накладных расходов на постоянное создание и уничтожение потоков, а также позволяет ограничить максимальное количество одновременных запросов (что важно, чтобы не получить бан от серверов). Настройка `ThreadPoolExecutor` вручную дает полный контроль над этим процессом.