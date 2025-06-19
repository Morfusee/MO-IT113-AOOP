package com.oop.motorph.dto.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.oop.motorph.dto.PayrollDTO;
import com.oop.motorph.entity.Payroll;

/**
 * Maps {@link Payroll} entities to {@link PayrollDTO} objects.
 * This mapper transforms detailed payroll entities, which include nested
 * employee and compensation information,
 * into a flattened DTO suitable for API responses or reporting.
 */
@Service
public class PayrollDTOMapper implements Function<Payroll, PayrollDTO> {
    /**
     * Converts a {@code Payroll} entity to a {@code PayrollDTO} using direct
     * constructor invocation.
     * This method extracts relevant fields from the payroll and its associated
     * employee and compensation
     * entities to populate the DTO.
     *
     * @param payroll The {@link Payroll} entity to map.
     * @return A new {@link PayrollDTO} containing the mapped payroll data.
     */
    @Override
    public PayrollDTO apply(Payroll payroll) {
        return new PayrollDTO(
                payroll.getEmployee().getEmployeeNumber(),
                payroll.getEmployee().getPersonalInfo(),
                payroll.getEmployee().getEmploymentInfo(),
                payroll.getGrossSalary(),
                payroll.getTotalHoursRendered(),
                payroll.getCompensation().getHourlyRate(),
                payroll.getTotalAllowances(),
                payroll.getCompensation().getRiceSubsidy(),
                payroll.getCompensation().getPhoneAllowance(),
                payroll.getCompensation().getClothingAllowance(),
                payroll.getTotalDeductions(),
                payroll.getSssDeduction(),
                payroll.getPhilhealthDeduction(),
                payroll.getPagibigDeduction(),
                payroll.getTaxableSalary(),
                payroll.getSalaryAfterTax(),
                payroll.getWithHoldingTax(),
                payroll.getNetSalary(),
                payroll.getStartDate(),
                payroll.getEndDate());
    }

    /**
     * Converts a {@code Payroll} entity to a {@code PayrollDTO} using a builder
     * pattern.
     * This method offers an alternative, more fluent way to construct the DTO,
     * similarly extracting and mapping data from the payroll and its related
     * entities.
     *
     * @param payroll The {@link Payroll} entity to map.
     * @return A new {@link PayrollDTO} built from the payroll entity data.
     */
    public PayrollDTO mapToPayroll(Payroll payroll) {
        return PayrollDTO.builder()
                .employeeNum(payroll.getEmployee().getEmployeeNumber())
                .personalInfo(payroll.getEmployee().getPersonalInfo())
                .employeeInfo(payroll.getEmployee().getEmploymentInfo())
                .grossSalary(payroll.getGrossSalary())
                .totalHoursRendered(payroll.getTotalHoursRendered())
                .hourlyRate(payroll.getCompensation().getHourlyRate())
                .totalAllowances(payroll.getTotalAllowances())
                .riceSubsidy(payroll.getCompensation().getRiceSubsidy())
                .phoneAllowance(payroll.getCompensation().getPhoneAllowance())
                .clothingAllowance(payroll.getCompensation().getClothingAllowance())
                .totalDeductions(payroll.getTotalDeductions())
                .sssDeduction(payroll.getSssDeduction())
                .philhealthDeduction(payroll.getPhilhealthDeduction())
                .pagibigDeduction(payroll.getPagibigDeduction())
                .taxableSalary(payroll.getTaxableSalary())
                .salaryAfterTax(payroll.getSalaryAfterTax())
                .withHoldingTax(payroll.getWithHoldingTax())
                .netSalary(payroll.getNetSalary())
                .startDate(payroll.getStartDate())
                .endDate(payroll.getEndDate())
                .build();
    }

}