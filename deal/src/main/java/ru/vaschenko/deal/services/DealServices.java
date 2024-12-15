package ru.vaschenko.deal.services;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.vaschenko.deal.dto.*;
import ru.vaschenko.deal.exception.PrescoringException;
import ru.vaschenko.deal.exception.ScoringCalculationException;
import ru.vaschenko.deal.mapping.CreditMapper;
import ru.vaschenko.deal.mapping.ScoringDataMapper;
import ru.vaschenko.deal.models.Client;
import ru.vaschenko.deal.models.Credit;
import ru.vaschenko.deal.models.Statement;
import ru.vaschenko.deal.models.enams.ApplicationStatus;
import ru.vaschenko.deal.models.enams.CreditStatus;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealServices {
  private final ClientService clientService;
  private final StatementService statementService;
  private final CreditService creditService;
  private final CalculatorService calculatorService;
  private final ScoringDataMapper scoringDataMapper;
  private final CreditMapper creditMapper;

  /**
   * Создаёт заявку на основе предоставленных данных, взаимодействует с микросервисом Калькулятор
   * для получения кредитных предложений.
   *
   * @param loanStatementRequestDto объект запроса с данными для создания заявки {@link
   *     LoanStatementRequestDto}.
   * @return список кредитных предложений {@link LoanOfferDto}.
   * @throws PrescoringException если при отправке запроса в микросервис Калькулятор возникает
   *     ошибка предскоринга.
   */
  public ResponseEntity<List<LoanOfferDto>> createStatement(
      LoanStatementRequestDto loanStatementRequestDto) {
    Client client = clientService.createClient(loanStatementRequestDto);

    Statement statement = statementService.createStatement(client);

    List<LoanOfferDto> offers;
    offers = calculatorService.getLoanOffers(loanStatementRequestDto, statement);

    offers.forEach(oldOffer -> oldOffer.setStatementId(statement.getStatementId()));
    log.debug("Created list of offers: {}", offers);
    return ResponseEntity.ok(offers);
  }

  /**
   * Обрабатывает выбор одного из предложений кредита.
   *
   * @param loanOfferDto выбранное предложение {@link LoanOfferDto}.
   */
  public void selectOffer(LoanOfferDto loanOfferDto) {
    Statement statement = statementService.findStatementById(loanOfferDto.getStatementId());
    statement.setStatus(ApplicationStatus.APPROVED);
    statement.setAppliedOffer(loanOfferDto);
    statementService.saveStatement(statement);
  }

  /**
   * Завершает процесс регистрации и выполняет полный расчёт кредита.
   *
   * @param statementId идентификатор заявки {@link UUID}.
   * @param finishRegistrationRequestDto данные для завершения регистрации {@link
   *     FinishRegistrationRequestDto}.
   * @throws ScoringCalculationException если при запросе к микросервису Калькулятор возникает
   *     ошибка расчёта.
   */
  public void calculate(
      UUID statementId, FinishRegistrationRequestDto finishRegistrationRequestDto) {
    log.info("The registration completion process has begun.");
    Statement statement = statementService.findStatementById(statementId);
    ScoringDataDto scoringDataDto =
        scoringDataMapper.toScoringDataDto(statement, finishRegistrationRequestDto);

    CreditDto creditDto = calculatorService.getCredit(scoringDataDto, statement);

    Credit credit = creditMapper.toCredit(creditDto);
    credit.setCreditStatus(CreditStatus.CALCULATED);
    creditService.safeCredit(credit);
  }
}
