import { notifications } from "@mantine/notifications";
import {
  createEmployeeApi,
  deleteEmployeeApi,
  employeesApi,
  editEmployeeApi,
} from "../services/employeeService";
import { EmployeeData, EmployeeRequest } from "../types/employee";
import { APIResponse, FetchedUser } from "../types/responses";
import { AxiosError } from "axios";

function employeeController() {
  const getAllEmployees = async (): Promise<APIResponse<FetchedUser[]>> => {
    try {
      const getAllEmployeesResponse = await employeesApi();

      return getAllEmployeesResponse;
    } catch (error) {
      throw new Error("Failed to get all employees");
    }
  };

  const createEmployee = async (employee: EmployeeRequest) => {
    try {
      const createEmployeeResponse = await createEmployeeApi(employee);

      return createEmployeeResponse;
    } catch (error: unknown | APIResponse<null>) {
      if (error instanceof AxiosError) {
        // Show notification
        notifications.show({
          withBorder: true,
          autoClose: 5000,
          title: error.response?.data.message,
          message: error.response?.data.errors.error,
          color: "red",
        });
      }
      throw new Error("Failed to create employee");
    }
  };

  const deleteEmployee = async (
    employeeNumber: EmployeeData["employeeNumber"]
  ) => {
    try {
      const deleteEmployeeResponse = await deleteEmployeeApi(employeeNumber);

      return deleteEmployeeResponse;
    } catch (error) {
      throw new Error("Failed to delete employee");
    }
  };

  const editEmployee = async (
    employeeNumber: EmployeeData["employeeNumber"],
    employee: EmployeeRequest
  ) => {
    try {
      const editEmployeeResponse = await editEmployeeApi(
        employeeNumber,
        employee
      );

      return editEmployeeResponse;
    } catch (error: unknown | APIResponse<null>) {
      if (error instanceof AxiosError) {
        // Show notification
        notifications.show({
          withBorder: true,
          autoClose: 5000,
          title: error.response?.data.message,
          message: error.response?.data.errors.error,
          color: "red",
        });
      }
      throw new Error("Failed to edit employee");
    }
  };

  return { getAllEmployees, createEmployee, deleteEmployee, editEmployee };
}

export default employeeController;
