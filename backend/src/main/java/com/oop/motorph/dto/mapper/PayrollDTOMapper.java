package com.oop.motorph.dto.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.oop.motorph.dto.PayrollDTO;
import com.oop.motorph.entity.Payroll;

@Service
public class PayrollDTOMapper implements Function<Payroll, PayrollDTO> {
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
