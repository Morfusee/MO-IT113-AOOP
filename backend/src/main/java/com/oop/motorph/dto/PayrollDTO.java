package com.oop.motorph.dto;

import java.sql.Date;

import com.oop.motorph.entity.EmploymentInfo;
import com.oop.motorph.entity.PersonalInfo;

import lombok.Builder;

@Builder
public record PayrollDTO(
        Long employeeNum,
        PersonalInfo personalInfo,
        EmploymentInfo employeeInfo,
        Double grossSalary,
        Double totalHoursRendered,
        Double hourlyRate,
        Double totalAllowances,
        Double riceSubsidy,
        Double phoneAllowance,
        Double clothingAllowance,
        Double totalDeductions,
        Double sssDeduction,
        Double philhealthDeduction,
        Double pagibigDeduction,
        Double taxableSalary,
        Double salaryAfterTax,
        Double withHoldingTax,
        Double netSalary,
        Date startDate,
        Date endDate) {

}
