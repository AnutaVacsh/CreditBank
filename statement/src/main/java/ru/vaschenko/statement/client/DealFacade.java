package ru.vaschenko.statement.client;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.statement.annotation.FeignRetryable;
import ru.vaschenko.statement.dto.LoanOfferDto;
import ru.vaschenko.statement.dto.LoanStatementRequestDto;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealFacade implements DealClient {
    private final DealClient dealClient;

    /**
     * Отправляет запрос на получение списка кредитных предложений в микросервис Deal.
     * <p>
     * Метод обрабатывает {@link LoanStatementRequestDto} и возвращает список {@link LoanOfferDto},
     * полученный от микросервиса Deal.
     * </p>
     *
     * @param loanStatementRequestDto данные для прескоринга заявки на кредит
     * @return список кредитных предложений {@link LoanOfferDto}
     */
    @Override
    @FeignRetryable
    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {
        log.debug("Sending request to get loan offers with data {}", loanStatementRequestDto);
        return executeRequest(() -> dealClient.getLoanOffers(loanStatementRequestDto));
    }

    /**
     * Отправляет запрос на выбор кредитного предложения в микросервис Deal.
     * <p>
     * Метод принимает {@link LoanOfferDto}, который передается в микросервис Deal
     * для фиксации выбранного предложения.
     * </p>
     *
     * @param loanOfferDto данные выбранного кредитного предложения
     */
    @Override
    @FeignRetryable
    public void selectOffer(LoanOfferDto loanOfferDto) {
        log.debug("Sending request to select offer with data: {}", loanOfferDto);
        executeRequest(() -> {
            dealClient.selectOffer(loanOfferDto);
            return Optional.empty();
        });
    }

    private <T> T executeRequest(Supplier<T> executor) {
        try {
            T result = executor.get();
            log.debug("Response from deal service received successfully, return data: {}", result);
            return result;
        } catch (FeignException e) {
            log.error("Request failed due to error: {}", e.getMessage());
            throw e;
        }
    }
}


//    @Override
//    @FeignRetryable
//    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {
//        log.debug("Sending request to get loan offers with data {}", loanStatementRequestDto);
//        return executeRequest(() -> dealClient.getLoanOffers(loanStatementRequestDto));
//    }
//
//    @Override
//    public void selectOffer(LoanOfferDto loanOfferDto) {
//        log.debug("Sending request to select offer with data: {}", loanOfferDto);
//        dealClient.selectOffer(loanOfferDto);
//        return executeRequest(() -> dealClient.selectOffer(loanOfferDto));
//    }
//
//    private <T> T executeRequest(Supplier<T> executor) {
//        try {
//            T result = executor.get();
//            log.debug("Response from deal service received successfully, return data {}", result);
//            return result;
//        } catch (FeignException e) {
//            log.error("Request failed due to error: {}", e.getMessage());
//            throw e;
//        }
//    }
//}
