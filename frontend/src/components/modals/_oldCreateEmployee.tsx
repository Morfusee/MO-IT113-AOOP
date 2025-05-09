import {
  Button,
  Flex,
  ScrollArea,
  Stack,
  Text,
  TextInput,
  NumberInput,
  NumberInputProps,
  TextInputProps,
  Select,
} from "@mantine/core";
import { ContextModalProps } from "@mantine/modals";
import { useForm, UseFormReturnType } from "@mantine/form";
import { Fragment, useEffect, useMemo, useRef } from "react";
import { EmployeeFormValues } from "../../types/employee";
import {
  DatePickerInput,
  DatePickerInputFactory,
  DatePickerInputProps,
} from "@mantine/dates";

function CreateEmployee({ context, id, innerProps }: ContextModalProps<{}>) {
  const form = useForm<EmployeeFormValues>({
    initialValues: {
      firstName: "",
      lastName: "",
      age: 1,
      birthdate: new Date(),
      phoneNumber: "",
      address: "",
      employeeNumber: 1,
      position: "",
      department: "",
      supervisor: "",
      employmentStatus: "",
      hourlyRate: 0,
      sss: "",
      philhealth: "",
      pagibig: "",
      tin: "",
      basicSalary: 0,
      grossSemiMonthlyRate: 0,
      riceSubsidy: 0,
      phoneAllowance: 0,
      clothingAllowance: 0,
    },
  });

  const employmentStatusOptions = useMemo(() => {
    return [
      { label: "Regular", value: "regular" },
      { label: "Probationary", value: "probationary" },
    ];
  }, []);

  useEffect(() => {
    context.updateModal({
      modalId: id,
      title: (
        <Text fw={700} size="md" lh={"h4"}>
          Create Employee
        </Text>
      ),
      size: "lg",
      scrollAreaComponent: ScrollArea.Autosize,
      styles: {
        content: {
          overflowY: "hidden",
        },
      },
      centered: true,
    });
  }, [id]);

  const handleSubmit = form.onSubmit((values) => {
    console.log(values);
    // Handle form submission here
    context.closeModal(id);
  });

  return (
    <form onSubmit={handleSubmit}>
      <Stack w={"100%"} gap={20}>
        {/* Personal Information */}
        <Text fw={600} size="lg">
          Personal Information
        </Text>
        <Flex w={"100%"} gap={10} wrap="wrap">
          <TextInput
            label="First Name"
            placeholder="John"
            className="flex-[1_1_49%]"
            required
            {...form.getInputProps("firstName")}
          />
          <TextInput
            label="Last Name"
            placeholder="Doe"
            className="flex-[1_1_49%]"
            required
            {...form.getInputProps("lastName")}
          />
          <NumberInput
            label="Age"
            placeholder="12"
            className="flex-[1_1_49%]"
            required
            {...form.getInputProps("age")}
          />
          <DatePickerInput
            label="Birthdate"
            placeholder="January 1, 2000"
            className="flex-[1_1_49%]"
            required
            {...form.getInputProps("birthdate")}
          />
          <TextInput
            label="Phone Number"
            placeholder="+639123456789"
            className="flex-[1_1_49%]"
            required
            {...form.getInputProps("phoneNumber")}
          />
          <TextInput
            label="Address"
            placeholder="1234 Main St., City, Country"
            w={"100%"}
            required
            {...form.getInputProps("address")}
          />
        </Flex>

        {/* Employment Details */}
        <Text fw={600} size="lg">
          Employment Details
        </Text>
        <Flex w={"100%"} gap={10} wrap="wrap">
          <TextInput
            label="Employee Number"
            placeholder="10001"
            className="flex-[1_1_49%]"
            required
            disabled
            {...form.getInputProps("employeeNumber")}
          />
          <TextInput
            label="Position"
            placeholder="Software Engineer"
            className="flex-[1_1_49%]"
            required
            {...form.getInputProps("position")}
          />
          <TextInput
            label="Department"
            placeholder="Engineering"
            className="flex-[1_1_49%]"
            required
            {...form.getInputProps("department")}
          />
          <TextInput
            label="Supervisor"
            placeholder="Jane Doe"
            className="flex-[1_1_49%]"
            required
            {...form.getInputProps("supervisor")}
          />
          <Select
            label="Employment Status"
            data={employmentStatusOptions}
            placeholder="Regular"
            className="flex-[1_1_49%]"
            required
            {...form.getInputProps("employmentStatus")}
          />
          <NumberInput
            label="Hourly Rate"
            placeholder="100.00"
            className="flex-[1_1_49%]"
            required
            {...form.getInputProps("hourlyRate")}
          />
        </Flex>

        {/* Government IDs */}
        <Text fw={600} size="lg">
          Government IDs
        </Text>
        <Flex w={"100%"} gap={10} wrap="wrap">
          <TextInput
            label="SSS"
            className="flex-[1_1_49%]"
            required
            {...form.getInputProps("sss")}
          />
          <TextInput
            label="PhilHealth"
            className="flex-[1_1_49%]"
            required
            {...form.getInputProps("philhealth")}
          />
          <TextInput
            label="Pag-IBIG"
            className="flex-[1_1_49%]"
            required
            {...form.getInputProps("pagibig")}
          />
          <TextInput
            label="TIN"
            className="flex-[1_1_49%]"
            required
            {...form.getInputProps("tin")}
          />
        </Flex>

        {/* Compensation */}
        <Text fw={600} size="lg">
          Salary & Allowances
        </Text>
        <Flex w={"100%"} gap={10} wrap="wrap">
          <NumberInput
            label="Basic Salary"
            className="flex-[1_1_49%]"
            required
            {...form.getInputProps("basicSalary")}
          />
          <NumberInput
            label="Gross Semi-Monthly Rate"
            className="flex-[1_1_49%]"
            required
            {...form.getInputProps("grossSemiMonthlyRate")}
          />
          <NumberInput
            label="Rice Subsidy"
            className="flex-[1_1_49%]"
            required
            {...form.getInputProps("riceSubsidy")}
          />
          <NumberInput
            label="Phone Allowance"
            className="flex-[1_1_49%]"
            required
            {...form.getInputProps("phoneAllowance")}
          />
          <NumberInput
            label="Clothing Allowance"
            className="flex-[1_1_49%]"
            required
            {...form.getInputProps("clothingAllowance")}
          />
        </Flex>

        <Flex gap="md" justify="flex-end" mt="md">
          <Button variant="subtle" onClick={() => context.closeModal(id)}>
            Cancel
          </Button>
          <Button type="submit">Create Employee</Button>
        </Flex>
      </Stack>
    </form>
  );
}
