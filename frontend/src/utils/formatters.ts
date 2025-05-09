import {
  IconArrowDown,
  IconArrowUp,
  IconBriefcase,
  IconBriefcase2,
  IconCalendarWeek,
  IconCash,
  IconClockPause,
  IconExclamationCircle,
  IconIdBadge2,
  IconUser,
} from "@tabler/icons-react";
import {
  EmployeeFormValues,
  EmployeeRequest,
  IEmployee
} from "../types/employee";
import {
  PayrollAllowancesDetails,
  PayrollDeductionsDetails,
  PayrollGrossSalaryDetails,
  PayrollNetSalaryDetails,
  PayrollSalaryAfterTaxDetails,
  PayrollTaxableSalaryDetails,
} from "../types/payroll";
import {
  APIResponse,
  FetchedAttendanceAnalytics,
  FetchedPayrollDetails,
} from "../types/responses";
import { calculateAge, dateToString } from "./dateUtils";

export const formatFullName = (
  firstName: string = "John",
  lastName: string = "Doe"
) => {
  return `${firstName} ${lastName}`;
};

export const formatEmployeeDetails = (user: IEmployee) => {
  return [
    formatPersonalInformation(user),
    formatEmploymentInformation(user),
    formatGovernmentInformation(user),
    formatCompensation(user),
  ];
};

export const formatPersonalInformation = (user: IEmployee) => {
  return {
    title: "Personal Information",
    icon: IconUser,
    details: [
      {
        title: "Birthdate",
        value: user.personalInfo.birthday,
      },
      {
        title: "Age",
        value: calculateAge(new Date(user.personalInfo.birthday)),
      },
      {
        title: "Phone No.",
        value: user.personalInfo.phoneNumber,
      },
      {
        title: "Address",
        value: user.personalInfo.address,
      },
    ],
  };
};

export const formatEmploymentInformation = (user: IEmployee) => {
  return {
    title: "Employment Details",
    icon: IconBriefcase,
    details: [
      {
        title: "Position",
        value: user.employmentInfo.position,
      },
      {
        title: "Supervisor",
        value: user.employmentInfo.immediateSupervisor,
      },
      {
        title: "Employment Status",
        value: user.employmentInfo.status,
      },
      {
        title: "Hourly Rate",
        value: user.compensation.hourlyRate,
      },
    ],
  };
};

export const formatGovernmentInformation = (user: IEmployee) => {
  return {
    title: "Government IDs",
    icon: IconIdBadge2,
    details: [
      {
        title: "SSS",
        value: user.governmentIds.sss,
      },
      {
        title: "PhilHealth",
        value: user.governmentIds.philhealth,
      },
      {
        title: "Pag-IBIG",
        value: user.governmentIds.pagibig,
      },
      {
        title: "TIN",
        value: user.governmentIds.tin,
      },
    ],
  };
};

export const formatCompensation = (user: IEmployee) => {
  return {
    title: "Salary & Allowances",
    icon: IconCash,
    details: [
      {
        title: "Basic Salary",
        value: user.compensation.basicSalary,
      },
      {
        title: "Gross Semi-Monthly Rate",
        value: user.compensation.grossSemiMonthlyRate,
      },
      {
        title: "Hourly Rate",
        value: user.compensation.hourlyRate,
      },
      {
        title: "Rice Subsidy",
        value: user.compensation.riceSubsidy,
      },
      {
        title: "Phone Allowance",
        value: user.compensation.phoneAllowance,
      },
      {
        title: "Clothing Allowance",
        value: user.compensation.clothingAllowance,
      },
    ],
  };
};

