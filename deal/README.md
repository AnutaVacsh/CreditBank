## MVP Level 2 реализация микросервиса сделка (Deal)

## Описание проекта

Микросервис Deal предназначен для управления процессом подачи и обработки заявок на кредит.

Пользователь отправляет заявку на кредит через эндпоинт /deal/statement, предоставляя информацию в формате
[LoanStatementRequestDto.java](src%2Fmain%2Fjava%2Fru%2Fvaschenko%2Fdeal%2Fdto%2FLoanStatementRequestDto.java). На
основе этих данных в базе данных создаются два объекта:
клиент ([Client.java](src%2Fmain%2Fjava%2Fru%2Fvaschenko%2Fdeal%2Fmodels%2FClient.java)) и заявка (
[Statement.java](src%2Fmain%2Fjava%2Fru%2Fvaschenko%2Fdeal%2Fmodels%2FStatement.java)). После этого сервис делает запрос
к микросервису "Калькулятор" на эндпоинт /calculator/offers, чтобы получить
список возможных условий кредита. Каждому предложению добавляется идентификатор заявки, и список возвращается клиенту,
упорядоченный от наименее выгодного к наиболее выгодному.

Далее пользователь выбирает одно из предложений через эндпоинт /deal/offer/select, микросервис обновляет соответствующую
заявку. Находится объект Statement по переданному идентификатору, обновляются его статус, история статусов и информация
о выбранном предложении. Эти изменения сохраняются в базе данных.

Для завершения регистрации и окончательного расчета кредита пользователь отправляет данные через эндпоинт
/deal/calculate/{statementId}. На основе этих данных и информации о клиенте формируется
объект [ScoringDataDto.java](src%2Fmain%2Fjava%2Fru%2Fvaschenko%2Fdeal%2Fdto%2FScoringDataDto.java), который
отправляется в микросервис "Калькулятор" на эндпоинт /calculator/calc. Результат расчета, используется для создания
сущности Credit, которая сохраняется в базе данных. Заявка обновляется.

## Стек технологий

- Java 17
- Spring Boot
- Lombok
- Mockito
- Junit 5
- Maven
- Swagger
- MapStruct
- PostgreSQL
- Liquibase
- Apache Kafka

## Требования для запуска

- Java 17 или выше.
- Maven для управления зависимостями.
- Среда разработки (например IntelliJ IDEA) или консоль

## Как запустить приложение

```bash
git clone https://github.com/AnutaVacsh/CreditBank 
cd deal
```

Перейти в корень и собрать проект:

```bash
mvn clean package
```

После сборки, запустить проект:

```bash
java -jar target/deal-0.0.1-SNAPSHOT.jar
```

## Взаимодействие

Приложение имеет интеграцию со Swagger для тестирования API.
После запуска перейдите по адресу:

```
http://localhost:8081/swagger-ui/index.html.
```

расчёт возможных условий кредита
![statementRes.png](img%2FstatementRes.png)
![statementReq.png](img%2FstatementReq.png)

выбор одного из предложений
![selectRes.png](img%2FselectRes.png)
![selectReq.png](img%2FselectReq.png)

завершение регистрации + полный подсчёт кредита
![calculateRes.png](img%2FcalculateRes.png)
![calculateReq.png](img%2FcalculateReq.png)