import { BackendRoutes } from "../constants/backendRoutes";
import axiosInstance from "../factories/axiosInstance";
import { EmployeeData, EmployeeRequest } from "../types/employee";
import { APIResponse, FetchedUser } from "../types/responses";

export const employeesApi = async () => {
  const response = await axiosInstance
    .get<APIResponse<FetchedUser[]>>(BackendRoutes.HR.EMPLOYEES)
    .then((res) => {
      if (res.status != 200) {
        throw new Error("Failed to get attendance analytics");
      }

      return res.data;
    });

  return response;
};

export const createEmployeeApi = async (employee: EmployeeRequest) => {
  const response = await axiosInstance
    .post(BackendRoutes.HR.EMPLOYEES, employee)
    .then((res) => {
      if (res.status != 200) {
        throw new Error("Failed to create employee");
      }

      return res.data;
    });

  return response;
};

export const deleteEmployeeApi = async (
  employeeNumber: EmployeeData["employeeNumber"]
) => {
  const response = await axiosInstance
    .delete(BackendRoutes.HR.EMPLOYEES + `/${employeeNumber}`)
    .then((res) => {
      if (res.status != 200) {
        throw new Error("Failed to delete employee");
      }

      return res.data;
    });

  return response;
};

export const editEmployeeApi = async (
  employeeNumber: EmployeeData["employeeNumber"],
  employee: EmployeeRequest
) => {
  const response = await axiosInstance
    .patch(BackendRoutes.HR.EMPLOYEES + `/${employeeNumber}`, employee)
    .then((res) => {
      if (res.status != 200) {
        throw new Error("Failed to edit employee");
      }

      return res.data;
    });

  return response;
};
