package ru.vaschenko.statement.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.vaschenko.statement.api.StatementApi;
import ru.vaschenko.statement.dto.LoanOfferDto;
import ru.vaschenko.statement.dto.LoanStatementRequestDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatementController implements StatementApi {
    @Override
    public List<LoanOfferDto> calculateLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {
        return List.of();
    }

    @Override
    public void selectOffer(LoanOfferDto loanOfferDto) {

    }
}
