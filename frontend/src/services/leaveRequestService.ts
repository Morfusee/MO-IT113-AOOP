import { BackendRoutes } from "../constants/backendRoutes";
import axiosInstance from "../factories/axiosInstance";
import { LeaveRequestsData } from "../types/leaveRequests";
import {
  APIResponse,
  FetchedLeaveRequest,
  FetchedLeaveRequests,
} from "../types/responses";

export const getAllLeaveRequestHRApi = async (
  employeeNum: number | null,
  status: string
) => {
  const response = await axiosInstance
    .get<APIResponse<FetchedLeaveRequests>>(BackendRoutes.HR.LEAVE_REQUEST, {
      params: {
        employeeNum: employeeNum,
        status: status,
      },
    })
    .then((res) => {
      if (res.status != 200) {
        throw new Error("Failed to authenticate");
      }

      return res.data;
    });

  return response;
};

export const getAllLeaveRequestEmployeeApi = async (
  employeeNum: number | null,
  status: string
) => {
  const response = await axiosInstance
    .get<APIResponse<FetchedLeaveRequests>>(BackendRoutes.LEAVE_REQUEST, {
      params: {
        employeeNum: employeeNum,
        status: status,
      },
    })
    .then((res) => {
      if (res.status != 200) {
        throw new Error("Failed to get leave requests");
      }

      return res.data;
    });

  return response;
};

export const editLeaveRequestHRApi = async (
  leaveRequest: LeaveRequestsData
) => {
  const response = await axiosInstance
    .patch<APIResponse<FetchedLeaveRequests>>(
      BackendRoutes.HR.LEAVE_REQUEST,
      leaveRequest
    )
    .then((res) => {
      if (res.status != 200) {
        throw new Error("Failed to edit leave requests");
      }

      return res.data;
    });

  return response;
};

export const deleteLeaveRequestApi = async (
  leaveRequest: LeaveRequestsData
) => {
  const response = await axiosInstance
    .delete<APIResponse<FetchedLeaveRequest>>(
      BackendRoutes.LEAVE_REQUEST + `/${leaveRequest.id}`
    )
    .then((res) => {
      if (res.status != 200) {
        throw new Error("Failed to delete leave requests");
      }

      return res.data;
    });

  return response;
};

export const createLeaveRequestApi = async (
  leaveRequest: Omit<LeaveRequestsData, "id" | "employeeName" | "notes">
) => {
  const response = await axiosInstance
    .post<APIResponse<FetchedLeaveRequest>>(
      BackendRoutes.LEAVE_REQUEST,
      leaveRequest
    )
    .then((res) => {
      if (res.status != 200) {
        throw new Error("Failed to create leave requests");
      }

      return res.data;
    });

  return response;
};
