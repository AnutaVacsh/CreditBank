package ru.vaschenko.gateway.client.statement;

import feign.FeignException;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.gateway.annotation.FeignRetryable;
import ru.vaschenko.gateway.dto.LoanOfferDto;
import ru.vaschenko.gateway.dto.LoanStatementRequestDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatementFacade implements StatementClient {
    private final StatementClient statementClient;

    /**
     * Отправляет запрос на получение списка кредитных предложений в микросервис Statement.
     *
     * @param loanStatementRequestDto данные для прескоринга заявки на кредит
     * @return список кредитных предложений {@link LoanOfferDto}
     */
    @Override
    @FeignRetryable
    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {
        log.debug("Sending request to get loan offers with data {}", loanStatementRequestDto);
        return executeRequest(() -> statementClient.getLoanOffers(loanStatementRequestDto));
    }

    /**
     * Отправляет запрос на выбор кредитного предложения в микросервис Statement.
     *
     * @param loanOfferDto данные выбранного кредитного предложения
     */
    @Override
    @FeignRetryable
    public void selectOffer(LoanOfferDto loanOfferDto) {
        log.debug("Sending request to select offer with data: {}", loanOfferDto);
        executeRequest(() -> {
            statementClient.selectOffer(loanOfferDto);
            return Optional.empty();
        });
    }

    private <T> T executeRequest(Supplier<T> executor) {
        try {
            T result = executor.get();
            log.debug("Response from statement service received successfully, return data: {}", result);
            return result;
        } catch (FeignException e) {
            log.error("Request failed due to error: {}", e.getMessage());
            throw e;
        }
    }
}
