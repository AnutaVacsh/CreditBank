package ru.vaschenko.gateway.client.statement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vaschenko.gateway.dto.LoanOfferDto;
import ru.vaschenko.gateway.dto.LoanStatementRequestDto;

@ExtendWith(MockitoExtension.class)
class StatementFacadeTest {

  @Mock private StatementClient statementClient;

  @InjectMocks private StatementFacade statementFacade;

  private LoanStatementRequestDto loanStatementRequestDto;
  private LoanOfferDto loanOfferDto;

  @BeforeEach
  void setUp() {
    // Given
    loanStatementRequestDto = LoanStatementRequestDto.builder().build();
    loanOfferDto = LoanOfferDto.builder().build();
  }

  @Test
  void getLoanOffers_shouldCallGetLoanOffersOnStatementClient() {
    // Given
    when(statementClient.getLoanOffers(loanStatementRequestDto))
        .thenReturn(Collections.singletonList(loanOfferDto));

    // When
    List<LoanOfferDto> result = statementFacade.getLoanOffers(loanStatementRequestDto);

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    verify(statementClient, times(1)).getLoanOffers(loanStatementRequestDto);
  }

  @Test
  void selectOffer_shouldCallSelectOfferOnStatementClient() {
    // When
    statementFacade.selectOffer(loanOfferDto);

    // Then
    verify(statementClient, times(1)).selectOffer(loanOfferDto);
  }
}
