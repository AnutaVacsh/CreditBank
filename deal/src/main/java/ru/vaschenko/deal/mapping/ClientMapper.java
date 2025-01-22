package ru.vaschenko.deal.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.vaschenko.deal.dto.ClientDto;
import ru.vaschenko.deal.dto.LoanStatementRequestDto;
import ru.vaschenko.deal.models.Client;

@Mapper(componentModel = "spring")
public interface ClientMapper {
  @Mapping(target = "passport.series", source = "passportSeries")
  @Mapping(target = "passport.number", source = "passportNumber")
  @Mapping(target = "birthdate", source = "birthdate")
  Client toClient(LoanStatementRequestDto loanStatement);

  ClientDto toClientDto(Client client);
}
