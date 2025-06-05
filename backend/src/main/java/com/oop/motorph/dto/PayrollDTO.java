package com.oop.motorph.dto;

import java.sql.Date;

import lombok.Builder;

@Builder
public record PayrollDTO(
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
