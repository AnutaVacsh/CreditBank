package ru.vaschenko.gateway.controller.mvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.vaschenko.gateway.client.deal.DealFacade;
import ru.vaschenko.gateway.client.statement.StatementFacade;
import ru.vaschenko.gateway.controller.StatementController;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class StatementControllerMvcTest {
  @Mock private StatementFacade statementClient;

  @Mock private DealFacade dealClient;

  @InjectMocks private StatementController statementController;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(statementController).build();
  }

  @Test
  void getLoanOffers_returnsLoanOffers() throws Exception {
    // When & Then
    mockMvc
        .perform(
            post("/v1/statement")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "loanStatementField": "loanStatementValue"
                    }
                """))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  void selectOffer_selectsLoanOffer() throws Exception {
    // When & Then
    mockMvc
        .perform(
            post("/v1/statement/select")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "offerField": "offerValue"
                    }
                """))
        .andExpect(status().isOk());
  }

  @Test
  void calculate_returnsCalculationResult() throws Exception {
    // Given
    UUID statementId = UUID.randomUUID();
    
    // When & Then
    mockMvc
        .perform(
            post("/v1/statement/registration/" + statementId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "registrationField": "registrationValue"
                    }
                """))
        .andExpect(status().isOk());
  }
}