export const formatAttendanceAnalytics = (
  analytics: APIResponse<FetchedAttendanceAnalytics>
) => {
  return {
    present: {
      title: "Present",
      icon: IconCalendarWeek,
      value: analytics?.data.totalPresent + analytics?.data.totalLates,
    },
    lates: {
      title: "Lates",
      icon: IconClockPause,
      value: analytics?.data.totalLates,
    },
    absent: {
      title: "Absent",
      icon: IconExclamationCircle,
      value: analytics?.data.totalAbsent,
    },
    renderedHours: {
      title: "Rendered Hours",
      icon: IconBriefcase2,
      value: analytics?.data.totalRenderedHours,
    },
    averageCheckIn: {
      title: "Average Check In",
      icon: IconArrowDown,
      value: analytics?.data.averageCheckIn || "00:00:00",
    },
    averageCheckOut: {
      title: "Average Check Out",
      icon: IconArrowUp,
      value: analytics?.data.averageCheckOut || "00:00:00",
    },
  };
};

export const formatGrossSalary = (
  data: FetchedPayrollDetails
): PayrollGrossSalaryDetails => {
  return {
    totalHoursRendered: data.totalHoursRendered,
    hourlyRate: data.hourlyRate,
    grossSalary: data.grossSalary,
  };
};

export const formatAllowances = (
  data: FetchedPayrollDetails
): PayrollAllowancesDetails => {
  return {
    riceSubsidy: data.riceSubsidy,
    phoneAllowance: data.phoneAllowance,
    clothingAllowance: data.clothingAllowance,
    totalAllowances: data.totalAllowances,
  };
};

export const formatDeductions = (
  data: FetchedPayrollDetails
): PayrollDeductionsDetails => {
  return {
    pagibigDeduction: data.pagibigDeduction,
    philhealthDeduction: data.philhealthDeduction,
    sssDeduction: data.sssDeduction,
    totalDeductions: data.totalDeductions,
  };
};

export const formatTaxableSalary = (
  data: FetchedPayrollDetails
): PayrollTaxableSalaryDetails => {
  return {
    totalDeductions: data.totalDeductions,
    grossSalary: data.grossSalary,
    taxableSalary: data.taxableSalary,
  };
};

export const formatSalaryAfterTax = (
  data: FetchedPayrollDetails
): PayrollSalaryAfterTaxDetails => {
  return {
    taxableSalary: data.taxableSalary,
    withHoldingTax: data.withHoldingTax,
    salaryAfterTax: data.salaryAfterTax,
  };
};

export const formatNetSalary = (
  data: FetchedPayrollDetails
): PayrollNetSalaryDetails => {
  return {
    salaryAfterTax: data.salaryAfterTax,
    totalAllowances: data.totalAllowances,
    netSalary: data.netSalary,
  };
};

export const formatEmployeeRequest = (
  data: EmployeeFormValues
): EmployeeRequest => {
  return {
    user: {
      username: data.username,
    },
    personalInfo: formatPersonalInfoRequest(data),
    employmentInfo: formatEmployeeInfoRequest(data),
    governmentIds: formatGovernmentInfoRequest(data),
    compensation: formatCompensationRequest(data),
  };
};

export const formatPersonalInfoRequest = (
  data: EmployeeFormValues
): EmployeeRequest["personalInfo"] => {
  return {
    lastName: data.lastName,
    firstName: data.firstName,
    birthday: dateToString(data.birthdate),
    address: data.address,
    phoneNumber: data.phoneNumber,
  };
};

export const formatEmployeeInfoRequest = (
  data: EmployeeFormValues
): EmployeeRequest["employmentInfo"] => {
  return {
    status: data.employmentStatus,
    position: data.position,
    immediateSupervisor: data.supervisor,
  };
};

export const formatGovernmentInfoRequest = (
  data: EmployeeFormValues
): EmployeeRequest["governmentIds"] => {
  return {
    pagibig: data.pagibig,
    philhealth: data.philhealth,
    sss: data.sss,
    tin: data.tin,
  };
};

export const formatCompensationRequest = (
  data: EmployeeFormValues
): EmployeeRequest["compensation"] => {
  return {
    basicSalary: data.basicSalary,
    grossSemiMonthlyRate: data.grossSemiMonthlyRate,
    hourlyRate: data.hourlyRate,
    riceSubsidy: data.riceSubsidy,
    phoneAllowance: data.phoneAllowance,
    clothingAllowance: data.clothingAllowance,
  };
};
