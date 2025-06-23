import { notifications } from "@mantine/notifications";
import { reportRequestNotification } from "../constants/notificationData";
import {
  getFilenameFromContentDisposition,
  downloadFile,
} from "../utils/downloadUtils";
import {
  generateAllEmployeesAnnualPayrollReportAPI,
  generateEmployeeAnnualPayrollReportAPI,
  generateEmployeePayrollReportAPI,
} from "../services/reportService";

function reportController() {
  const getEmployeePayrollReport = async (
    title: string,
    employeeNumber: string,
    startDate: string,
    endDate: string
  ) => {
    // More robust ID
    const notificationId = `generate-report-notification-${Date.now()}`;

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
      const response = await generateEmployeePayrollReportAPI(
        title,
        employeeNumber,
        startDate,
        endDate
      );

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

  const getAnnualEmployeePayrollReport = async (
    title: string,
    employeeNumber: string,
    year: string
  ) => {
    // More robust ID
    const notificationId = `generate-report-notification-${Date.now()}`;

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
      const response = await generateEmployeeAnnualPayrollReportAPI(
        title,
        employeeNumber,
        year
      );

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

  const getAllEmployeesPayrollSummaryReport = async (
    title: string,
    year: string
  ) => {
    // More robust ID
    const notificationId = `generate-report-notification-${Date.now()}`;

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
      const response = await generateAllEmployeesAnnualPayrollReportAPI(
        title,
        year
      );

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

  return {
    getEmployeePayrollReport,
    getAnnualEmployeePayrollReport,
    getAllEmployeesPayrollSummaryReport,
  };
}

export default reportController;
