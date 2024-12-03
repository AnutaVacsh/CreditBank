package ru.vaschenko.calculator.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.calculator.dto.LoanOfferDto;
import ru.vaschenko.calculator.dto.LoanStatementRequestDto;
import ru.vaschenko.calculator.dto.scoring.PreScoringInfoDTO;
import ru.vaschenko.calculator.service.proveders.ScoringProvider;
import ru.vaschenko.calculator.service.proveders.rules.impl.soft.InsuranceSoftPreScoringRule;
import ru.vaschenko.calculator.service.proveders.rules.impl.soft.SalaryClientSoftPreScoringRule;

@Slf4j
@Service
@RequiredArgsConstructor
public class OfferService {
  private final ScoringService scoringService;
  private final ScoringProvider scoringProvider;

  /**
   * Генерирует список предложений по кредиту на основе запроса клиента.
   *
   * @param requestDto объект {@link LoanStatementRequestDto}
   * @return список объектов {@link LoanOfferDto}, отсортированных по убыванию процентной ставки.
   */
  public List<LoanOfferDto> generateLoanOffers(LoanStatementRequestDto requestDto) {
    log.debug("Starting the generation of loan offers {}", requestDto.toString());

    List<PreScoringInfoDTO> preScoringInfoDTOs = scoringProvider.preScoring();

    return generateLoanOffers(requestDto, preScoringInfoDTOs).stream()
        .sorted((o1, o2) -> o2.getRate().compareTo(o1.getRate()))
        .collect(Collectors.toList());
  }

  /**
   * Генерирует список предложений по кредиту на основе результатов прескоринга.
   *
   * @param requestDto объект {@link LoanStatementRequestDto}
   * @param preScoringInfoDTOs список {@link PreScoringInfoDTO}
   * @return список объектов {@link LoanOfferDto}
   */
  private List<LoanOfferDto> generateLoanOffers(
      LoanStatementRequestDto requestDto, List<PreScoringInfoDTO> preScoringInfoDTOs) {
    List<LoanOfferDto> loanOffers = new ArrayList<>();
    BigDecimal rate;
    BigDecimal totalAmount;
    BigDecimal monthlyPayment;

    for (PreScoringInfoDTO preScoringInfoDTO : preScoringInfoDTOs) {
      rate = preScoringInfoDTO.rateAndOtherScoringDto().newRate();
      totalAmount =
          requestDto.getAmount().add(preScoringInfoDTO.rateAndOtherScoringDto().otherService());
      monthlyPayment =
          scoringService.calculateMonthlyPayment(totalAmount, rate, requestDto.getTerm());

      boolean isSalaryClient =
          preScoringInfoDTO.rules().get(SalaryClientSoftPreScoringRule.class.getSimpleName());
      boolean isInsuranceEnabled =
          preScoringInfoDTO.rules().get(InsuranceSoftPreScoringRule.class.getSimpleName());

      loanOffers.add(
          LoanOfferDto.builder()
              .statementId(UUID.randomUUID())
              .requestedAmount(requestDto.getAmount())
              .totalAmount(totalAmount)
              .term(requestDto.getTerm())
              .monthlyPayment(monthlyPayment)
              .rate(rate)
              .isInsuranceEnabled(isInsuranceEnabled)
              .isSalaryClient(isSalaryClient)
              .build());
    }

    log.debug("generate loan offers {}", loanOffers);
    return loanOffers;
  }
}
