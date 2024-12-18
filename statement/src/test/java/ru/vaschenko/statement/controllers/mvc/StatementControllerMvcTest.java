package ru.vaschenko.statement.controllers.mvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.vaschenko.statement.controllers.StatementController;
import ru.vaschenko.statement.service.StatementService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class StatementControllerMvcTest {

    @Mock
    private StatementService statementService;

    @InjectMocks
    private StatementController statementController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(statementController).build();
    }

    @Test
    void testCalculateLoanOffers_InvalidRequest() throws Exception {
        // Given
        String invalidRequestJson = """
                {
                    "amount": 50000,
                    "term": 2,
                    "first_name": "John",
                    "last_name": "Doe",
                    "middle_name": "Ivanovich",
                    "email": "example@example.com",
                    "birthdate": "1990-05-01",
                    "passport_series": "1234",
                    "passport_number": "123456"
                }
                """;

        // When
        mockMvc.perform(post("/v1/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestJson))
                // Then
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSelectOffer_InvalidRequest() throws Exception {
        // Given
        String invalidRequestJson = """
                {
                    "requested_amount": 500000,
                    "total_amount": 600000,
                    "term": 24,
                    "monthly_payment": 16666.67,
                    "rate": 11,
                    "is_insurance_enabled": true,
                    "is_salary_client": false
                }
                """;

        // When
        mockMvc.perform(post("/v1/statement/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestJson))
                // Then
                .andExpect(status().isOk());
    }
}