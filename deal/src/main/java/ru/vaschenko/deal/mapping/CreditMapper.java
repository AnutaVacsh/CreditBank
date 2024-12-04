package ru.vaschenko.deal.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.vaschenko.deal.dto.CreditDto;
import ru.vaschenko.deal.models.Credit;

@Mapper(componentModel = "spring")
public interface CreditMapper {
  @Mapping(target = "insuranceEnabled", source = "isInsuranceEnabled")
  @Mapping(target = "salaryClient", source = "isSalaryClient")
  Credit toCredit(CreditDto creditDto);

  @Mapping(target = "isInsuranceEnabled", source = "insuranceEnabled")
  @Mapping(target = "isSalaryClient", source = "salaryClient")
  CreditDto toCreditDto(Credit credit);
}
