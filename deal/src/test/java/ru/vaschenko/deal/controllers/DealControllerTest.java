package ru.vaschenko.deal.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.vaschenko.deal.dto.FinishRegistrationRequestDto;
import ru.vaschenko.deal.dto.LoanOfferDto;
import ru.vaschenko.deal.dto.LoanStatementRequestDto;
import ru.vaschenko.deal.services.DealServices;

@ExtendWith(MockitoExtension.class)
class DealControllerTest {

  @Mock private DealServices dealServices;

  @InjectMocks private DealController dealController;

  private LoanOfferDto loanOfferDto;

  @BeforeEach
  void setUp() {
    loanOfferDto =
        LoanOfferDto.builder().statementId(UUID.randomUUID()).rate(BigDecimal.valueOf(11)).build();
  }

  @Test
  void testCreateStatement() {
    LoanStatementRequestDto requestDto = LoanStatementRequestDto.builder().build();

    when(dealServices.createStatement(requestDto))
        .thenReturn(ResponseEntity.ok(Collections.singletonList(loanOfferDto)));

    ResponseEntity<List<LoanOfferDto>> response = dealController.createStatement(requestDto);

    assertNotNull(response);
    assertEquals(1, response.getBody().size());
    assertEquals(BigDecimal.valueOf(11), response.getBody().get(0).getRate());

    verify(dealServices, times(1)).createStatement(requestDto);
  }

  @Test
  void testSelectOffer() {
    LoanOfferDto loanOfferDto = LoanOfferDto.builder().build();

    doNothing().when(dealServices).selectOffer(loanOfferDto);

    dealController.selectOffer(loanOfferDto);

    verify(dealServices, times(1)).selectOffer(loanOfferDto);
  }

  @Test
  void testCalculate() {
    UUID statementId = UUID.randomUUID();
    FinishRegistrationRequestDto finishRegistrationRequestDto =
        FinishRegistrationRequestDto.builder().build();

    doNothing().when(dealServices).calculate(statementId, finishRegistrationRequestDto);

    dealController.calculate(statementId, finishRegistrationRequestDto);

    verify(dealServices, times(1)).calculate(statementId, finishRegistrationRequestDto);
  }
}
