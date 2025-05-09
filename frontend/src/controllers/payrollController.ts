import { payrollApi, payrollMonthsApi } from "../services/payrollService";

function payrollController() {
  const getPayroll = async (
    employeeNumber: number,
    startDate: string,
    endDate: string
  ) => {
    try {
      const payrollResponse = await payrollApi(
        employeeNumber,
        startDate,
        endDate
      );

      return payrollResponse;
    } catch (error) {
      throw new Error("Failed to get payroll");
    }
  };

  const getPayrollMonths = async (selectedDate: Date) => {
    try {
      // Get year
      const year = selectedDate.getUTCFullYear();

      // Fetch payroll months
      const payrollMonthsResponse = await payrollMonthsApi(year);

      return payrollMonthsResponse;
    } catch (error) {
      throw new Error("Failed to get payroll months");
    }
  };

  return { getPayroll, getPayrollMonths };
}

export default payrollController;
