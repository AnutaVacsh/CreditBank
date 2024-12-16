package ru.vaschenko.deal.mapping;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.vaschenko.deal.dto.CreditDto;
import ru.vaschenko.deal.models.Credit;

class CreditMapperTest {

  private CreditMapper creditMapper;

  @BeforeEach
  void setUp() {
    creditMapper = Mappers.getMapper(CreditMapper.class);
  }

  @Test
  void shouldMapCreditDtoToCredit() {
    // Given
    CreditDto creditDto =
        CreditDto.builder().isInsuranceEnabled(true).isSalaryClient(false).build();

    // When
    Credit credit = creditMapper.toCredit(creditDto);

    // Then
    assertNotNull(credit);

    assertTrue(credit.getInsuranceEnabled());
    assertFalse(credit.getSalaryClient());
  }

  @Test
  void shouldMapCreditToCreditDto() {
    // Given
    Credit credit = new Credit().setInsuranceEnabled(true).setSalaryClient(false);

    // When
    CreditDto creditDto = creditMapper.toCreditDto(credit);

    // Then
    assertNotNull(creditDto, "CreditDto should not be null");

    assertTrue(creditDto.getIsInsuranceEnabled());
    assertFalse(creditDto.getIsSalaryClient());
  }
}
