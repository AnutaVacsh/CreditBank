package ru.vaschenko.deal.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    // Given
    LoanStatementRequestDto requestDto = LoanStatementRequestDto.builder().build();
    when(dealServices.createStatement(requestDto))
        .thenReturn(ResponseEntity.ok(Collections.singletonList(loanOfferDto)));

    // When
    ResponseEntity<List<LoanOfferDto>> response = dealController.createStatement(requestDto);

    // Then
    assertNotNull(response);
    assertEquals(1, response.getBody().size());
    assertEquals(BigDecimal.valueOf(11), response.getBody().get(0).getRate());

    verify(dealServices, times(1)).createStatement(requestDto);
  }

  @Test
  void testSelectOffer() {
    // Given
    LoanOfferDto loanOfferDto = LoanOfferDto.builder().build();
    doNothing().when(dealServices).selectOffer(loanOfferDto);

    // When
    dealController.selectOffer(loanOfferDto);

    // Then
    verify(dealServices, times(1)).selectOffer(loanOfferDto);
  }

  @Test
  void testCalculate() {
    // Given
    UUID statementId = UUID.randomUUID();
    FinishRegistrationRequestDto finishRegistrationRequestDto =
        FinishRegistrationRequestDto.builder().build();
    doNothing().when(dealServices).calculate(statementId, finishRegistrationRequestDto);

    // When
    dealController.calculate(statementId, finishRegistrationRequestDto);

    // Then
    verify(dealServices, times(1)).calculate(statementId, finishRegistrationRequestDto);
  }

  @Test
  void testSendCodeDocument() {
    // Given
    UUID statementId = UUID.randomUUID();
    doNothing().when(dealServices).sendCodeDocument(statementId);

    // When
    dealController.sendCodeDocument(statementId);

    // Then
    verify(dealServices, times(1)).sendCodeDocument(statementId);
  }

  @Test
  void testSignCodeDocument() {
    // Given
    UUID statementId = UUID.randomUUID();
    doNothing().when(dealServices).signCodeDocument(statementId);

    // When
    dealController.signCodeDocument(statementId);

    // Then
    verify(dealServices, times(1)).signCodeDocument(statementId);
  }

  @Test
  void testCodeDocument() {
    // Given
    UUID statementId = UUID.randomUUID();
    String sesCode = "123456";
    doNothing().when(dealServices).codeDocument(statementId, sesCode);

    // When
    dealController.codeDocument(statementId, sesCode);

    // Then
    verify(dealServices, times(1)).codeDocument(statementId, sesCode);
  }
}
