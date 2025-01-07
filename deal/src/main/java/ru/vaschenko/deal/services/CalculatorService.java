package ru.vaschenko.deal.services;

import java.util.List;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vaschenko.deal.dto.CreditDto;
import ru.vaschenko.deal.dto.LoanOfferDto;
import ru.vaschenko.deal.dto.LoanStatementRequestDto;
import ru.vaschenko.deal.dto.ScoringDataDto;
import ru.vaschenko.deal.exception.PrescoringException;
import ru.vaschenko.deal.exception.ScoringCalculationException;
import ru.vaschenko.deal.models.Statement;
import ru.vaschenko.deal.models.enams.ApplicationStatus;
import ru.vaschenko.deal.client.CalculatorFacade;

@Service
@RequiredArgsConstructor
public class CalculatorService {
  private final CalculatorFacade calculatorClient;
  private final StatementService statementService;
  private final MessageService messageService;

  /**
   * Получает предложения по кредиту Метод взаимодействует с сервисом калькулятора для получения
   * предложений по кредиту.
   *
   * @param loanStatementRequestDto Объект данных, содержащий запрос для создания заявки на кредит.
   *     {@link LoanStatementRequestDto}.
   * @param statement Заявка {@link Statement}.
   * @return Список предложений по кредиту на основе данных заявки. {@link LoanOfferDto}.
   * @throws PrescoringException Если произошла ошибка валидации при запросе предложений по кредиту
   *     от сервиса калькулятора.
   */
  public List<LoanOfferDto> getLoanOffers(
      LoanStatementRequestDto loanStatementRequestDto, Statement statement) {
    try {
      return calculatorClient.getLoanOffers(loanStatementRequestDto);
    } catch (FeignException e) {
      statement.setStatus(ApplicationStatus.CC_DENIED);
      statementService.saveStatement(statement);
      throw new PrescoringException(e.getMessage());
    }
  }

  /**
   * Получает информацию о кредите и обновляет статус заявки. Этот метод взаимодействует с сервисом
   * калькулятора для получения данных о кредите.
   *
   * @param scoringDataDto Объект содержащий информацию для скоринга. {@link ScoringDataDto}.
   * @param statement Заявка. {@link Statement}.
   * @return Объект данных о кредите, содержащий информацию о рассчитанном кредите. {@link
   *     CreditDto}.
   * @throws ScoringCalculationException Если в кредите отказано.
   */
  public CreditDto getCredit(ScoringDataDto scoringDataDto, Statement statement) {
    try {
      statement.setStatus(ApplicationStatus.CC_APPROVED);
      statementService.saveStatement(statement);
      return calculatorClient.getCredit(scoringDataDto);
    } catch (FeignException e) {
      statement.setStatus(ApplicationStatus.CC_DENIED);
      statementService.saveStatement(statement);
      messageService.creditDenied(statement);
      throw new ScoringCalculationException(e.getMessage());
    }
  }
}
