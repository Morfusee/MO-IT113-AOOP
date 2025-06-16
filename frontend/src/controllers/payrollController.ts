import { notifications } from "@mantine/notifications";
import { reportRequestNotification } from "../constants/notificationData";
import {
  payrollApi,
  payrollGenerateReport,
  payrollMonthsApi,
} from "../services/payrollService";
import {
  getFilenameFromContentDisposition,
  downloadFile,
} from "../utils/downloadUtils";

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

  const getGeneratePayrollReport = async () => {
    const notificationId = `generate-report-notification-${Date.now()}`; // More robust ID

    // Show the initial loading notification
    notifications.show({
      loading: true,
      withBorder: true,
      autoClose: false,
      withCloseButton: false,
      id: notificationId,
      title: reportRequestNotification.generate.loading.title,
      message: reportRequestNotification.generate.loading.message,
    });

    try {
      const response = await payrollGenerateReport();

      const blob = response.data;

      // Create a temporary download URL
      const url = window.URL.createObjectURL(blob);

      // Safely extract the filename
      const filename = getFilenameFromContentDisposition(
        "report.pdf",
        response.headers?.["content-disposition"] as string
      );

      // Trigger the file download
      downloadFile(url, filename);

      // Clean up the object URL
      window.URL.revokeObjectURL(url);

      // Show notification
      notifications.update({
        id: notificationId,
        withBorder: true,
        autoClose: 5000,
        loading: false,
        title: reportRequestNotification.generate.success.title,
        message: reportRequestNotification.generate.success.message,
      });
    } catch (error) {
      // Show notification
      notifications.show({
        withBorder: true,
        autoClose: 5000,
        title: reportRequestNotification.generate.error.title,
        message: reportRequestNotification.generate.error.message,
        color: "red",
      });

      console.error("Error generating report:", error);
    }
  };

  return { getPayroll, getPayrollMonths, getGeneratePayrollReport };
}

export default payrollController;
