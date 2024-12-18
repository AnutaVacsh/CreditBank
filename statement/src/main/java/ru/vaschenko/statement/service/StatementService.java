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

    public List<LoanOfferDto> calculateLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {
        log.info("Starting loan calculation process");
        return dealFacade.getLoanOffers(loanStatementRequestDto);
    }

    public void selectOffer(LoanOfferDto loanOfferDto) {
        log.info("Starting offer selection process for statementId={}", loanOfferDto.getStatementId());
        dealFacade.selectOffer(loanOfferDto);
    }
}
