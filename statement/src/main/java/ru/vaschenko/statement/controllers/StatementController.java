package ru.vaschenko.statement.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.vaschenko.statement.api.StatementApi;
import ru.vaschenko.statement.dto.LoanOfferDto;
import ru.vaschenko.statement.dto.LoanStatementRequestDto;
import ru.vaschenko.statement.service.StatementService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatementController implements StatementApi {
    private final StatementService statementService;

    /**
     * Обрабатывает запрос на прескоринг и получение предложений по кредиту.
     *
     * <ul>
     *     <li>Получает {@link LoanStatementRequestDto} через API.</li>
     *     <li>Выполняет прескоринг заявки на основе переданных данных.</li>
     *     <li>Возвращает список из 4 предложений {@link LoanOfferDto}, упорядоченных от наименее выгодного к наиболее выгодному.</li>
     * </ul>
     *
     * @param loanStatementRequestDto данные заявки на кредит
     * @return список кредитных предложений {@link LoanOfferDto}
     */
    @Override
    public List<LoanOfferDto> calculateLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {
        return statementService.calculateLoanOffers(loanStatementRequestDto);
    }


    /**
     * Обрабатывает выбор кредитного предложения пользователем.
     *
     * <ul>
     *     <li>Получает {@link LoanOfferDto} через API.</li>
     *     <li>Отправляет POST-запрос на эндпоинт /deal/offer/select в микросервис "Deal",
     *     где заявка с обновлённым статусом и сам кредит сохраняются в бд.</li>
     *
     * </ul>
     *
     * @param loanOfferDto выбранное кредитное предложение
     */
    @Override
    public void selectOffer(LoanOfferDto loanOfferDto) {
        statementService.selectOffer(loanOfferDto);
    }
}
