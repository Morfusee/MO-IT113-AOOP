import { BackendRoutes } from "../constants/backendRoutes";
import axiosInstance from "../factories/axiosInstance";

export const generateEmployeePayrollReportAPI = async (
  title: string,
  employeeNumber: string,
  startDate: string,
  endDate: string
) => {
  const response = await axiosInstance
    .get<Blob>(BackendRoutes.REPORT + "/payroll", {
      params: {
        title: title,
        userId: employeeNumber,
        startDate: startDate,
        endDate: endDate,
      },
      responseType: "blob",
    })
    .then((res) => {
      if (res.status != 200) {
        throw new Error("Failed to generate report.");
      }

      return res;
    });
  return response;
};

export const generateEmployeeAnnualPayrollReportAPI = async (
  title: string,
  employeeNumber: string,
  year: string
) => {
  const response = await axiosInstance
    .get<Blob>(BackendRoutes.REPORT + "/payroll/annual", {
      params: {
        title: title,
        userId: employeeNumber,
        year: year,
      },
      responseType: "blob",
    })
    .then((res) => {
      if (res.status != 200) {
        throw new Error("Failed to generate report.");
      }

      return res;
    });
  return response;
};

export const generateAllEmployeesAnnualPayrollReportAPI = async (
  title: string,
  year: string
) => {
  const response = await axiosInstance
    .get<Blob>(BackendRoutes.REPORT + "/payroll/annual/summary", {
      params: {
        title: title,
        year: year,
      },
      responseType: "blob",
    })
    .then((res) => {
      if (res.status != 200) {
        throw new Error("Failed to generate report.");
      }

      return res;
    });
  return response;
};
