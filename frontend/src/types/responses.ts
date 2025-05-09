import { AttendanceData } from "./attendance";
import { IEmployee } from "./employee";
import { LeaveRequestsData } from "./leaveRequests";
import {
  PayrollAllowancesDetails,
  PayrollDeductionsDetails,
  PayrollGrossSalaryDetails,
  PayrollNetSalaryDetails,
  PayrollSalaryAfterTaxDetails,
  PayrollTaxableSalaryDetails,
} from "./payroll";

export interface APIResponse<T> {
  status: number;
  message: string;
  data: T;
  errors: {
    error: string;
  };
  timestamp: string;
}

export interface FetchedUser {
  employee: IEmployee;
}

export interface FetchedAttendanceAnalytics {
  averageCheckIn: string;
  averageCheckOut: string;
  totalAbsent: number;
  totalLates: number;
  totalPresent: number;
  totalRenderedHours: number;
}

export interface FetchedPayrollDetails
  extends PayrollGrossSalaryDetails,
    PayrollAllowancesDetails,
    PayrollDeductionsDetails,
    PayrollNetSalaryDetails,
    PayrollSalaryAfterTaxDetails,
    PayrollTaxableSalaryDetails {}

export interface FetchedPayrollMonths extends Array<string> {}

export interface FetchedAttendance extends AttendanceData {}

export interface FetchedLeaveRequests extends Array<LeaveRequestsData> {}

export interface FetchedLeaveRequest extends LeaveRequestsData {}
