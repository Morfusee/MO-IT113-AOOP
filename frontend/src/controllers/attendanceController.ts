import {
  attendanceAnalyticsApi,
  attendanceApi,
} from "../services/attendanceService";
import { dateToString } from "../utils/dateUtils";

function attendanceController() {
  const getAttendanceAnalytics = async (
    employeeNum: number | undefined,
    startDate: Date
  ) => {
    try {
      if (!employeeNum) return null;

      // Convert the date to a string
      const startDateStr = dateToString(startDate);

      // Call the API
      const attendanceAnalyticsResponse = await attendanceAnalyticsApi(
        employeeNum,
        startDateStr
      );

      return attendanceAnalyticsResponse;
    } catch (error) {
      console.error(error);
      return null;
    }
  };

  const getAttendance = async (
    employeeNum: number | undefined,
    startDate: Date
  ) => {
    try {
      if (!employeeNum) return null;

      // Convert the date to a string
      const startDateStr = dateToString(startDate);

      // Call the API
      const attendanceResponse = await attendanceApi(employeeNum, startDateStr);

      return attendanceResponse;
    } catch (error) {
      console.error(error);
      return null;
    }
  };

  return {
    getAttendanceAnalytics,
    getAttendance,
  };
}

export default attendanceController;
