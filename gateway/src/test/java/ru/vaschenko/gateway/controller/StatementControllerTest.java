package ru.vaschenko.gateway.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vaschenko.gateway.client.deal.DealFacade;
import ru.vaschenko.gateway.client.statement.StatementFacade;
import ru.vaschenko.gateway.dto.FinishRegistrationRequestDto;
import ru.vaschenko.gateway.dto.LoanOfferDto;
import ru.vaschenko.gateway.dto.LoanStatementRequestDto;

@ExtendWith(MockitoExtension.class)
class StatementControllerTest {

    @Mock
    private StatementFacade statementFacade;

    @Mock
    private DealFacade dealFacade;

    @InjectMocks
    private StatementController statementController;

    private UUID statementId;
    private LoanStatementRequestDto loanStatementRequestDto;
    private LoanOfferDto loanOfferDto;
    private FinishRegistrationRequestDto finishRegistrationRequestDto;

    @BeforeEach
    void setUp() {
        // Given
        statementId = UUID.randomUUID();
        loanStatementRequestDto = LoanStatementRequestDto.builder().build();
        loanOfferDto = LoanOfferDto.builder().build();
        finishRegistrationRequestDto = FinishRegistrationRequestDto.builder().build();
    }

    @Test
    void getLoanOffers_returnsLoanOffers() {
        // Given
        List<LoanOfferDto> expectedOffers = List.of(LoanOfferDto.builder().build());
        when(statementFacade.getLoanOffers(loanStatementRequestDto)).thenReturn(expectedOffers);

        // When
        List<LoanOfferDto> result = statementController.getLoanOffers(loanStatementRequestDto);

        // Then
        assertEquals(expectedOffers, result);
        verify(statementFacade, times(1)).getLoanOffers(loanStatementRequestDto);
    }

    @Test
    void selectOffer_callsStatementFacade() {
        // When
        statementController.selectOffer(loanOfferDto);

        // Then
        verify(statementFacade, times(1)).selectOffer(loanOfferDto);
    }

    @Test
    void calculate_callsDealFacade() {
        // When
        statementController.calculate(statementId, finishRegistrationRequestDto);

        // Then
        verify(dealFacade, times(1)).calculate(statementId, finishRegistrationRequestDto);
    }
}
