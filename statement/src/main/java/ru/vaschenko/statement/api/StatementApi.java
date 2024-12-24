package ru.vaschenko.statement.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.vaschenko.statement.dto.LoanOfferDto;
import ru.vaschenko.statement.dto.LoanStatementRequestDto;
import ru.vaschenko.statement.util.ApiPath;

import java.util.List;

@RequestMapping(ApiPath.BASE_URL)
@Tag(name = "Statement API", description = "API for handling credit statements")
public interface StatementApi {

    @PostMapping
    @Operation(
            summary = "Prescoring and credit conditions calculation",
            description = "Creates a credit statement and returns a list of possible credit conditions"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully calculated credit conditions"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    List<LoanOfferDto> calculateLoanOffers(@RequestBody @Valid LoanStatementRequestDto loanStatementRequestDto);

    @PostMapping(ApiPath.OFFER)
    @Operation(
            summary = "Select a credit offer",
            description = "Allows the selection of one credit offer"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully selected the credit offer"),
            @ApiResponse(responseCode = "400", description = "Invalid credit offer data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    void selectOffer(@RequestBody @Valid LoanOfferDto loanOfferDto);
}
