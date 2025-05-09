import { BackendRoutes } from "../constants/backendRoutes";
import axiosInstance from "../factories/axiosInstance";
import {
  APIResponse,
  FetchedAttendance,
  FetchedAttendanceAnalytics,
} from "../types/responses";

export const attendanceAnalyticsApi = async (
  employeeNum: number,
  startDate: string
) => {
  const response = axiosInstance
    .get<APIResponse<FetchedAttendanceAnalytics>>(
      BackendRoutes.ATTENDANCE_ANALYTICS,
      {
        params: {
          employeeNum: employeeNum,
          startDate: startDate,
        },
      }
    )
    .then((res) => {
      if (res.status != 200) {
        throw new Error("Failed to get attendance analytics");
      }

      return res.data;
    });

  return response;
};

export const attendanceApi = async (employeeNum: number, startDate: string) => {
  const response = axiosInstance
    .get<APIResponse<FetchedAttendance[]>>(BackendRoutes.ATTENDANCE, {
      params: {
        employeeNum: employeeNum,
        startDate: startDate,
      },
    })
    .then((res) => {
      if (res.status != 200) {
        throw new Error("Failed to get attendance");
      }

      return res.data;
    });

  return response;
};
