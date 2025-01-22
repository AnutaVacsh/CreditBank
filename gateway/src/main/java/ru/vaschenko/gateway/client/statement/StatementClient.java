package ru.vaschenko.gateway.client.statement;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import ru.vaschenko.gateway.dto.LoanOfferDto;
import ru.vaschenko.gateway.dto.LoanStatementRequestDto;
import ru.vaschenko.gateway.util.ApiPath;

@FeignClient(value = "statement-mc", url = "${client.statement.url}")
public interface StatementClient {
    @PostMapping(ApiPath.STATEMENT_CLIENT_URL)
    List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto);

    @PostMapping(ApiPath.STATEMENT_OFFER)
    void selectOffer(LoanOfferDto loanOfferDto);
}
