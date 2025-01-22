package ru.vaschenko.gateway.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vaschenko.gateway.client.deal.DealFacade;

@ExtendWith(MockitoExtension.class)
class DocumentControllerTest {

    @Mock
    private DealFacade dealFacade;

    @InjectMocks
    private DocumentController documentController;

    private UUID statementId;

    @BeforeEach
    void setUp() {
        // Given
        statementId = UUID.randomUUID();
    }

    @Test
    void sendCodeDocument_callsDealFacade() {
        // When
        documentController.sendCodeDocument(statementId);

        // Then
        verify(dealFacade, times(1)).sendCodeDocument(statementId);
    }

    @Test
    void signCodeDocument_callsDealFacade() {
        // When
        documentController.signCodeDocument(statementId);

        // Then
        verify(dealFacade, times(1)).signCodeDocument(statementId);
    }

    @Test
    void codeDocument_callsDealFacade() {
        // Given
        String sesCode = "12345";

        // When
        documentController.codeDocument(statementId, sesCode);

        // Then
        verify(dealFacade, times(1)).codeDocument(statementId, sesCode);
    }
}