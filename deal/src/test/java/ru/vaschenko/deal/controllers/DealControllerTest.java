package ru.vaschenko.deal.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.vaschenko.deal.dto.FinishRegistrationRequestDto;
import ru.vaschenko.deal.dto.LoanOfferDto;
import ru.vaschenko.deal.dto.LoanStatementRequestDto;
import ru.vaschenko.deal.services.DealServices;

@ExtendWith(MockitoExtension.class)
class DealControllerTest {

  @Mock private DealServices dealServices;

  @InjectMocks private DealController dealController;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;
  private LoanOfferDto loanOfferDto;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(dealController).build();
    objectMapper = new ObjectMapper();

    loanOfferDto =
        LoanOfferDto.builder().statementId(UUID.randomUUID()).rate(BigDecimal.valueOf(11)).build();
  }

  @Test
  void testCreateStatement() throws Exception {
    LoanStatementRequestDto requestDto = LoanStatementRequestDto.builder().build();

    when(dealServices.createStatement(requestDto))
        .thenReturn(ResponseEntity.ok(Collections.singletonList(loanOfferDto)));

    mockMvc
        .perform(
            post("/deal/statement")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].rate").value(11));

    verify(dealServices, times(1)).createStatement(requestDto);
  }

  @Test
  void testSelectOffer() throws Exception {
    mockMvc
        .perform(
            post("/deal/offer/select")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loanOfferDto)))
        .andExpect(status().isOk());

    verify(dealServices, times(1)).selectOffer(any(LoanOfferDto.class));
  }

  @Test
  void testCalculate() throws Exception {
    UUID statementId = UUID.randomUUID();
    FinishRegistrationRequestDto finishRegistrationRequestDto =
        FinishRegistrationRequestDto.builder().build();

    mockMvc
        .perform(
            post("/deal/calculate/" + statementId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(finishRegistrationRequestDto)))
        .andExpect(status().isOk());

    verify(dealServices, times(1)).calculate(statementId, finishRegistrationRequestDto);
  }
}
