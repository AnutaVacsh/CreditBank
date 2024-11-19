package ru.vaschenko.calculator.dto;

import lombok.Getter;
import lombok.Setter;
import ru.vaschenko.calculator.type.Gender;
import ru.vaschenko.calculator.type.MaritalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
public class ScoringDataDto {
    private BigDecimal amount;
    private Integer term;
    private String firstName;
    private String lastName;
    private String middleName;
    private Gender gender;
    private LocalDate birthdate;
    private String passportSeries;
    private String passportNumber;
    private LocalDate passportIssueDate;
    private String passportIssueBranch;
    private MaritalStatus maritalStatus;
    private Integer dependentAmount;
    private EmploymentDto employment;
    private String accountNumber;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
}
