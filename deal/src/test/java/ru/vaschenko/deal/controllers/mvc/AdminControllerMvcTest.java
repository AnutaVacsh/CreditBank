package ru.vaschenko.deal.controllers.mvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.vaschenko.deal.controllers.AdminController;
import ru.vaschenko.deal.dto.StatementDto;
import ru.vaschenko.deal.models.enams.ApplicationStatus;
import ru.vaschenko.deal.services.AdminService;

@ExtendWith(MockitoExtension.class)
class AdminControllerMvcTest {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    private MockMvc mockMvc;
    private EasyRandom easyRandom;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
        EasyRandomParameters parameters = new EasyRandomParameters()
                .collectionSizeRange(1, 3)
                .randomizationDepth(2);
        easyRandom = new EasyRandom(parameters);
    }

    @Test
    void testDocumentCreated() throws Exception {
        // Given
        UUID statementId = UUID.randomUUID();
        ApplicationStatus status = ApplicationStatus.APPROVED;
        doNothing().when(adminService).documentCreated(statementId, status);

        // When & Then
        mockMvc.perform(put("/deal/admin/statement/" + statementId + "/status")
                        .param("status", status.name()))
                .andExpect(status().isOk());

        verify(adminService, times(1)).documentCreated(statementId, status);
    }

    @Test
    void testGetStatementById() throws Exception {
        // Given
        UUID statementId = UUID.randomUUID();
        StatementDto statementDto = easyRandom.nextObject(StatementDto.class);
        when(adminService.getStatementById(statementId)).thenReturn(statementDto);

        // When & Then
        mockMvc.perform(get("/deal/admin/statement/" + statementId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(adminService, times(1)).getStatementById(statementId);
    }

    @Test
    void testGetStatements() throws Exception {
        // Given
        List<StatementDto> statements = easyRandom.objects(StatementDto.class, 3).toList();
        when(adminService.getStatements()).thenReturn(statements);

        // When & Then
        mockMvc.perform(get("/deal/admin/statement")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(adminService, times(1)).getStatements();
    }
}
