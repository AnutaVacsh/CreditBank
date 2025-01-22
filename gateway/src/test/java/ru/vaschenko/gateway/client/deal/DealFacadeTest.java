package ru.vaschenko.gateway.client.deal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vaschenko.gateway.dto.FinishRegistrationRequestDto;

import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class DealFacadeTest {

    @Mock
    private DealClient dealClient;

    @InjectMocks
    private DealFacade dealFacade;

    private UUID statementId;
    private FinishRegistrationRequestDto finishRegistrationRequestDto;

    @BeforeEach
    void setUp() {
        // Given
        statementId = UUID.randomUUID();
    finishRegistrationRequestDto =
        FinishRegistrationRequestDto.builder().build();
    }

    @Test
    void calculate_shouldCallCalculateOnDealClient() {
        // When
        dealFacade.calculate(statementId, finishRegistrationRequestDto);

        // Then
        verify(dealClient, times(1)).calculate(statementId, finishRegistrationRequestDto);
    }

    @Test
    void sendCodeDocument_shouldCallSendCodeDocumentOnDealClient() {
        // When
        dealFacade.sendCodeDocument(statementId);

        // Then
        verify(dealClient, times(1)).sendCodeDocument(statementId);
    }

    @Test
    void signCodeDocument_shouldCallSignCodeDocumentOnDealClient() {
        // When
        dealFacade.signCodeDocument(statementId);

        // Then
        verify(dealClient, times(1)).signCodeDocument(statementId);
    }

    @Test
    void codeDocument_shouldCallCodeDocumentOnDealClient() {
        // Given
        String sesCode = "12345";

        // When
        dealFacade.codeDocument(statementId, sesCode);

        // Then
        verify(dealClient, times(1)).codeDocument(statementId, sesCode);
    }
}
