package ru.vaschenko.calculator.dto;

import lombok.Getter;
import lombok.Setter;
import ru.vaschenko.calculator.type.EmploymentStatus;
import ru.vaschenko.calculator.type.Position;

import java.math.BigDecimal;

@Setter @Getter
public class EmploymentDto {

    private EmploymentStatus employmentStatus;
    private String employerINN;
    private BigDecimal salary;
    private Position position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
