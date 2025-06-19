package com.oop.motorph.entity;

import java.sql.Date;
import java.util.List;
import java.util.stream.IntStream;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a payroll record for an employee, encapsulating all components of
 * their salary for a specific period.
 * This class includes calculations for gross salary, allowances, deductions
 * (SSS, PhilHealth, Pag-IBIG),
 * taxable income, withholding tax, and net salary.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payroll {

    private Employee employee;

    private List<Attendance> attendance;

    /**
     * Unwraps the fields of the {@link Compensation} object directly into the JSON
     * output of this Payroll object.
     * This means compensation-related fields will appear as top-level fields in the
     * JSON, rather than nested under a "compensation" object.
     */
    @JsonUnwrapped
    private Compensation compensation;

    private Double totalAllowances;

    private Double totalHoursRendered;

    private Double sssDeduction;

    private Double philhealthDeduction;

    private Double pagibigDeduction;

    private Double totalDeductions;

    private Double taxableSalary;

    private Double salaryAfterTax;

    private Double withHoldingTax;

    private Double grossSalary;

    private Double netSalary;

    private Date startDate;

    private Date endDate;

    /**
     * Constructs a Payroll object by performing all necessary calculations based on
     * employee information and attendance records for a given period.
     * This constructor initializes compensation, calculates hours, allowances,
     * deductions, and various salary components.
     *
     * @param employee   The {@link Employee} for whom the payroll is being
     *                   generated.
     * @param attendance A list of {@link Attendance} records for the employee
     *                   within the payroll period.
     * @param startDate  The start date of the payroll period.
     * @param endDate    The end date of the payroll period.
     */
    public Payroll(Employee employee, List<Attendance> attendance,
            Date startDate, Date endDate) {
        this.compensation = employee.getCompensation();
        this.employee = employee;
        this.attendance = attendance;
        this.totalHoursRendered = calculateTotalRenderedHours(attendance);
        this.grossSalary = calculateGrossSalary(); // Gross salary needed for some deductions
        this.totalAllowances = calculateTotalAllowances();
        this.sssDeduction = calculateSSSDeduction(this.grossSalary);
        this.philhealthDeduction = calculatePhilHealthDeduction(this.grossSalary);
        this.pagibigDeduction = calculatePagibigDeduction(this.grossSalary);
        this.totalDeductions = calculateTotalDeductions();
        this.taxableSalary = calculateTaxableSalary(this.grossSalary);
        this.withHoldingTax = calculateWithholdingTax(this.taxableSalary);
        this.salaryAfterTax = calculateSalaryAfterTax(this.taxableSalary);
        this.netSalary = calculateNetSalary();
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Calculates the total hours rendered by summing the total hours from all
     * provided attendance records.
     *
     * @param attendances A list of {@link Attendance} records.
     * @return The sum of total hours rendered.
     */
    public Double calculateTotalRenderedHours(List<Attendance> attendances) {
        return attendances.stream().mapToDouble(Attendance::calculateTotalHours).sum();
    }

    /**
     * Calculates the total allowances by summing the rice subsidy, phone allowance,
     * and clothing allowance from the employee's compensation.
     *
     * @return The sum of all allowances.
     */
    public Double calculateTotalAllowances() {
        if (compensation == null) {
            return 0.0;
        }
        return compensation.getRiceSubsidy() + compensation.getPhoneAllowance()
                + compensation.getClothingAllowance();
    }

    /**
     * Calculates the total deductions by summing the SSS, PhilHealth, and Pag-IBIG
     * deductions.
     * This method assumes that individual deduction calculations have already been
     * performed and their values are set in the object.
     *
     * @return The sum of all calculated deductions.
     */
    public Double calculateTotalDeductions() {
        return this.sssDeduction + this.philhealthDeduction + this.pagibigDeduction;
    }

    /**
     * Calculates the gross salary based on the employee's hourly rate and total
     * hours rendered.
     *
     * @return The calculated gross salary.
     */
    public Double calculateGrossSalary() {
        if (compensation == null || compensation.getHourlyRate() == null || totalHoursRendered == null) {
            return 0.0;
        }
        return compensation.getHourlyRate() * totalHoursRendered;
    }

    /**
     * Calculates the PhilHealth deduction based on the provided gross salary.
     * The deduction amount varies based on predefined salary ranges as per
     * PhilHealth contribution tables.
     *
     * @param salary The gross salary used for calculating the PhilHealth deduction.
     * @return The PhilHealth deduction amount.
     */
    public Double calculatePhilHealthDeduction(Double salary) {
        if (salary == null || salary <= 0) {
            return 0.0;
        }
        if (salary > 60000) {
            return (double) (1800 / 2); // Capped at 1800 for monthly, so semi-monthly is 900
        }
        if (salary > 10000 && salary <= 60000) {
            return (salary * 0.03) / 2; // 3% of salary, then divided by 2 for semi-monthly
        }
        if (salary > 0 && salary <= 10000) {
            return (double) (300 / 2); // Minimum 300 for monthly, so semi-monthly is 150
        }
        return 0.0;
    }

    /**
     * Calculates the SSS (Social Security System) deduction based on the provided
     * gross salary.
     * The deduction is determined by predefined compensation ranges and
     * corresponding contribution amounts.
     *
     * @param salary The gross salary used for calculating the SSS deduction.
     * @return The SSS deduction amount.
     */
    public Double calculateSSSDeduction(Double salary) {
        double[] compensationRanges = { 3250, 3750, 4250, 4750, 5250, 5750, 6250, 6750, 7250, 7750, 8250, 8750, 9250,
                9750, 10250, 10750, 11250, 11750, 12250, 12750, 13250, 13750, 14250, 14750, 15250, 15750, 16250, 16750,
                17250, 17750, 18250, 18750, 19250, 19750, 20250, 20750, 21250, 21750, 22250, 22750, 23250, 23750, 24250,
                24750, Double.MAX_VALUE };

        double[] contributions = { 135.00, 157.50, 180.00, 202.50, 225.00, 247.50, 270.00, 292.50, 315.00, 337.50,
                360.00, 382.50, 405.00, 427.50, 450.00, 472.50, 495.00, 517.50, 540.00, 562.50, 585.00, 607.50, 630.00,
                652.50, 675.00, 697.50, 720.00, 742.50, 765.00, 787.50, 810.00, 832.50, 855.00, 877.50, 900.00, 922.50,
                945.00, 967.50, 990.00, 1012.50, 1035.00, 1057.50, 1080.00, 1102.50, 1125.00 };

        // If salary is 0, the contribution is 0.0
        return (salary == 0) ? 0.0
                // Otherwise, find the correct contribution based on the salary bracket.
                // It streams through the compensation ranges to find the first one where the
                // salary is less than the upper bound,
                // and returns the corresponding contribution. If no such range is found (i.e.,
                // salary is very high),
                // it defaults to the last (highest) contribution.
                : contributions[IntStream.range(0, compensationRanges.length)
                        .filter(i -> salary < compensationRanges[i])
                        .findFirst()
                        .orElse(contributions.length - 1)];
    }

    /**
     * Calculates the Pag-IBIG (Home Development Mutual Fund) deduction based on the
     * provided gross salary.
     * The deduction is fixed at 100 if salary is above 1500, 50 if between 1000 and
     * 1500, otherwise 0.
     *
     * @param salary The gross salary used for calculating the Pag-IBIG deduction.
     * @return The Pag-IBIG deduction amount.
     */
    public double calculatePagibigDeduction(Double salary) {
        if (salary == null || salary <= 0) {
            return 0.0;
        }
        if (salary > 1500) {
            return 100;
        }
        if (salary > 1000 && salary <= 1500) {
            return 50;
        }
        return 0;
    }

    /**
     * Calculates the Withholding Tax based on the taxable salary using tiered tax
     * brackets applicable in the Philippines.
     *
     * @param taxableSalary The employee's taxable salary.
     * @return The calculated withholding tax amount.
     */
    public Double calculateWithholdingTax(Double taxableSalary) {
        if (taxableSalary == null || taxableSalary <= 0) {
            return 0.0;
        }

        if (taxableSalary >= 666667) {
            return ((taxableSalary - 666667) * 0.35) + 200833.33;
        }
        if (taxableSalary >= 166667) { // Corrected order to avoid overlapping ranges
            return ((taxableSalary - 166667) * 0.32) + 40833.33;
        }
        if (taxableSalary >= 66667) {
            return ((taxableSalary - 66667) * 0.30) + 10833;
        }
        if (taxableSalary >= 33333) {
            return ((taxableSalary - 33333) * 0.25) + 2500;
        }
        if (taxableSalary >= 20833) {
            return ((taxableSalary - 20833) * 0.20);
        }
        // If taxableSalary is less than 20833, the tax is 0.0
        return 0.0;
    }

    /**
     * Calculates the taxable salary by subtracting the total deductions from the
     * gross salary.
     * This method assumes that gross salary and total deductions have already been
     * calculated.
     *
     * @param grossSalary The employee's gross salary.
     * @return The calculated taxable salary.
     */
    public Double calculateTaxableSalary(Double grossSalary) {
        if (grossSalary == null) {
            return 0.0;
        }
        // Ensure totalDeductions is not null, initialize if it hasn't been calculated
        // yet.
        if (this.totalDeductions == null) {
            this.totalDeductions = calculateTotalDeductions();
        }
        return grossSalary - this.totalDeductions;
    }

    /**
     * Calculates the salary after tax by subtracting the withholding tax from the
     * taxable salary.
     * This method assumes that taxable salary and withholding tax have already been
     * calculated.
     *
     * @param taxableSalary The employee's taxable salary.
     * @return The salary remaining after withholding tax.
     */
    public Double calculateSalaryAfterTax(Double taxableSalary) {
        if (taxableSalary == null) {
            return 0.0;
        }
        // Ensure withHoldingTax is not null, initialize if it hasn't been calculated
        // yet.
        if (this.withHoldingTax == null) {
            this.withHoldingTax = calculateWithholdingTax(taxableSalary);
        }
        return taxableSalary - this.withHoldingTax;
    }

    /**
     * Calculates the net salary by adding total allowances to the salary after tax.
     * This represents the final take-home pay for the employee.
     * This method assumes that salary after tax and total allowances have already
     * been calculated.
     *
     * @return The calculated net salary.
     */
    public Double calculateNetSalary() {
        // Ensure salaryAfterTax and totalAllowances are not null, initialize if they
        // haven't been calculated yet.
        if (this.salaryAfterTax == null && this.taxableSalary != null) {
            this.salaryAfterTax = calculateSalaryAfterTax(this.taxableSalary);
        } else if (this.salaryAfterTax == null) {
            return 0.0; // Cannot calculate if taxableSalary is also null
        }

        if (this.totalAllowances == null) {
            this.totalAllowances = calculateTotalAllowances();
        }
        return this.salaryAfterTax + this.totalAllowances;
    }
}