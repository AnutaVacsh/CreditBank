package ru.vaschenko.statement.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.vaschenko.statement.api.StatementApi;
import ru.vaschenko.statement.dto.LoanOfferDto;
import ru.vaschenko.statement.dto.LoanStatementRequestDto;
import ru.vaschenko.statement.service.StatementService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatementController implements StatementApi {
    private final StatementService statementService;

    @Override
    public List<LoanOfferDto> calculateLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {
        return statementService.calculateLoanOffers(loanStatementRequestDto);
    }

    @Override
    public void selectOffer(LoanOfferDto loanOfferDto) {
        statementService.selectOffer(loanOfferDto);
    }
}
