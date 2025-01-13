# Проектное задание: Java Development (осенний набор 2024)

## Логика работы всей системы

1. Пользователь отправляет заявку на кредит.
2. МС Заявка осуществляет прескоринг прескорингзаявки и если прескоринг проходит, то заявка сохраняется в МС Сделка и
   отправляется в МС калькулятор.
3. МС Калькулятор возвращает через МС Заявку пользователю 4 предложения (сущность "LoanOffer") по кредиту с разными
   условиями (например без страховки, со страховкой, с зарплатным клиентом, со страховкой и зарплатным клиентом) или
   отказ.
4. Пользователь выбирает одно из предложений, отправляется запрос в МС Заявка, а оттуда в МС Сделка, где заявка на
   кредит и сам кредит сохраняются в базу.
5. МС Досье отправляет клиенту письмо с текстом "Ваша заявка предварительно одобрена, завершите оформление".
6. Клиент отправляет запрос в МС Сделка со всеми своими полными данными о работодателе и прописке.
   Происходит скоринг данных в МС Калькулятор, МС Калькулятор рассчитывает все данные по кредиту (ПСК, график платежей и
   тд), МС Сделка сохраняет обновленную заявку и сущность кредит сделанную на основе CreditDto полученного из КК со
   статусом CALCULATED в БД.
7. После валидации МС Досье отправляет письмо на почту клиенту с одобрением или отказом.
   Если кредит одобрен, то в письме присутствует ссылка на запрос "Сформировать документы"
8. Клиент отправляет запрос на формирование документов в МС Досье, МС Досье отправляет клиенту на почту документы для
   подписания и ссылку на запрос на согласие с условиями.
9. Клиент может отказаться от условий или согласиться.
   Если согласился - МС Досье на почту отправляет код и ссылку на подписание документов, куда клиент должен отправить
   полученный код в МС Сделка.
10. Если полученный код совпадает с отправленным, МС Сделка выдает кредит (меняет статус сущности "Кредит" на ISSUED, а
    статус заявки на CREDIT_ISSUED)

