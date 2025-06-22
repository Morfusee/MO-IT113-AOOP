import { BackendRoutes } from "../constants/backendRoutes";
import axiosInstance from "../factories/axiosInstance";
import {
  APIResponse,
  FetchedPayrollDetails,
  FetchedPayrollMonths,
} from "../types/responses";

export const payrollApi = async (
  employeeNumber: number,
  startDate: string,
  endDate: string
) => {
  const response = await axiosInstance
    .get<APIResponse<FetchedPayrollDetails>>(BackendRoutes.PAYROLL, {
      params: {
        employeeNum: employeeNumber,
        startDate,
        endDate,
      },
    })
    .then((res) => {
      if (res.status != 200) {
        throw new Error("Failed to get payroll");
      }

      return res.data;
    });

  return response;
};

export const payrollMonthsApi = async (year: number) => {
  const response = await axiosInstance
    .get<APIResponse<FetchedPayrollMonths>>(BackendRoutes.PAYROLL + "/months", {
      params: {
        year,
      },
    })
    .then((res) => {
      if (res.status != 200) {
        throw new Error("Failed to get payroll months");
      }

      return res.data;
    });

  return response;
};
