package ru.vaschenko.calculator.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.calculator.dto.LoanOfferDto;
import ru.vaschenko.calculator.dto.LoanStatementRequestDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class OfferService {
    BaseCalculatorService baseCalculatorService;

    public List<LoanOfferDto> generateLoanOffers(LoanStatementRequestDto requestDto) {
        log.info("Начинаем генерацию предложений по кредиту");
        List<LoanOfferDto> offers = new ArrayList<>();

        boolean[][] combinations = {
                {false, false}, {false, true}, {true, false}, {true, true}};

        for (boolean[] combination : combinations) {
            LoanOfferDto offer = createOffer(requestDto, combination[0], combination[1]);
            offers.add(offer);
        }
        log.info("Список предложений создан");

        offers.sort(Comparator.comparing(LoanOfferDto::getRate).reversed());

        log.info("Список предложений отсортирован");
        return offers;
    }

    private LoanOfferDto createOffer(LoanStatementRequestDto requestDto, boolean isInsuranceEnabled, boolean isSalaryClient) {
        // вычисляем ставку
        BigDecimal rate = baseCalculatorService.calculateRate(isInsuranceEnabled, isSalaryClient);

        // кредит + страховка
        BigDecimal totalAmount = baseCalculatorService.calculateTotalAmount(requestDto.getAmount(), isInsuranceEnabled);

        // вычисляем monthlyPayment
        log.info("{} {} {}", totalAmount, rate, requestDto.getTerm());
        BigDecimal monthlyPayment = baseCalculatorService.calculateMonthlyPayment(totalAmount, rate, requestDto.getTerm());

        LoanOfferDto offer = new LoanOfferDto();
        offer.setStatementId(UUID.randomUUID());
        offer.setRequestedAmount(requestDto.getAmount());
        offer.setTotalAmount(totalAmount);
        offer.setTerm(requestDto.getTerm());
        offer.setMonthlyPayment(monthlyPayment);
        offer.setRate(rate);
        offer.setIsInsuranceEnabled(isInsuranceEnabled);
        offer.setIsSalaryClient(isSalaryClient);

        log.info("Предложение создано");
        return offer;
    }
}
