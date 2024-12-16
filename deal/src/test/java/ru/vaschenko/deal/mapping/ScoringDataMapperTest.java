package ru.vaschenko.deal.mapping;

import static java.time.LocalDate.parse;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.vaschenko.deal.dto.EmploymentDto;
import ru.vaschenko.deal.dto.FinishRegistrationRequestDto;
import ru.vaschenko.deal.dto.LoanOfferDto;
import ru.vaschenko.deal.dto.ScoringDataDto;
import ru.vaschenko.deal.models.Client;
import ru.vaschenko.deal.models.Statement;
import ru.vaschenko.deal.models.enams.EmploymentStatus;
import ru.vaschenko.deal.models.enams.Gender;
import ru.vaschenko.deal.models.enams.MaritalStatus;
import ru.vaschenko.deal.models.json.Passport;

class ScoringDataMapperTest {
  private ScoringDataMapper scoringDataMapper;

  @BeforeEach
  void setUp() {
    scoringDataMapper = Mappers.getMapper(ScoringDataMapper.class);
  }

  @Test
  void shouldMapStatementAndFinishRegistrationRequestDtoToScoringDataDto() {
    // Given
    Statement statement =
        new Statement()
            .setClient(
                new Client()
                    .setFirstName("John")
                    .setLastName("Doe")
                    .setMiddleName("Ivanovich")
                    .setBirthdate(parse("1990-01-01"))
                    .setPassport(new Passport().setSeries("1234").setNumber("567890")))
            .setAppliedOffer(
                new Statement()
                    .setAppliedOffer(
                        LoanOfferDto.builder()
                            .requestedAmount(BigDecimal.valueOf(10000))
                            .term(12)
                            .isInsuranceEnabled(true)
                            .isSalaryClient(false)
                            .build())
                    .getAppliedOffer());

    FinishRegistrationRequestDto finishDto =
        FinishRegistrationRequestDto.builder()
            .passportIssueBranch("Some Branch")
            .passportIssueDate(parse("2010-01-01"))
            .gender(Gender.MALE)
            .maritalStatus(MaritalStatus.SINGLE)
            .accountNumber("123456789")
            .employment(EmploymentDto.builder().employmentStatus(EmploymentStatus.EMPLOYED).build())
            .dependentAmount(0)
            .build();

    // When
    ScoringDataDto scoringDataDto = scoringDataMapper.toScoringDataDto(statement, finishDto);

    // Then
    assertNotNull(scoringDataDto);

    assertEquals(BigDecimal.valueOf(10000), scoringDataDto.getAmount());
    assertEquals(12, scoringDataDto.getTerm());
    assertTrue(scoringDataDto.getIsInsuranceEnabled());
    assertFalse(scoringDataDto.getIsSalaryClient());
    assertEquals("John", scoringDataDto.getFirstName());
    assertEquals("Doe", scoringDataDto.getLastName());
    assertEquals("Ivanovich", scoringDataDto.getMiddleName());
    assertEquals(parse("1990-01-01"), scoringDataDto.getBirthdate());
    assertEquals("1234", scoringDataDto.getPassportSeries());
    assertEquals("567890", scoringDataDto.getPassportNumber());
    assertEquals(parse("2010-01-01"), scoringDataDto.getPassportIssueDate());
    assertEquals(Gender.MALE, scoringDataDto.getGender());
    assertEquals(MaritalStatus.SINGLE, scoringDataDto.getMaritalStatus());
    assertEquals("123456789", scoringDataDto.getAccountNumber());
    assertEquals(EmploymentStatus.EMPLOYED, scoringDataDto.getEmployment().getEmploymentStatus());
    assertEquals(0, scoringDataDto.getDependentAmount());
  }
}
