package ru.vaschenko.statement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.statement.client.DealFacade;
import ru.vaschenko.statement.dto.LoanOfferDto;
import ru.vaschenko.statement.dto.LoanStatementRequestDto;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatementService {
    private final DealFacade dealFacade;

    /**
     * Выполняет процесс прескоринга для расчета списка кредитных предложений.
     *
     * @param loanStatementRequestDto данные для прескоринга заявки на кредит
     * @return список кредитных предложений {@link LoanOfferDto}
     */
    public List<LoanOfferDto> calculateLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {
        log.info("Starting loan calculation process");
        return dealFacade.getLoanOffers(loanStatementRequestDto);
    }

    /**
     * Выбирает одно из предложений по заявке на кредит.
     *
     * @param loanOfferDto данные выбранного кредитного предложения
     */
    public void selectOffer(LoanOfferDto loanOfferDto) {
        log.info("Starting offer selection process for statementId={}", loanOfferDto.getStatementId());
        dealFacade.selectOffer(loanOfferDto);
    }
}
