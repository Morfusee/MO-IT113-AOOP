import {
  ActionIcon,
  Button,
  Container,
  Flex,
  Input,
  Paper,
  rem,
  ScrollArea,
  Stack,
  Table,
  Text,
} from "@mantine/core";
import { useDebouncedCallback } from "@mantine/hooks";
import { modals } from "@mantine/modals";
import { IconDotsVertical, IconPlus, IconSearch } from "@tabler/icons-react";
import { useMemo, useState } from "react";
import { useSearchParams } from "react-router";
import employeeController from "../controllers/employeeController";
import { APIResponse, FetchedUser } from "../types/responses";
import { formatFullName } from "../utils/formatters";
import EmployeeMenu from "../components/menus/EmployeeMenu";

function Employees() {
  const [searchParams, setSearchParams] = useSearchParams();

  const handleSearch = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearchParams((params) => {
      if (e.target.value === "") {
        params.delete("search");
        return params;
      }

      params.set("search", e.target.value);
      return params;
    });
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
        <Flex justify={"space-between"}>
          <Input
            placeholder="Search"
            leftSection={<IconSearch size={16} />}
            miw={rem(250)}
            onChange={handleSearch}
          />
          <Button
            onClick={() =>
              modals.openContextModal({
                modal: "createOrEditEmployee",
                innerProps: {},
              })
            }
            leftSection={<IconPlus size={15} />}
            variant="default"
          >
            Create
          </Button>
        </Flex>
        <EmployeeTable />
      </Stack>
    </Container>
  );
}

function EmployeeTable() {
  return (
    <ScrollArea
      offsetScrollbars
      h={"100dvh"} // Expands this component to the remaining space lol idk why.
      component={Paper}
      scrollbars="xy"
      type="auto"
    >
      <Table
        highlightOnHover
        stickyHeader
        styles={{
          tr: {
            "--table-highlight-on-hover-color": "var(--mantine-color-dark-6)",
          },
        }}
      >
        <Table.Thead>
          <Table.Tr>
            <Table.Th>EN</Table.Th>
            <Table.Th>Name</Table.Th>
            <Table.Th>Position</Table.Th>
            <Table.Th>Immediate Supervisor</Table.Th>
            <Table.Th>Status</Table.Th>
            <Table.Th></Table.Th>
          </Table.Tr>
        </Table.Thead>
        <Table.Tbody className="relative">
          <EmployeeTableRows />
        </Table.Tbody>
      </Table>
    </ScrollArea>
  );
}

function EmployeeTableRows() {
  const [employees, setEmployees] = useState<FetchedUser[]>([]);
  const [searchParams, setSearchParams] = useSearchParams();

  const { getAllEmployees } = employeeController();

  const debouncedEmployeeFiltering = useDebouncedCallback(
    (employees: APIResponse<FetchedUser[]>) => {
      const search = searchParams.get("search")?.toLowerCase();
      const filteredEmployees = employees.data.filter((employee) => {
        const fullName = formatFullName(
          employee.employee.personalInfo.firstName,
          employee.employee.personalInfo.lastName
        );
        const employeeNumber = employee.employee.employeeNumber.toString();

        return (
          fullName.toLowerCase().includes(search as string) ||
          employeeNumber.includes(search as string)
        );
      });

      setEmployees(filteredEmployees);
      return;
    },
    250
  );

  const memoizedEmployees = useMemo(() => {
    getAllEmployees().then((response) => {
      // If there is a search query, filter the employees based on the search query.
      if (searchParams.has("search")) {
        return debouncedEmployeeFiltering(response);
      }

      // If there is no search query, just set the employees to the response data.
      setEmployees(response.data);
    });
  }, [searchParams]);

  if (employees.length === 0) {
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
      {employees.map((data, index) => (
        <Table.Tr
          key={index}
          style={{
            cursor: "pointer",
          }}
          onClick={() =>
            modals.openContextModal({
              modal: "viewEmployee",
              innerProps: {
                employee: data,
              },
            })
          }
        >
          <Table.Td>{data.employee.employeeNumber}</Table.Td>
          <Table.Td>
            {formatFullName(
              data.employee.personalInfo.firstName,
              data.employee.personalInfo.lastName
            )}
          </Table.Td>
          <Table.Td>{data.employee.employmentInfo.position}</Table.Td>
          <Table.Td>
            {data.employee.employmentInfo.immediateSupervisor}
          </Table.Td>
          <Table.Td>{data.employee.employmentInfo.status}</Table.Td>
          <Table.Td>
            <EmployeeMenu
              employee={data.employee}
              employeeNum={data.employee.employeeNumber}
            >
              <ActionIcon
                radius={"xl"}
                variant="subtle"
                onClick={(e) => {
                  e.stopPropagation();
                }}
              >
                <IconDotsVertical size={20} />
              </ActionIcon>
            </EmployeeMenu>
          </Table.Td>
        </Table.Tr>
      ))}
    </>
  );
}

export default Employees;
