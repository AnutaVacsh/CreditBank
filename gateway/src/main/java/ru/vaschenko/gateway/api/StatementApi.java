package ru.vaschenko.gateway.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.vaschenko.gateway.dto.FinishRegistrationRequestDto;
import ru.vaschenko.gateway.dto.LoanOfferDto;
import ru.vaschenko.gateway.dto.LoanStatementRequestDto;
import ru.vaschenko.gateway.util.ApiPath;

@RequestMapping(ApiPath.BASE_URL_STATEMENT)
@Tag(name = "Statement API", description = "API for managing loan applications")
public interface StatementApi {
  /**
   * Получить список кредитных предложений
   *
   * @param loanStatementRequestDto данные для получения кредитных предложений
   * @return список предложений
   */
  @Operation(
      summary = "Get a list of loan offers",
      description = "Sends a request to retrieve a list of loan offers.")
  @PostMapping
  List<LoanOfferDto> getLoanOffers(@RequestBody LoanStatementRequestDto loanStatementRequestDto);

  /**
   * Выбор кредитного предложения
   *
   * @param loanOfferDto данные выбранного кредитного предложения
   */
  @Operation(
      summary = "Select a loan offer",
      description = "Sends a request to select a loan offer.")
  @PostMapping(ApiPath.STATEMENT_SELECT)
  void selectOffer(@RequestBody LoanOfferDto loanOfferDto);

  /**
   * Расчет по заявке
   *
   * @param statementId идентификатор заявки
   * @param finishRegistrationRequestDto данные для завершения регистрации
   */
  @Operation(
      summary = "Calculate credit",
      description = "Sends a request to calculate the loan application.")
  @PostMapping(ApiPath.STATEMENT_REGISTRATION_ID)
  void calculate(
      @PathVariable UUID statementId,
      @RequestBody FinishRegistrationRequestDto finishRegistrationRequestDto);
}
