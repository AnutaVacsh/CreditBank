package ru.vaschenko.gateway.client.deal;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.gateway.annotation.FeignRetryable;
import ru.vaschenko.gateway.client.statement.StatementClient;
import ru.vaschenko.gateway.dto.FinishRegistrationRequestDto;
import ru.vaschenko.gateway.dto.LoanOfferDto;
import ru.vaschenko.gateway.dto.LoanStatementRequestDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealFacade implements DealClient {
    private final DealClient dealClient;

    /**
     * Отправляет запрос на расчет кредитного предложения.
     * @param statementId идентификатор заявки
     * @param finishRegistrationRequestDto данные для завершения регистрации
     */
    @Override
    @FeignRetryable
    public void calculate(UUID statementId, FinishRegistrationRequestDto finishRegistrationRequestDto) {
        log.debug("Sending request to calculate credit with statementId {} and data {}", statementId, finishRegistrationRequestDto);
        executeRequest(() -> dealClient.calculate(statementId, finishRegistrationRequestDto));
    }

    /**
     * Отправляет запрос на отправку документов.
     * @param statementId идентификатор заявки
     */
    @Override
    @FeignRetryable
    public void sendCodeDocument(UUID statementId) {
        log.debug("Sending request to send code document with statementId {}", statementId);
        executeRequest(() -> dealClient.sendCodeDocument(statementId));
    }

    /**
     * Отправляет запрос на подписание документов.
     * @param statementId идентификатор заявки
     */
    @Override
    @FeignRetryable
    public void signCodeDocument(UUID statementId) {
        log.debug("Sending request to sign code document with statementId {}", statementId);
        executeRequest(() -> dealClient.signCodeDocument(statementId));
    }

    /**
     * Подписание документов.
     * @param statementId идентификатор заявки
     * @param sesCode код для документа
     */
    @Override
    @FeignRetryable
    public void codeDocument(UUID statementId, String sesCode) {
        log.debug("Sending request to code document with statementId {} and sesCode {}", statementId, sesCode);
        executeRequest(() -> dealClient.codeDocument(statementId, sesCode));
    }

    private void executeRequest(Runnable executor) {
        try {
            executor.run();  // Выполняем запрос
            log.debug("Request executed successfully");
        } catch (FeignException e) {
            log.error("Request failed due to error: {}", e.getMessage());
            throw e;
        }
    }
}
