import {
  Container,
  rem,
  Flex,
  SegmentedControl,
  Stack,
  Button,
  Paper,
  ScrollArea,
  Table,
  Text,
  Checkbox,
  Transition,
} from "@mantine/core";
import { modals } from "@mantine/modals";
import { IconPlus } from "@tabler/icons-react";
import leaveRequestController from "../controllers/leaveRequestController";
import userController from "../controllers/userController";
import { useMemo, useState } from "react";
import { isAdmin } from "../utils/permissionUtils";
import { LeaveRequestsData } from "../types/leaveRequests";
import { stringToDate } from "../utils/dateUtils";
import { useSearchParams } from "react-router";
import { scope, status } from "../constants/segmentedControls";
import { notifications } from "@mantine/notifications";
import { leaveRequestNotification } from "../constants/notificationData";
import { openConfirmModal } from "../constants/leaveRequestConfirmationModals";
import LeaveRequestButtonGroup from "../components/buttons/LeaveRequestButtonGroup";

function LeaveRequest() {
  // States
  const [searchParams, setSearchParams] = useSearchParams();
  const [selectedRows, setSelectedRows] = useState<LeaveRequestsData[]>([]);
  const [hasCreatedLeaveRequest, setHasCreatedLeaveRequest] =
    useState<boolean>(false);

  // Controllers
  const { editLeaveRequests, deleteLeaveRequests, createLeaveRequest } =
    leaveRequestController();
  const { getUser } = userController();

  const handleStatusSegmentedControl = (value: string) => {
    setSearchParams((params) => {
      params.set("status", value);
      return params;
    });
  };

  const handleScopeSegmentedControl = (value: string) => {
    setSearchParams((params) => {
      if (value == "Created by me") {
        params.delete("scope");
        return params;
      }

      // If the value is "All", set the scope to "All"
      params.set("scope", value);
      return params;
    });
  };

  const handleCreateLeaveRequest = async (
    leaveRequest: Omit<LeaveRequestsData, "id" | "employeeName" | "notes">
  ) => {
    setHasCreatedLeaveRequest(true);
    try {
      await createLeaveRequest(leaveRequest);

      // Show notification
      notifications.show({
        withBorder: true,
        autoClose: 5000,
        title: leaveRequestNotification.created.success.title,
        message: leaveRequestNotification.created.success.message,
      });
    } catch (error) {
      console.error(error);

      // Show notification
      notifications.show({
        withBorder: true,
        autoClose: 5000,
        title: leaveRequestNotification.created.error.title,
        message: leaveRequestNotification.created.error.message,
      });
    }

    // Triggering the reload of the leave requests
    setHasCreatedLeaveRequest(false);
  };

  const handleUnselectItems = () => {
    setSelectedRows([]);
  };

  return (
    <Container
      w={"100%"}
      fluid
      px={rem(80)}
      py={rem(50)}
      style={{
        overflowY: "auto",
      }}
    >
      <Stack mah={"100%"} w={"100%"} gap={rem(10)}>
        <Flex
          align={"center"}
          gap={rem(10)}
          wrap={"wrap"}
          justify={"space-between"}
        >
          <Flex gap={rem(10)} wrap={"wrap"}>
            <SegmentedControl
              data={status}
              w={"fit-content"}
              styles={{
                label: { paddingTop: rem(3), paddingBottom: rem(3) },
              }}
              defaultValue={searchParams.get("status") || status[0]}
              onChange={(value) => handleStatusSegmentedControl(value)}
            />
            <SegmentedControl
              data={scope}
              w={"fit-content"}
              styles={{
                label: { paddingTop: rem(3), paddingBottom: rem(3) },
              }}
              defaultValue={searchParams.get("scope") || scope[0]}
              onChange={(value) => handleScopeSegmentedControl(value)}
            />
          </Flex>
        </Flex>
        <Flex
          align={"center"}
          gap={"xs"}
          wrap={"wrap"}
          justify={"space-between"}
        >
          <Flex gap={"xs"} align={"center"}>
            {/* Admin buttons */}
            <LeaveRequestButtonGroup
              selectedRows={selectedRows}
              setSelectedRows={setSelectedRows}
            />
            <Transition
              mounted={selectedRows.length !== 0}
              transition="fade"
              duration={200}
              timingFunction="ease"
            >
              {(styles) => (
                <Button
                  onClick={handleUnselectItems}
                  variant="default"
                  style={styles}
                >
                  Unselect {selectedRows.length} items
                </Button>
              )}
            </Transition>
          </Flex>
          <Button
            onClick={() =>
              modals.openContextModal({
                modal: "createLeaveRequest",
                innerProps: {
                  handleCreateLeaveRequest,
                },
              })
            }
            leftSection={<IconPlus size={15} />}
            variant="default"
          >
            Create
          </Button>
        </Flex>
        <LeaveRequestTable
          hasCreatedLeaveRequest={hasCreatedLeaveRequest}
          selectedRows={selectedRows}
          setSelectedRows={setSelectedRows}
        />
      </Stack>
    </Container>
  );
}

