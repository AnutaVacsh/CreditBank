## MVP Level 3 Реализация микросервиса Заявка (statement).

## Описание проекта

Микросервис Statement предназначен для управления процессом подачи и обработки заявок на кредит.
Взаимодействует с микросервисом Deal.

Пользователь отправляет заявку на кредит через эндпоинт /statement, предоставляя информацию в формате
[LoanStatementRequestDto.java](src%2Fmain%2Fjava%2Fru%2Fvaschenko%2Fdeal%2Fdto%2FLoanStatementRequestDto.java). Далее
эти данные передаются в МС Deal через эндпоинт /deal//statement. На основе этих данных в базе данных создаются два
объекта:
клиент ([Client.java](src%2Fmain%2Fjava%2Fru%2Fvaschenko%2Fdeal%2Fmodels%2FClient.java)) и заявка (
[Statement.java](src%2Fmain%2Fjava%2Fru%2Fvaschenko%2Fdeal%2Fmodels%2FStatement.java)). После этого сервис делает запрос
к МС Calculator на эндпоинт /calculator/offers, чтобы получить
список возможных условий кредита. Каждому предложению добавляется идентификатор заявки, и список возвращается клиенту,
упорядоченный от наименее выгодного к наиболее выгодному.

Пользователь выбирает одно из предложений, оно отправляется в МС Заявка в
формате [LoanOfferDto.java](src%2Fmain%2Fjava%2Fru%2Fvaschenko%2Fstatement%2Fdto%2FLoanOfferDto.java) на эндпоинт
/statement/offer, а оттуда в МС Сделка /deal/offer/select, где заявка с обновлённым статусом и
сам кредит сохраняются в бд.

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
cd statement
```

Перейти в корень и собрать проект:

```bash
mvn clean package
```

После сборки, запустить проект:

```bash
java -jar target/statement-0.0.1-SNAPSHOT.jar
```

## Взаимодействие

Приложение имеет интеграцию со Swagger для тестирования API.
После запуска перейдите по адресу:

для доступа к Swagger UI

```
http://localhost:8082/swagger-ui/index.html.
```

и API документации

```
http://localhost:8082/v1/api-docs
```

Прескоринг + запрос на расчёт возможных условий кредита
![reqStatement.png](img%2FreqStatement.png)
![resStatement.png](img%2FresStatement.png)

Выбор одного из предложений.
![reqOffer.png](img%2FreqOffer.png)
![resOffer.png](img%2FresOffer.png)