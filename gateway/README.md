## MVP Level 5. Реализация паттерна API-Gateway

## Описание проекта

Микросервис API Gateway реализует паттерн API-Gateway для системы кредитного конвейера. Его задача — предоставить
клиенту единый интерфейс для взаимодействия с несколькими микросервисами, скрывая внутреннюю логику системы и
обеспечивая простоту и удобство API. Вместо того, чтобы клиент отправлял запросы напрямую в различные микросервисы с
бизнес-логикой, все запросы проходят через API Gateway, который распределяет их и направляет к нужным сервисам.

## Стек технологий

- Java 17
- Spring Boot
- Lombok
- Mockito
- Junit 5
- Maven
- Swagger

## Требования для запуска

- Java 17 или выше.
- Maven для управления зависимостями.
- Среда разработки (например IntelliJ IDEA) или консоль

## Как запустить приложение

```bash
git clone https://github.com/AnutaVacsh/CreditBank 
cd gateway
```

Перейти в корень и собрать проект:

```bash
mvn clean package
```

После сборки, запустить проект:

```bash
java -jar target/gateway-0.0.1-SNAPSHOT.jar
```

## Взаимодействие

Приложение имеет интеграцию со Swagger для тестирования API.
После запуска перейдите по адресу:

для доступа к Swagger UI

```
http://localhost:8084/swagger-ui/index.html.
```

и API документации

```
http://localhost:8084/v1/api-docs
```

Прескоринг + запрос на расчёт возможных условий кредита
![reqStatement.png](img%2FreqStatement.png)
![resStatement.png](img%2FresStatement.png)

Выбор одного из предложений.
![reqSelect.png](img%2FreqSelect.png)
![resSelect.png](img%2FresSelect.png)

Завершение регистрации + полный подсчёт кредита
![reqRegistr.png](img%2FreqRegistr.png)
![resRegistr.png](img%2FresRegistr.png)

Запрос на отправку документов
![send.png](img%2Fsend.png)

Запрос на подписание документов
![sign.png](img%2Fsign.png)

Подписание документов
![code.png](img%2Fcode.png)