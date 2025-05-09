import {
  PayrollAllowancesDetails,
  PayrollDeductionsDetails,
  PayrollGrossSalaryDetails,
  PayrollNetSalaryDetails,
  PayrollSalaryAfterTaxDetails,
  PayrollTaxableSalaryDetails,
} from "../types/payroll";

export const grossSalaryDetailsFallback: PayrollGrossSalaryDetails = {
  totalHoursRendered: 0,
  hourlyRate: 0,
  grossSalary: 0,
};

export const allowancesDetailsFallback: PayrollAllowancesDetails = {
  riceSubsidy: 0,
  phoneAllowance: 0,
  clothingAllowance: 0,
  totalAllowances: 0,
};

export const deductionsDetailsFallback: PayrollDeductionsDetails = {
  sssDeduction: 0,
  philhealthDeduction: 0,
  pagibigDeduction: 0,
  totalDeductions: 0,
};

export const taxableSalaryDetailsFallback: PayrollTaxableSalaryDetails = {
  totalDeductions: 0,
  grossSalary: 0,
  taxableSalary: 0,
};

export const salaryAfterTaxDetailsFallback: PayrollSalaryAfterTaxDetails = {
  taxableSalary: 0,
  withHoldingTax: 0,
  salaryAfterTax: 0,
};

export const netSalaryDetailsFallback: PayrollNetSalaryDetails = {
  salaryAfterTax: 0,
  totalAllowances: 0,
  netSalary: 0,
};
