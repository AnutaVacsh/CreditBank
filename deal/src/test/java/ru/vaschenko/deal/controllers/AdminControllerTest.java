package ru.vaschenko.deal.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.vaschenko.deal.dto.StatementDto;
import ru.vaschenko.deal.models.enams.ApplicationStatus;
import ru.vaschenko.deal.services.AdminService;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    private EasyRandom easyRandom;

    @BeforeEach
    void setUp() {
        EasyRandomParameters parameters = new EasyRandomParameters()
                .collectionSizeRange(1, 3)
                .randomizationDepth(2);
        easyRandom = new EasyRandom(parameters);
    }

    @Test
    void testDocumentCreated() {
        // Given
        UUID statementId = UUID.randomUUID();
        ApplicationStatus status = ApplicationStatus.APPROVED;

        doNothing().when(adminService).documentCreated(statementId, status);

        // When
        adminController.documentCreated(statementId, status);

        // Then
        verify(adminService, times(1)).documentCreated(statementId, status);
    }

    @Test
    void testGetStatementById() {
        // Given
        UUID statementId = UUID.randomUUID();
        StatementDto statementDto = easyRandom.nextObject(StatementDto.class);
        when(adminService.getStatementById(statementId)).thenReturn(statementDto);

        // When
        StatementDto result = adminController.getStatementById(statementId);

        // Then
        assertNotNull(result);
        assertEquals(statementDto, result);
        verify(adminService, times(1)).getStatementById(statementId);
    }

    @Test
    void testGetStatements() {
        // Given
        List<StatementDto> statements = easyRandom.objects(StatementDto.class, 3).toList();
        when(adminService.getStatements()).thenReturn(statements);

        // When
        ResponseEntity<List<StatementDto>> response = adminController.getStatements();

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(statements, response.getBody());
        verify(adminService, times(1)).getStatements();
    }
}