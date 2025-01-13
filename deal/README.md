## MVP Level 2 реализация микросервиса сделка (Deal)

## Описание проекта

Микросервис Deal предназначен для управления процессом подачи и обработки заявок на кредит.

Взаимодействие между МС-deal и МС-dossier было реализовано через Kafka
МС-deal выступает в роли издателя (producer), МС-dossier в роли подписчика (consumer)
В кафке были заведены 6 топиков, соответствующие темам, по которым необходимо направить письмо на почту Клиенту:

- finish-registration
- create-documents
- send-documents
- send-ses
- credit-issued
- statement-denied

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
о выбранном предложении. Эти изменения сохраняются в базе данных. После чего, через топик __finish-registration__
отправляется сообщение в МС-dossier

Для завершения регистрации и окончательного расчета кредита пользователь отправляет данные через эндпоинт
/deal/calculate/{statementId}. На основе этих данных и информации о клиенте формируется
объект [ScoringDataDto.java](src%2Fmain%2Fjava%2Fru%2Fvaschenko%2Fdeal%2Fdto%2FScoringDataDto.java), который
отправляется в микросервис "Калькулятор" на эндпоинт /calculator/calc. Результат расчета, используется для создания
сущности Credit, которая сохраняется в базе данных. Заявка обновляется. После чего, через топик __create-documents__
отправляется сообщение в МС-dossier.
Если в кредите отказано, то сообщение отправляется в топик __statement-denied__

Для отправки документов пользователь отправляет запрос на эндпоинт /deal/document/{statementId}/send, после чего через
топик __send-documents__ в МС-dossier
отправляется [EmailMessageCredit.java](src%2Fmain%2Fjava%2Fru%2Fvaschenko%2Fdeal%2Fdto%2FEmailMessageCredit.java), далее
пользователю на почту отправляются готовые документы

Для запроса на подписание документов пользователь отправляет запрос на эндпоинт /deal/document/{statementId}/sign, после
чего через топик __send-ses__ в МС-dossier
отправляется [EmailMessage.java](src%2Fmain%2Fjava%2Fru%2Fvaschenko%2Fdeal%2Fdto%2FEmailMessage.java), далее
пользователю на почту отправляется ses-код

Для подписания документов пользователь отправляет запрос на эндпоинт /deal/document/{statementId}/code с ses-кодом,
после чего через топик __credit-issued__ в МС-dossier
отправляется [EmailMessage.java](src%2Fmain%2Fjava%2Fru%2Fvaschenko%2Fdeal%2Fdto%2FEmailMessage.java)

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

запрос на отправку документов
![send.png](img%2Fsend.png)

запрос на подписание документов
![sign.png](img%2Fsign.png)

подписание документов
![code.png](img%2Fcode.png)

сообщения, отправляемые на почту
![mail.png](img%2Fmail.png)

обновление статуса заявки
![updateStatus.png](img%2FupdateStatus.png)

получение заявки по id
![statementId.png](img%2FstatementId.png)

получение всех заявок
![allStatement.png](img%2FallStatement.png)