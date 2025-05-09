import {
  Button,
  Combobox,
  Flex,
  Input,
  InputBase,
  rem,
  Text,
  useCombobox,
} from "@mantine/core";
import { DatePickerInput } from "@mantine/dates";
import { useForm } from "@mantine/form";
import { ContextModalProps } from "@mantine/modals";
import { useEffect, useMemo } from "react";
import userController from "../../controllers/userController";

function CreateLeaveRequest({
  context,
  id,
  innerProps,
}: ContextModalProps<{
  handleCreateLeaveRequest: (leaveRequest: {
    employeeNum: number;
    startDate: string;
    endDate: string;
    leaveType: string;
    status: string;
  }) => void;
}>) {
  // Set up the props
  const { handleCreateLeaveRequest } = innerProps;

  // Controllers
  const { getUser } = userController();

  useEffect(() => {
    context.updateModal({
      modalId: id,
      title: (
        <Text fw={700} size="md" lh={"h4"}>
          Create Leave Request
        </Text>
      ),
      centered: true,
    });
  }, [id]);

  // Combobox event handlers
  const combobox = useCombobox({
    onDropdownClose: () => combobox.resetSelectedOption(),
    onDropdownOpen: () => combobox.resetSelectedOption(),
  });

  const comboboxOnOptionSubmit = (value: string) => {
    form.setValues({
      leaveType: value,
    });
    combobox.toggleDropdown();
  };

  // Hook for managing form
  const form = useForm({
    mode: "controlled",
    initialValues: {
      leaveType: "",
      startDate: new Date(),
      endDate: new Date(),
    },
    validate: {
      leaveType: (value) => {
        if (!value.trim()) {
          return "Leave type is required";
        }
      },
      startDate: (value: Date, values) => {
        if (!value) return "Start date is required";

        // Normalize the dates to compare them
        const start = new Date(value).setHours(0, 0, 0, 0);
        const end = new Date(values.endDate).setHours(0, 0, 0, 0);

        if (start > end) {
          return "Start date must be before end date";
        }
      },
      endDate: (value: Date, values) => {
        if (!value) return "End date is required";

        // Normalize the dates to compare them
        const start = new Date(values.startDate).setHours(0, 0, 0, 0);
        const end = new Date(value).setHours(0, 0, 0, 0);

        if (start > end) {
          return "End date must be after start date";
        }
      },
    },
  });

  const modalOnClose = () => {
    context.closeModal(id);
  };

  const handleSubmit = async () => {
    if (!form.isValid) return;

    handleCreateLeaveRequest({
      employeeNum: getUser()?.employeeNumber!,
      startDate: form.values.startDate.toISOString(),
      endDate: form.values.endDate.toISOString(),
      leaveType: form.values.leaveType,
      status: "Pending",
    });

    form.reset();
    modalOnClose();
  };

  const sickLeaveOptions = useMemo(
    () => [
      "Sick Leave",
      "Vacation Leave",
      "Maternity Leave",
      "Paternity Leave",
      "Bereavement Leave",
    ],
    []
  );

  return (
    <form onSubmit={form.onSubmit(() => handleSubmit())}>
      <Flex gap={"xs"} direction={"column"}>
        <Combobox
          store={combobox}
          onOptionSubmit={comboboxOnOptionSubmit}
          key={form.key("leaveType")}
          {...form.getInputProps("leaveType")}
        >
          <Combobox.Target>
            <InputBase
              styles={{
                label: { marginBottom: rem(5) },
              }}
              label="Leave Type"
              component="button"
              type="button"
              pointer
              rightSection={<Combobox.Chevron />}
              onClick={() => combobox.toggleDropdown()}
              rightSectionPointerEvents="none"
            >
              {form.getValues().leaveType || (
                <Input.Placeholder>Pick Leave Type</Input.Placeholder>
              )}
            </InputBase>
          </Combobox.Target>
          <Combobox.Dropdown>
            <Combobox.Options>
              {sickLeaveOptions.map((option, index) => (
                <Combobox.Option value={option} key={index}>
                  {option}
                </Combobox.Option>
              ))}
            </Combobox.Options>
          </Combobox.Dropdown>
        </Combobox>
        <DatePickerInput
          styles={{
            label: { marginBottom: rem(5) },
          }}
          label="Start Date"
          placeholder="Pick date"
          key={form.key("startDate")}
          {...form.getInputProps("startDate")}
        />
        <DatePickerInput
          styles={{
            label: { marginBottom: rem(5) },
          }}
          label="End Date"
          placeholder="Pick date"
          key={form.key("endDate")}
          {...form.getInputProps("endDate")}
        />
        <Flex direction={"row"} gap={"xs"} justify={"flex-end"} pt={"md"}>
          <Button variant="subtle" onClick={() => context.closeAll()}>
            Cancel
          </Button>
          <Button variant="filled" type="submit">
            Confirm
          </Button>
        </Flex>
      </Flex>
    </form>
  );
}

export default CreateLeaveRequest;