function LeaveRequestTable({
  hasCreatedLeaveRequest,
  selectedRows,
  setSelectedRows,
}: {
  hasCreatedLeaveRequest: boolean;
  selectedRows: LeaveRequestsData[];
  setSelectedRows: React.Dispatch<React.SetStateAction<LeaveRequestsData[]>>;
}) {
  return (
    <ScrollArea
      offsetScrollbars
      h={"100dvh"} // Expands this component to the remaining space lol idk why.
      component={Paper}
      scrollbars="xy"
      type="auto"
    >
      <Table highlightOnHover stickyHeader>
        <Table.Thead>
          <Table.Tr>
            <Table.Th></Table.Th>
            <Table.Th>EN</Table.Th>
            <Table.Th>Name</Table.Th>
            <Table.Th>Leave Type</Table.Th>
            <Table.Th>Start Date</Table.Th>
            <Table.Th>End Date</Table.Th>
            <Table.Th>Status</Table.Th>
          </Table.Tr>
        </Table.Thead>
        <Table.Tbody className="relative">
          <LeaveRequestTableRows
            hasCreatedLeaveRequest={hasCreatedLeaveRequest}
            selectedRows={selectedRows}
            setSelectedRows={setSelectedRows}
          />
        </Table.Tbody>
      </Table>
    </ScrollArea>
  );
}

function LeaveRequestTableRows({
  hasCreatedLeaveRequest,
  selectedRows,
  setSelectedRows,
}: {
  hasCreatedLeaveRequest: boolean;
  selectedRows: LeaveRequestsData[];
  setSelectedRows: React.Dispatch<React.SetStateAction<LeaveRequestsData[]>>;
}) {
  // States
  const [leaveRequests, setLeaveRequests] = useState<LeaveRequestsData[]>([]);
  const [searchParams, setSearchParams] = useSearchParams();

  // Controllers
  const { getLeaveRequests } = leaveRequestController();
  const { getUser } = userController();

  const handleFetchLeaveRequests = () => {
    const user = getUser();
    let employeeToFetch = user?.employeeNumber;
    const status = searchParams.get("status");
    const scope = searchParams.get("scope");

    if (!status) return;

    if (scope === "All") {
      employeeToFetch = undefined;
    }

    getLeaveRequests(employeeToFetch ?? null, status).then((leaves) => {
      // If no leave requests, return an empty array
      if (!leaves) return [];

      // Set the leave request to the state
      setLeaveRequests(leaves.data);
    });
  };

  // Fetch leave requests
  const memoizedLeaveRequests = useMemo(() => {
    handleFetchLeaveRequests();
  }, [searchParams, hasCreatedLeaveRequest]);

  // Fetch leave requests if there are no selected rows
  const memoizedRerenderOnAction = useMemo(() => {
    if (selectedRows.length !== 0) return;

    handleFetchLeaveRequests();
  }, [selectedRows]);

  const handleCheckboxChange = (leaveRequest: LeaveRequestsData) => {
    setSelectedRows((prev) => {
      const isSelected = prev.some(
        (prevLeave) => prevLeave.id == leaveRequest.id
      );

      return isSelected
        ? prev.filter((prevLeave) => prevLeave.id != leaveRequest.id)
        : [...prev, leaveRequest];
    });
  };

  const isCheckboxChecked = (leaveRequest: LeaveRequestsData) => {
    return selectedRows.some(
      (selectedRow) => selectedRow.id == leaveRequest.id
    );
  };

  const isCheckboxDisabled = (leaveRequest: LeaveRequestsData) => {
    if (isAdmin(getUser()?.employmentInfo.position)) return false;

    // If the leave request is not created by the user and is not an HR Manager,
    // disable the checkbox
    return leaveRequest.employeeNum !== getUser()?.employeeNumber;
  };

  if (leaveRequests.length === 0) {
    return (
      <Table.Tr>
        <Table.Td colSpan={7} align="center">
          <Flex w={"100%"} align={"center"} justify={"center"} py={rem(10)}>
            <Text fw={600}>No leave request records found.</Text>
          </Flex>
        </Table.Td>
      </Table.Tr>
    );
  }

  return (
    <>
      {leaveRequests.map((leaveRequest, index) => (
        <Table.Tr key={index}>
          <Table.Td>
            <Checkbox
              disabled={isCheckboxDisabled(leaveRequest)}
              checked={isCheckboxChecked(leaveRequest)}
              onChange={() => handleCheckboxChange(leaveRequest)}
            />
          </Table.Td>
          <Table.Td>{leaveRequest.employeeNum}</Table.Td>
          <Table.Td>{leaveRequest.employeeName}</Table.Td>
          <Table.Td>{leaveRequest.leaveType}</Table.Td>
          <Table.Td>{stringToDate(leaveRequest.startDate)}</Table.Td>
          <Table.Td>{stringToDate(leaveRequest.endDate)}</Table.Td>
          <Table.Td>{leaveRequest.status}</Table.Td>
        </Table.Tr>
      ))}
    </>
  );
}

export default LeaveRequest;
