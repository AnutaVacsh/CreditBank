package ru.vaschenko.deal.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.vaschenko.deal.dto.StatementDto;
import ru.vaschenko.deal.models.Statement;

@Mapper(componentModel = "spring")
public interface StatementMapping {
    @Mapping(target = "clientDto", source = "client")
    @Mapping(target = "creditDto", source = "credit")
    StatementDto toStatementDto(Statement statement);
}
