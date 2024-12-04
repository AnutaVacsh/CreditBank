package ru.vaschenko.deal.services;

import feign.FeignException;
import jakarta.transaction.Transactional;
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
import ru.vaschenko.deal.models.enams.ChangeType;
import ru.vaschenko.deal.models.enams.CreditStatus;
import ru.vaschenko.deal.services.client.CalculatorClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealServices {
  private final ClientService clientService;
  private final StatementService statementService;
  private final CreditService creditService;
  private final CalculatorClient calculatorClient;
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
    try {
      log.info("Request to the Calculator microservice to get offers.");
      offers = calculatorClient.getLoanOffers(loanStatementRequestDto);
    } catch (FeignException e) {
      log.info("Error occurred while making a request to the Calculator microservice.");
      if (e.status() == 400) {
        statement.setStatus(ApplicationStatus.CC_DENIED, ChangeType.AUTOMATIC);
        statementService.saveStatement(statement);
        throw new PrescoringException(e.contentUTF8());
      }
      throw e;
    }

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
    log.info("Начат процесс завершения регистрации.");
    Statement statement = statementService.findStatementById(statementId);
    ScoringDataDto scoringDataDto =
        scoringDataMapper.toScoringDataDto(statement, finishRegistrationRequestDto);

    CreditDto creditDto;
    try {
      log.info("Запрос к микросервису Калькулятор для выполнения расчёта.");
      creditDto = calculatorClient.getCredit(scoringDataDto);
      statement.setStatus(ApplicationStatus.CC_APPROVED);
      statementService.saveStatement(statement);
    } catch (FeignException e) {
      log.info("Ошибка при запросе к микросервису Калькулятор.");
      if (e.status() == 500) {
        statement.setStatus(ApplicationStatus.CC_DENIED);
        statementService.saveStatement(statement);
        throw new ScoringCalculationException(e.contentUTF8());
      }
      throw e;
    }

    Credit credit = creditMapper.toCredit(creditDto);
    credit.setCreditStatus(CreditStatus.CALCULATED);
    creditService.safeCredit(credit);
  }
}
