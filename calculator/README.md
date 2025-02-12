# MVP Level 1 реализация микросервиса Калькулятор

## Описание проекта

Сервис кредитных предложений предназначен для предоставления клиентам персонализированных вариантов кредитования на
основе их индивидуальных параметров и предпочтений.

Прескоринг реализован на уровне валидации и при неудаче
возвращается [ErrorMessageDto.java](src/main/java/ru/vaschenko/calculator/dto/ErrorMessageDto.java)
c возможной причиной.
Расчёт ставки происходит
в [DefaultScoringProvider.java](src%2Fmain%2Fjava%2Fru%2Fvaschenko%2Fcalculator%2Fservice%2Fproveders%2Fimpl%2FDefaultScoringProvider.java)
с использованием
правил [PreScoringRules.java](src%2Fmain%2Fjava%2Fru%2Fvaschenko%2Fcalculator%2Fservice%2Fproveders%2Frules%2FPreScoringRules.java)
Ежемесячный платеж рассчитывается по формуле аннуитетного платежа, которая учитывает сумму кредита, процентную ставку и срок в месяцах.
На основании полученных данных создаются предложения в виде объектов LoanOfferDto

Скоринг тоже реализован в сервисном слое в
компененте [DefaultScoringProvider.java](src%2Fmain%2Fjava%2Fru%2Fvaschenko%2Fcalculator%2Fservice%2Fproveders%2Fimpl%2FDefaultScoringProvider.java)
с использованием правил
[ScoringHardRules.java](src%2Fmain%2Fjava%2Fru%2Fvaschenko%2Fcalculator%2Fservice%2Fproveders%2Frules%2FScoringHardRules.java)
и
[ScoringSoftRules.java](src%2Fmain%2Fjava%2Fru%2Fvaschenko%2Fcalculator%2Fservice%2Fproveders%2Frules%2FScoringSoftRules.java),
из hard rules
возвращается [RejectionAndMessageScoringDTO.java](src%2Fmain%2Fjava%2Fru%2Fvaschenko%2Fcalculator%2Fdto%2Fscoring%2FRejectionAndMessageScoringDTO.java)
с указанием успешности прохождения и возможной ошибкой, из soft rules
возвращается [RateAndOtherScoringDto.java](src%2Fmain%2Fjava%2Fru%2Fvaschenko%2Fcalculator%2Fdto%2Fscoring%2FRateAndOtherScoringDto.java)
с измененённой ставкой и суммой дополнительных услуг(страховки). Все значения для правил скоринга указаны в [application.properties](src%2Fmain%2Fresources%2Fapplication.properties)
На основании полученных данных создаётся CreditDto

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
cd calculator
```

Перейти в корень и собрать проект:

```bash
mvn clean package
```

После сборки, запустить проект:

```bash
java -jar target/calculator-0.0.1-SNAPSHOT.jar
```

## Взаимодействие

Приложение имеет интеграцию со Swagger для тестирования API.
После запуска перейдите по адресу:

```
http://localhost:8080/swagger-ui/index.html.
```

Создание кредитных предложений
![offersReq.png](img%2FoffersReq.png)
![offersRes.png](img%2FoffersRes.png)

Вычисление кредитного предложения
![calcReq.png](img%2FcalcReq.png)
![calcRes.png](img%2FcalcRes.png)

Отказ после прескоринга
![offersReqEr.png](img%2FoffersReqEr.png)
![offersResEr.png](img%2FoffersResEr.png)

Отказ после скоринга
![calcReqEr.png](img%2FcalcReqEr.png)
![calcResEr.png](img%2FcalcResEr.png)
