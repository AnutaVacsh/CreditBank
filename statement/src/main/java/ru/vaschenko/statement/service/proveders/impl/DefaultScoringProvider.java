package ru.vaschenko.statement.service.proveders.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.vaschenko.statement.dto.ScoringDataDto;
import ru.vaschenko.statement.dto.scoring.PreScoringInfoDTO;
import ru.vaschenko.statement.dto.scoring.RateAndOtherScoringDto;
import ru.vaschenko.statement.dto.scoring.RejectionAndMessageScoringDTO;
import ru.vaschenko.statement.exception.ScoringCalculationException;
import ru.vaschenko.statement.service.proveders.ScoringProvider;
import ru.vaschenko.statement.service.proveders.rules.PreScoringRules;
import ru.vaschenko.statement.service.proveders.rules.ScoringHardRules;
import ru.vaschenko.statement.service.proveders.rules.ScoringSoftRules;

//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class DefaultScoringProvider implements ScoringProvider {
//  private final List<ScoringHardRules> hardFilters;
//  private final List<ScoringSoftRules> softFilters;
//  private final List<PreScoringRules> loanFilters;
//
//  @Value("${loan.base.rate}")
//  private BigDecimal rate;
//
//  /**
//   * Выполняет полный процесс скоринга.
//   *
//   * @param scoringDataDto объект {@link ScoringDataDto}, содержащий данные для скоринга.
//   * @return объект {@link RateAndOtherScoringDto}, содержащий итоговую ставку и дополнительные
//   *     услуги.
//   * @throws ScoringCalculationException любая из жестких проверок не пройдена.
//   */
//  @Override
//  public RateAndOtherScoringDto fullScoring(ScoringDataDto scoringDataDto) {
//    RejectionAndMessageScoringDTO rejectionAndMessageScoringDTO = hardScoring(scoringDataDto);
//    if (!rejectionAndMessageScoringDTO.rejection()) {
//      throw new ScoringCalculationException(rejectionAndMessageScoringDTO.message());
//    }
//    return softScoring(scoringDataDto, rate);
//  }
//
//  /**
//   * Выполняет жесткие проверки на основе предоставленных данных.
//   *
//   * @param scoringDataDto объект {@link ScoringDataDto}, содержащий данные для скоринга.
//   * @return объект {@link RejectionAndMessageScoringDTO}, содержащий результат проверки
//   *     (успех/отказ) и соответствующее сообщение.
//   */
//  @Override
//  public RejectionAndMessageScoringDTO hardScoring(ScoringDataDto scoringDataDto) {
//    List<String> errorMessages = new ArrayList<>();
//
//    for (ScoringHardRules filter : hardFilters) {
//      RejectionAndMessageScoringDTO rejectionAndMessage = filter.check(scoringDataDto);
//      if (!rejectionAndMessage.rejection()) {
//        errorMessages.add(rejectionAndMessage.message());
//      }
//    }
//
//    if (!errorMessages.isEmpty()) {
//      return new RejectionAndMessageScoringDTO(false, String.join(", ", errorMessages));
//    }
//
//    return new RejectionAndMessageScoringDTO(true, "");
//  }
//
//  /**
//   * Выполняет мягкие проверки на основе предоставленных данных и базовой ставки.
//   *
//   * @param scoringDataDto объект {@link ScoringDataDto}, содержащий данные для скоринга.
//   * @param rate базовая ставка для расчета.
//   * @return объект {@link RateAndOtherScoringDto}, содержащий скорректированную ставку и
//   *     дополнительные услуги.
//   */
//  @Override
//  public RateAndOtherScoringDto softScoring(ScoringDataDto scoringDataDto, BigDecimal rate) {
//    var resultList = softFilters.stream().map(filter -> filter.check(scoringDataDto)).toList();
//    BigDecimal diffRate =
//        resultList.stream()
//            .map(RateAndOtherScoringDto::newRate)
//            .reduce(BigDecimal.ZERO, BigDecimal::add);
//    BigDecimal otherService =
//        resultList.stream()
//            .map(RateAndOtherScoringDto::otherService)
//            .reduce(BigDecimal.ZERO, BigDecimal::add);
//    return new RateAndOtherScoringDto(rate.add(diffRate), otherService);
//  }
//
//  /**
//   * Выполняет прескоринг для различных комбинаций правил.
//   *
//   * @return список {@link PreScoringInfoDTO}, содержащий комбинации правил и соответствующие
//   *     скоринговые данные.
//   */
//  @Override
//  public List<PreScoringInfoDTO> preScoring() {
//    int size = loanFilters.size();
//    List<PreScoringInfoDTO> result = new ArrayList<>();
//
//    for (int m = 0; m < (1 << size); ++m) {
//      List<RateAndOtherScoringDto> temp = new ArrayList<>();
//      Map<String, Boolean> rules = new HashMap<>();
//      for (int i = 0; i < size; i++) {
//        PreScoringRules rule = loanFilters.get(i);
//        if ((m >> i & 1) == 1) {
//          rules.put(getRuleName(rule), true);
//          temp.add(rule.check(true));
//        } else {
//          rules.put(getRuleName(rule), false);
//          temp.add(rule.check(false));
//        }
//      }
//      RateAndOtherScoringDto diff =
//          temp.stream()
//              .reduce(
//                  (x, y) ->
//                      new RateAndOtherScoringDto(
//                          x.newRate().add(y.newRate()), x.otherService().add(y.otherService())))
//              .get();
//      result.add(
//          new PreScoringInfoDTO(
//              rules, new RateAndOtherScoringDto(rate.add(diff.newRate()), diff.otherService())));
//    }
//    return result;
//  }
//
//  private String getRuleName(PreScoringRules filter) {
//    return filter.getClass().getSimpleName();
//  }
//}
