package ru.vaschenko.statement.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import ru.vaschenko.statement.dto.LoanOfferDto;
import ru.vaschenko.statement.dto.LoanStatementRequestDto;
import ru.vaschenko.statement.util.ApiPath;

import java.util.List;

@FeignClient(value = "deal-mc", url = "${client.deal.url}")
public interface DealClient {
    @PostMapping(ApiPath.DEAL_STATEMENT)
    List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto);

    @PostMapping(ApiPath.DEAL_OFFER)
    void selectOffer(LoanOfferDto loanOfferDto);
}
