package ru.vaschenko.deal.mapping;

import static java.time.LocalDate.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.vaschenko.deal.dto.LoanStatementRequestDto;
import ru.vaschenko.deal.models.Client;

class ClientMapperTest {

  private ClientMapper clientMapper;

  @BeforeEach
  void setUp() {
    clientMapper = Mappers.getMapper(ClientMapper.class);
  }

  @Test
  void shouldMapLoanStatementRequestDtoToClient() {
    // Given
    LoanStatementRequestDto loanStatement =
        LoanStatementRequestDto.builder()
            .passportNumber("123456")
            .passportSeries("1234")
            .birthdate(parse("2000-01-01"))
            .build();

    // When
    Client client = clientMapper.toClient(loanStatement);

    // Then
    assertNotNull(client);

    assertEquals("1234", client.getPassport().getSeries());
    assertEquals("123456", client.getPassport().getNumber());
    assertEquals(parse("2000-01-01"), client.getBirthdate());
  }
}
