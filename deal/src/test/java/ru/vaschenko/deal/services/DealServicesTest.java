package ru.vaschenko.deal.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import feign.FeignException;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.vaschenko.deal.dto.*;
import ru.vaschenko.deal.exception.PrescoringException;
import ru.vaschenko.deal.mapping.CreditMapper;
import ru.vaschenko.deal.mapping.ScoringDataMapper;
import ru.vaschenko.deal.models.Client;
import ru.vaschenko.deal.models.Credit;
import ru.vaschenko.deal.models.Statement;
import ru.vaschenko.deal.models.enams.ApplicationStatus;
import ru.vaschenko.deal.models.enams.CreditStatus;
import ru.vaschenko.deal.services.client.CalculatorClient;

@ExtendWith(MockitoExtension.class)
class DealServicesTest {

  @Mock private ClientService clientService;

  @Mock private StatementService statementService;

  @Mock private CreditService creditService;

  @Mock private CalculatorClient calculatorClient;

  @Spy private ScoringDataMapper scoringDataMapper;

  @Spy private CreditMapper creditMapper;

  @InjectMocks private DealServices dealServices;

  LoanStatementRequestDto requestDto;
  Client mockClient;
  Statement mockStatement;

  @BeforeEach
  void setUp() {
    requestDto = LoanStatementRequestDto.builder().build();
    mockClient = new Client();
    mockStatement = new Statement();
    mockStatement.setStatementId(UUID.randomUUID());
  }

  @Test
  void testCreateStatement_Success() {
    List<LoanOfferDto> mockOffers = List.of(LoanOfferDto.builder().build());

    when(clientService.createClient(requestDto)).thenReturn(mockClient);
    when(statementService.createStatement(mockClient)).thenReturn(mockStatement);
    when(calculatorClient.getLoanOffers(requestDto)).thenReturn(mockOffers);

    ResponseEntity<List<LoanOfferDto>> response = dealServices.createStatement(requestDto);

    assertNotNull(response);
    assertEquals(mockOffers, response.getBody());
    verify(clientService).createClient(requestDto);
    verify(statementService).createStatement(mockClient);
    verify(calculatorClient).getLoanOffers(requestDto);
  }

  @Test
  void testCreateStatement_FailureDueToPrescoring() {

    when(clientService.createClient(requestDto)).thenReturn(mockClient);
    when(statementService.createStatement(mockClient)).thenReturn(mockStatement);

    feign.Response mockResponse =
        feign.Response.builder()
            .status(400)
            .request(
                feign.Request.create(
                    feign.Request.HttpMethod.GET,
                    "/calculator",
                    java.util.Map.of(),
                    null,
                    null,
                    null))
            .build();

    when(calculatorClient.getLoanOffers(requestDto))
        .thenThrow(FeignException.errorStatus("Bad Request", mockResponse));

    // Act & Assert
    assertThrows(PrescoringException.class, () -> dealServices.createStatement(requestDto));
    verify(statementService).saveStatement(mockStatement);
  }

  @Test
  void testSelectOffer() {
    LoanOfferDto offerDto = LoanOfferDto.builder().statementId(UUID.randomUUID()).build();
    Statement mockStatement = new Statement();

    when(statementService.findStatementById(offerDto.getStatementId())).thenReturn(mockStatement);

    dealServices.selectOffer(offerDto);

    assertEquals(ApplicationStatus.APPROVED, mockStatement.getStatus());
    assertEquals(offerDto, mockStatement.getAppliedOffer());
    verify(statementService).saveStatement(mockStatement);
  }

  @Test
  void testCalculate_Success() {
    // GIVEN:
    UUID statementId = UUID.randomUUID();
    FinishRegistrationRequestDto finishRegistrationRequestDto =
            FinishRegistrationRequestDto.builder().build();

    Statement mockStatement = new Statement();
    mockStatement.setStatementId(statementId);

    ScoringDataDto scoringDataDto = ScoringDataDto.builder().build();
    CreditDto creditDto = CreditDto.builder().build();
    Credit mockCredit = new Credit();

    when(statementService.findStatementById(statementId)).thenReturn(mockStatement);
    when(scoringDataMapper.toScoringDataDto(mockStatement, finishRegistrationRequestDto))
            .thenReturn(scoringDataDto);
    when(calculatorClient.getCredit(scoringDataDto)).thenReturn(creditDto);
    when(creditMapper.toCredit(creditDto)).thenReturn(mockCredit);

    // WHEN:
    dealServices.calculate(statementId, finishRegistrationRequestDto);

    // THEN:
    verify(statementService).saveStatement(mockStatement);
    verify(creditService).safeCredit(mockCredit);
    assertEquals(CreditStatus.CALCULATED, mockCredit.getCreditStatus());
  }

}
