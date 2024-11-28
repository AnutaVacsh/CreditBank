package ru.vaschenko.calculator.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vaschenko.calculator.dto.LoanOfferDto;
import ru.vaschenko.calculator.dto.LoanStatementRequestDto;
import ru.vaschenko.calculator.dto.scoring.PreScoringInfoDTO;
import ru.vaschenko.calculator.dto.scoring.RateAndOtherScoringDto;
import ru.vaschenko.calculator.service.proveders.impl.DefaultScoringProvider;
import ru.vaschenko.calculator.service.proveders.rules.impl.soft.InsuranceSoftPreScoringRule;
import ru.vaschenko.calculator.service.proveders.rules.impl.soft.SalaryClientSoftPreScoringRule;

@ExtendWith(MockitoExtension.class)
class OfferServiceTest {

  @Mock private ScoringService scoringService;
  @Mock private DefaultScoringProvider scoringProvider;

  @InjectMocks OfferService offerService;

  @Test
  void generateLoanOffers() {
    LoanStatementRequestDto request =
        LoanStatementRequestDto.builder().amount(new BigDecimal(1000000)).term(24).build();

    PreScoringInfoDTO preScoringInfo1 =
        mockPreScoringInfoDTO(BigDecimal.valueOf(15), BigDecimal.ZERO, true, true);
    PreScoringInfoDTO preScoringInfo2 =
        mockPreScoringInfoDTO(BigDecimal.valueOf(14), BigDecimal.ZERO, true, false);
    PreScoringInfoDTO preScoringInfo3 =
        mockPreScoringInfoDTO(BigDecimal.valueOf(12), BigDecimal.valueOf(100000), false, true);
    PreScoringInfoDTO preScoringInfo4 =
        mockPreScoringInfoDTO(BigDecimal.valueOf(11), BigDecimal.valueOf(100000), false, false);

    when(scoringProvider.preScoring())
        .thenReturn(List.of(preScoringInfo1, preScoringInfo2, preScoringInfo3, preScoringInfo4));

    when(scoringService.calculateMonthlyPayment(
            eq(new BigDecimal(1000000)), eq(BigDecimal.valueOf(15)), eq(24)))
        .thenReturn(BigDecimal.valueOf(48487));
    when(scoringService.calculateMonthlyPayment(
            eq(new BigDecimal(1000000)), eq(BigDecimal.valueOf(14)), eq(24)))
        .thenReturn(BigDecimal.valueOf(48013));
    when(scoringService.calculateMonthlyPayment(
            eq(new BigDecimal(1100000)), eq(BigDecimal.valueOf(12)), eq(24)))
        .thenReturn(BigDecimal.valueOf(51781));
    when(scoringService.calculateMonthlyPayment(
            eq(new BigDecimal(1100000)), eq(BigDecimal.valueOf(11)), eq(24)))
        .thenReturn(BigDecimal.valueOf(51269));

    List<LoanOfferDto> loanOffers = offerService.generateLoanOffers(request);

    assertEquals(4, loanOffers.size());

    assertEquals(BigDecimal.valueOf(11), loanOffers.get(3).getRate());
    assertEquals(BigDecimal.valueOf(12), loanOffers.get(2).getRate());
    assertEquals(BigDecimal.valueOf(14), loanOffers.get(1).getRate());
    assertEquals(BigDecimal.valueOf(15), loanOffers.get(0).getRate());

    for (LoanOfferDto offer : loanOffers) {
      assertNotNull(offer.getRate());
      assertNotNull(offer.getTotalAmount());
      assertNotNull(offer.getMonthlyPayment());
    }

    LoanOfferDto offer1 = loanOffers.get(0);
    assertEquals(BigDecimal.valueOf(1000000), offer1.getRequestedAmount());
    assertEquals(BigDecimal.valueOf(48487), offer1.getMonthlyPayment());
    assertEquals(24, offer1.getTerm());
  }

  private PreScoringInfoDTO mockPreScoringInfoDTO(BigDecimal rate, BigDecimal otherService, boolean isSalaryClient, boolean isInsuranceEnabled) {
    return new PreScoringInfoDTO(
            Map.of(
                    SalaryClientSoftPreScoringRule.class.getSimpleName(), isSalaryClient,
                    InsuranceSoftPreScoringRule.class.getSimpleName(), isInsuranceEnabled),
            new RateAndOtherScoringDto(rate, otherService)
    );
  }
}
