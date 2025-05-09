import { Button } from "@mantine/core";
import { leaveRequestNotification } from "../../constants/notificationData";
import leaveRequestController from "../../controllers/leaveRequestController";
import userController from "../../controllers/userController";
import { LeaveRequestsData } from "../../types/leaveRequests";
import { isAdmin } from "../../utils/permissionUtils";
import { notifications } from "@mantine/notifications";
import { useSearchParams } from "react-router";
import { openConfirmModal } from "../../constants/leaveRequestConfirmationModals";
import { useMemo } from "react";

function LeaveRequestButtonGroup({
  selectedRows,
  setSelectedRows,
}: {
  selectedRows: LeaveRequestsData[];
  setSelectedRows: React.Dispatch<React.SetStateAction<LeaveRequestsData[]>>;
}) {
  const [searchParams, setSearchParams] = useSearchParams();

  const { editLeaveRequests, deleteLeaveRequests } = leaveRequestController();
  const { getUser } = userController();

  const handleApproveRequests = async () => {
    try {
      const userPosition = getUser()?.employmentInfo.position;

      if (selectedRows.length == 0)
        throw new Error("No leave requests selected.");

      // Create an array of promises
      const updatedPromises = selectedRows.map((leaveRequest) =>
        editLeaveRequests(
          { ...leaveRequest, status: "Approved" },
          isAdmin(userPosition)
        )
      );

      // Wait for all requests to finish
      await Promise.all(updatedPromises);

      // Show notification
      notifications.show({
        withBorder: true,
        autoClose: 5000,
        title: leaveRequestNotification.approved.success.title,
        message: leaveRequestNotification.approved.success.message,
      });

      // Clear the selected rows after all requests are done
      setSelectedRows([]);
    } catch (error) {
      console.error(error);

      // Show notification
      notifications.show({
        withBorder: true,
        autoClose: 5000,
        title: leaveRequestNotification.approved.error.title,
        message: leaveRequestNotification.approved.error.message,
      });
    }
  };

  const handleDenyRequests = async () => {
    try {
      const userPosition = getUser()?.employmentInfo.position;

      if (selectedRows.length == 0)
        throw new Error("No leave requests selected.");

      // Create an array of promises
      const updatedPromises = selectedRows.map((leaveRequest) =>
        editLeaveRequests(
          { ...leaveRequest, status: "Denied" },
          isAdmin(userPosition)
        )
      );

      // Wait for all requests to finish
      await Promise.all(updatedPromises);

      // Show notification
      notifications.show({
        withBorder: true,
        autoClose: 5000,
        title: leaveRequestNotification.denied.success.title,
        message: leaveRequestNotification.denied.success.message,
      });

      // Clear the selected rows after all requests are done
      setSelectedRows([]);
    } catch (error) {
      console.error(error);

      // Show notification
      notifications.show({
        withBorder: true,
        autoClose: 5000,
        title: leaveRequestNotification.denied.error.title,
        message: leaveRequestNotification.denied.error.message,
      });
    }
  };

  const handleDeleteRequests = async () => {
    try {
      if (selectedRows.length == 0)
        throw new Error("No leave requests selected.");

      const deletedPromises = selectedRows.map((leaveRequest) =>
        deleteLeaveRequests(leaveRequest)
      );

      await Promise.all(deletedPromises);

      // Show notification
      notifications.show({
        withBorder: true,
        autoClose: 5000,
        title: leaveRequestNotification.deleted.success.title,
        message: leaveRequestNotification.deleted.success.message,
      });

      // Clear the selected rows after all requests are done
      setSelectedRows([]);
    } catch (error) {
      console.error(error);

      // Show notification
      notifications.show({
        withBorder: true,
        autoClose: 5000,
        title: leaveRequestNotification.deleted.error.title,
        message: leaveRequestNotification.deleted.error.message,
      });
    }
  };

  const isApproveButtonDisabled = () => {
    if (searchParams.get("status") === "Approved" || selectedRows.length == 0)
      return true;

    return false;
  };

  const isDenyButtonDisabled = () => {
    if (searchParams.get("status") === "Denied" || selectedRows.length == 0)
      return true;

    return false;
  };

  const isDeleteButtonDisabled = () => {
    return selectedRows.length === 0;
  };

  const memoizedIsAdmin = useMemo(
    () => isAdmin(getUser()?.employmentInfo.position),
    [getUser]
  );

  return (
    <>
      {memoizedIsAdmin && (
        <>
          <Button
            disabled={isApproveButtonDisabled()}
            onClick={() => openConfirmModal("approve", handleApproveRequests)}
            variant="default"
          >
            Approve
          </Button>
          <Button
            disabled={isDenyButtonDisabled()}
            onClick={() => openConfirmModal("deny", handleDenyRequests)}
            variant="default"
          >
            Deny
          </Button>
        </>
      )}
      <Button
        disabled={isDeleteButtonDisabled()}
        onClick={() => openConfirmModal("delete", handleDeleteRequests)}
        variant="default"
      >
        Delete
      </Button>
    </>
  );
}

export default LeaveRequestButtonGroup;
