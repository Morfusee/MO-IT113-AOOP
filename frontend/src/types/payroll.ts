export interface PayrollGrossSalaryDetails {
  totalHoursRendered: number;
  hourlyRate: number;
  grossSalary: number;
}

export interface PayrollAllowancesDetails {
  riceSubsidy: number;
  phoneAllowance: number;
  clothingAllowance: number;
  totalAllowances: number;
}

export interface PayrollDeductionsDetails {
  sssDeduction: number;
  philhealthDeduction: number;
  pagibigDeduction: number;
  totalDeductions: number;
}

export interface PayrollTaxableSalaryDetails {
  totalDeductions: number;
  grossSalary: number;
  taxableSalary: number;
}

export interface PayrollSalaryAfterTaxDetails {
  taxableSalary: number;
  withHoldingTax: number;
  salaryAfterTax: number;
}

export interface PayrollNetSalaryDetails {
  salaryAfterTax: number;
  totalAllowances: number;
  netSalary: number;
}
