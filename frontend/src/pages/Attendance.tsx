import {
  Container,
  Flex,
  Grid,
  Paper,
  rem,
  ScrollArea,
  Table,
  Text,
  ThemeIcon,
} from "@mantine/core";
import { DatesProvider, MonthPickerInput } from "@mantine/dates";
import { IconCaretDownFilled } from "@tabler/icons-react";
import { useEffect, useMemo, useState } from "react";
import { useSearchParams } from "react-router";
import { AsyncSelect } from "../components/combobox/AsyncSelect";
import { attendanceAnalytics as attendanceAnalyticsFallback } from "../constants/attendanceAnalytics";
import attendanceController from "../controllers/attendanceController";
import userController from "../controllers/userController";
import {
  AttendanceData,
  AttendanceStatsFrontend,
  AttendanceStatsObj,
} from "../types/attendance";
import { formatAttendanceAnalytics } from "../utils/formatters";
import { isAdmin } from "../utils/permissionUtils";

function Attendance() {
  const [selectedDate, setSelectedDate] = useState<Date>(
    // Fixes the month picker input bug delayed by one month
    new Date(Date.UTC(2022, 8))
  );

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
      <Flex
        mah={"100%"}
        w={"100%"}
        gap={rem(10)}
        direction={"column"}
        // maw={rem(1000)}
      >
        <Flex gap={rem(10)} align={"center"} w={"100%"}>
          <DatesProvider
            settings={{
              locale: "en",
              timezone: "UTC",
            }}
          >
            <MonthPickerInput
              value={
                // Add one month to the current date
                // new Date(selectedDate.getFullYear(), selectedDate.getMonth() + 1)
                selectedDate
              }
              placeholder={"Select month"}
              rightSection={<IconCaretDownFilled />}
              rightSectionPointerEvents="none"
              w={rem(180)}
              onChange={(date) => setSelectedDate(date as Date)}
            />
          </DatesProvider>
          <AsyncSelect />
        </Flex>
        <AttendanceStatistics selectedDate={selectedDate} />
        <AttendanceTable selectedDate={selectedDate} />
      </Flex>
    </Container>
  );
}

function AttendanceStatistics({ selectedDate }: { selectedDate: Date }) {
  // States
  const [attendanceStats, setAttendanceStats] =
    useState<AttendanceStatsFrontend>(attendanceAnalyticsFallback);
  const [searchParams, setSearchParams] = useSearchParams("");

  // Controllers
  const { getAttendanceAnalytics } = attendanceController();
  const { getUser } = userController();

  // Fetch attendance analytics
  useEffect(() => {
    const user = getUser();
    let employeeToFetch = user?.employeeNumber;

    // If the user is an admin, fetch the employee number from the search params
    if (
      isAdmin(user?.employmentInfo.position) &&
      searchParams.get("employeeNumber")
    ) {
      employeeToFetch = parseInt(searchParams.get("employeeNumber")!);
    }

    // Fetch the attendance analytics
    getAttendanceAnalytics(employeeToFetch, selectedDate).then((analytics) => {
      // If no analytics, use the fallback
      setAttendanceStats(
        analytics
          ? formatAttendanceAnalytics(analytics)
          : attendanceAnalyticsFallback
      );
    });
  }, [selectedDate, searchParams]); // Dependency array ensures this runs when selectedDate or searchParams change

  return (
    <Paper p={rem(20)} maw={rem(700)}>
      <Grid gutter={rem(25)}>
        {Object.values(attendanceStats).map(
          (attendance: AttendanceStatsObj, index) => (
            <Grid.Col span={{ base: 12, sm: 6, md: 4 }} key={index}>
              <Flex gap={rem(10)}>
                <ThemeIcon radius="xl" size="lg">
                  <attendance.icon size={20} />
                </ThemeIcon>
                <Flex direction={"column"} gap={rem(5)}>
                  <Text size={rem(30)} fw={700}>
                    {attendance.value}
                  </Text>
                  <Text size="sm">{attendance.title}</Text>
                </Flex>
              </Flex>
            </Grid.Col>
          )
        )}
      </Grid>
    </Paper>
  );
}

function AttendanceTable({ selectedDate }: { selectedDate: Date }) {
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
            <Table.Th>EN</Table.Th>
            <Table.Th>Date</Table.Th>
            <Table.Th>Time In</Table.Th>
            <Table.Th>Time Out</Table.Th>
            <Table.Th>Status</Table.Th>
            <Table.Th>Overtime</Table.Th>
            <Table.Th>Total Hours</Table.Th>
          </Table.Tr>
        </Table.Thead>
        <Table.Tbody className="relative">
          <AttendanceTableRows selectedDate={selectedDate} />
        </Table.Tbody>
      </Table>
    </ScrollArea>
  );
}

function AttendanceTableRows({ selectedDate }: { selectedDate: Date }) {
  // States
  const [attendances, setAttendances] = useState<AttendanceData[]>([]);
  const [searchParams, setSearchParams] = useSearchParams("");

  // Controllers
  const { getAttendance } = attendanceController();
  const { getUser } = userController();

  // Fetch attendance
  const memoizedAttendance = useMemo(() => {
    const user = getUser();
    let employeeToFetch = user?.employeeNumber;

    // If the user is an admin, fetch the employee number from the search params
    if (
      isAdmin(user?.employmentInfo.position) &&
      searchParams.get("employeeNumber")
    ) {
      employeeToFetch = parseInt(searchParams.get("employeeNumber")!);
    }

    getAttendance(employeeToFetch, selectedDate).then((attendances) => {
      // If no attendance, return an empty array
      if (!attendances) return [];

      // Set the attendance to the state
      setAttendances(attendances.data);
    });
  }, [selectedDate, searchParams]);

  if (attendances.length === 0) {
    return (
      <Table.Tr>
        <Table.Td colSpan={7} align="center">
          <Flex w={"100%"} align={"center"} justify={"center"} py={rem(10)}>
            <Text fw={600}>No attendance records found.</Text>
          </Flex>
        </Table.Td>
      </Table.Tr>
    );
  }

  return (
    <>
      {attendances.map((attendance, index) => (
        <Table.Tr key={index}>
          <Table.Td>{attendance.employeeNumber}</Table.Td>
          <Table.Td>{attendance.date}</Table.Td>
          <Table.Td>{attendance.timeIn}</Table.Td>
          <Table.Td>{attendance.timeOut}</Table.Td>
          <Table.Td>{attendance.status}</Table.Td>
          <Table.Td>{attendance.overtimeHours}</Table.Td>
          <Table.Td>{attendance.totalHours}</Table.Td>
        </Table.Tr>
      ))}
    </>
  );
}

export default Attendance;
