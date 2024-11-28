package ru.vaschenko.calculator.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.vaschenko.calculator.controller.CalculatorController;
import ru.vaschenko.calculator.dto.CreditDto;
import ru.vaschenko.calculator.dto.LoanOfferDto;
import ru.vaschenko.calculator.dto.LoanStatementRequestDto;
import ru.vaschenko.calculator.dto.ScoringDataDto;
import ru.vaschenko.calculator.service.CalculatorService;
import ru.vaschenko.calculator.service.OfferService;
import ru.vaschenko.calculator.service.ScoringService;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class CalculatorControllerMvcTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private OfferService offerService;

  @MockBean private ScoringService scoringService;

  @MockBean private CalculatorService calculatorService;

  @Autowired private CalculatorController calculatorController;

  @Test
  public void givenLoanRequest() throws Exception {
    LoanStatementRequestDto requestDto = LoanStatementRequestDto.builder().build();
    List<LoanOfferDto> loanOfferDto = Collections.nCopies(4, LoanOfferDto.builder().build());

    when(offerService.generateLoanOffers(any(LoanStatementRequestDto.class)))
        .thenReturn(loanOfferDto);

    mockMvc
        .perform(
            post("/calculator/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(4));
  }

  @Test
  void calculateCredit() throws Exception {
    ScoringDataDto scoringData = ScoringDataDto.builder().build();
    CreditDto creditDto = CreditDto.builder().build();

    when(calculatorService.calculateCredit(any(ScoringDataDto.class))).thenReturn(creditDto);

    mockMvc
        .perform(
            post("/calculator/calc")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(scoringData)))
        .andExpect(status().isCreated());
  }
}
