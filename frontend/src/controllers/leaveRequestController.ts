import {
  createLeaveRequestApi,
  deleteLeaveRequestApi,
  editLeaveRequestHRApi,
  getAllLeaveRequestEmployeeApi,
  getAllLeaveRequestHRApi,
} from "../services/leaveRequestService";
import { LeaveRequestsData } from "../types/leaveRequests";

function leaveRequestController() {
  const getLeaveRequests = async (
    employeeNum: number | null,
    status: string
  ) => {
    try {
      const getAllLeaveRequestsHRResponse = await getAllLeaveRequestHRApi(
        employeeNum,
        status
      );

      return getAllLeaveRequestsHRResponse;
    } catch (error) {
      console.error(error);

      return null;
    }
  };

  const editLeaveRequests = async (
    leaveRequest: LeaveRequestsData,
    isAdmin: boolean
  ) => {
    if (isAdmin) {
      try {
        const getAllLeaveRequestsHRResponse = await editLeaveRequestHRApi(
          leaveRequest
        );

        return getAllLeaveRequestsHRResponse;
      } catch (error) {
        console.error(error);

        return null;
      }
    }

    throw new Error("User is not authorized to edit leave requests.");
  };

  const deleteLeaveRequests = async (leaveRequest: LeaveRequestsData) => {
    try {
      const getAllLeaveRequestsHRResponse = await deleteLeaveRequestApi(
        leaveRequest
      );

      return getAllLeaveRequestsHRResponse;
    } catch (error) {
      console.error(error);

      return null;
    }
  };

  const createLeaveRequest = async (
    leaveRequest: Omit<LeaveRequestsData, "id" | "employeeName" | "notes">
  ) => {
    try {
      const createLeaveRequestResponse = await createLeaveRequestApi(
        leaveRequest
      );

      return createLeaveRequestResponse;
    } catch (error) {
      console.error(error);

      return null;
    }
  };

  return {
    getLeaveRequests,
    editLeaveRequests,
    deleteLeaveRequests,
    createLeaveRequest,
  };
}

export default leaveRequestController;
