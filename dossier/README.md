## MVP Level 4 Настройка уведомлений о ходе сделки.

## Описание проекта

В обязанности МС-dossier входит обработка сообщений из Кафки от МС-deal на каждом шаге, который требует отправки
письма на почту Клиенту

Взаимодействие между МС-deal и МС-dossier было реализовано через Kafka
МС-deal выступает в роли издателя (producer), МС-dossier в роли подписчика (consumer)
В кафке были заведены 6 топиков, соответствующие темам, по которым необходимо направить письмо на почту Клиенту:

- finish-registration
- create-documents
- send-documents
- send-ses
- credit-issued
- statement-denied

## Стек технологий

- Java 17
- Spring Boot
- Lombok
- Mockito
- Junit 5
- Maven
- Swagger
- Apache Kafka
- iText HTML2PDF

## Требования для запуска

- Java 17 или выше.
- Maven для управления зависимостями.
- Среда разработки (например IntelliJ IDEA) или консоль

## Как запустить приложение

## Взаимодействие

Приложение имеет интеграцию со Swagger для тестирования API.
После запуска перейдите по адресу:

для доступа к Swagger UI

```
http://localhost:8083/swagger-ui/index.html.
```

и API документации

```
http://localhost:8083/v1/api-docs
```