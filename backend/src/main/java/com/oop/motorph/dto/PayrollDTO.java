package com.oop.motorph.dto;

import java.sql.Date;

import com.oop.motorph.entity.EmploymentInfo;
import com.oop.motorph.entity.PersonalInfo;

import lombok.Builder;

/**
 * A Data Transfer Object (DTO) for comprehensive payroll information.
 * This record class aggregates various details necessary for a payroll slip or
 * report,
 * including employee personal and employment information, salary components,
 * allowances,
 * deductions, and tax calculations. The {@code @Builder} annotation from Lombok
 * provides a convenient way to construct instances of this DTO.
 *
 * @param employeeNum         The unique employee number.
 * @param personalInfo        {@link PersonalInfo} containing the employee's
 *                            personal details.
 * @param employeeInfo        {@link EmploymentInfo} containing the employee's
 *                            employment details.
 * @param grossSalary         The total earnings before any deductions.
 * @param totalHoursRendered  The total number of hours worked by the employee
 *                            during the payroll period.
 * @param hourlyRate          The employee's hourly rate of pay.
 * @param totalAllowances     The sum of all allowances provided to the
 *                            employee.
 * @param riceSubsidy         The allowance provided for rice subsidy.
 * @param phoneAllowance      The allowance provided for phone expenses.
 * @param clothingAllowance   The allowance provided for clothing.
 * @param totalDeductions     The sum of all deductions from the gross salary.
 * @param sssDeduction        The deduction for SSS (Social Security System).
 * @param philhealthDeduction The deduction for PhilHealth (Philippine Health
 *                            Insurance Corporation).
 * @param pagibigDeduction    The deduction for Pag-IBIG Fund (Home Development
 *                            Mutual Fund).
 * @param taxableSalary       The portion of the salary subject to tax after
 *                            non-taxable deductions.
 * @param salaryAfterTax      The salary amount remaining after mandatory
 *                            deductions but before withholding tax.
 * @param withHoldingTax      The amount of tax withheld from the employee's
 *                            salary.
 * @param netSalary           The final take-home pay after all deductions and
 *                            taxes.
 * @param startDate           The start date of the payroll period.
 * @param endDate             The end date of the payroll period.
 */
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