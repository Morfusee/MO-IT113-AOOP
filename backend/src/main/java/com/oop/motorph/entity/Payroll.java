package com.oop.motorph.entity;

import java.util.List;
import java.util.stream.IntStream;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Payroll {

    private Employee employee;

    private List<Attendance> attendance;

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

    // Constructors
    public Payroll(Employee employee, List<Attendance> attendance) {
        this.compensation = employee.getCompensation();
        this.employee = employee;
        this.attendance = attendance;
        this.totalHoursRendered = calculateTotalRenderedHours(attendance);
        this.totalAllowances = calculateTotalAllowances();
        this.sssDeduction = calculateSSSDeduction(calculateGrossSalary());
        this.philhealthDeduction = calculatePhilHealthDeduction(calculateGrossSalary());
        this.pagibigDeduction = calculatePagibigDeduction(calculateGrossSalary());
        this.totalDeductions = calculateTotalDeductions();
        this.taxableSalary = calculateTaxableSalary(calculateGrossSalary());
        this.withHoldingTax = calculateWithholdingTax(taxableSalary);
        this.salaryAfterTax = calculateSalaryAfterTax(taxableSalary);
        this.grossSalary = calculateGrossSalary();
        this.netSalary = calculateNetSalary();
    }

    // Methods
    public Double calculateTotalRenderedHours(List<Attendance> attendances) {
        return attendances.stream().mapToDouble(Attendance::calculateTotalHours).sum();
    }

    public Double calculateTotalAllowances() {
        return compensation.getRiceSubsidy() + compensation.getPhoneAllowance()
                + compensation.getClothingAllowance();
    }

    public Double calculateTotalDeductions() {
        return sssDeduction + philhealthDeduction + pagibigDeduction;
    }

    public Double calculateGrossSalary() {
        return compensation.getHourlyRate() * totalHoursRendered;
    }

    public Double calculatePhilHealthDeduction(Double salary) {
        if (salary > 60000)
            return (double) (1800 / 2);

        if ((salary < 60000) && (salary > 10000)) {
            double result = (salary * 0.03) / 2;
            return result;
        }

        if ((salary <= 10000) && (salary > 0))
            return (double) (300 / 2);

        return 0.0;
    }

    public Double calculateSSSDeduction(Double salary) {
        double[] compensationRanges = { 3250, 3750, 4250, 4750, 5250, 5750, 6250, 6750, 7250, 7750, 8250, 8750, 9250,
                9750, 10250, 10750, 11250, 11750, 12250, 12750, 13250, 13750, 14250, 14750, 15250, 15750, 16250, 16750,
                17250, 17750, 18250, 18750, 19250, 19750, 20250, 20750, 21250, 21750, 22250, 22750, 23250, 23750, 24250,
                24750, Double.MAX_VALUE };

        double[] contributions = { 135.00, 157.50, 180.00, 202.50, 225.00, 247.50, 270.00, 292.50, 315.00, 337.50,
                360.00, 382.50, 405.00, 427.50, 450.00, 472.50, 495.00, 517.50, 540.00, 562.50, 585.00, 607.50, 630.00,
                652.50, 675.00, 697.50, 720.00, 742.50, 765.00, 787.50, 810.00, 832.50, 855.00, 877.50, 900.00, 922.50,
                945.00, 967.50, 990.00, 1012.50, 1035.00, 1057.50, 1080.00, 1102.50, 1125.00 };

        return (salary == 0) ? 0.0
                : contributions[IntStream.range(0, compensationRanges.length)
                        .filter(i -> salary < compensationRanges[i])
                        .findFirst()
                        .orElse(contributions.length - 1)];
    }

    public double calculatePagibigDeduction(Double salary) {
        if (salary > 1500)
            return 100;

        if ((salary < 1500) && (salary > 1000))
            return 50;

        return 0;
    }

    public Double calculateWithholdingTax(Double taxableSalary) {
        if (taxableSalary >= 666667)
            return ((taxableSalary - 666667) * 0.35) + 200833.33;

        if ((taxableSalary < 666667) && (taxableSalary >= 166667))
            return ((taxableSalary - 166667) * 0.32) + 40833.33;

        if ((taxableSalary < 166667) && (taxableSalary >= 66667))
            return ((taxableSalary - 66667) * 0.30) + 10833;

        if ((taxableSalary < 66667) && (taxableSalary >= 33333))
            return ((taxableSalary - 33333) * 0.25) + 2500;

        if ((taxableSalary < 33333) && (taxableSalary >= 20833))
            return ((taxableSalary - 20833) * 0.20);

        if (taxableSalary <= 20832)
            return 0.0;

        return 0.0;
    }

    public Double calculateTaxableSalary(Double grossSalary) {
        return grossSalary - totalDeductions;
    }

    public Double calculateSalaryAfterTax(Double taxableSalary) {
        return taxableSalary - withHoldingTax;
    }

    public Double calculateNetSalary() {
        return salaryAfterTax + totalAllowances;
    }

}
