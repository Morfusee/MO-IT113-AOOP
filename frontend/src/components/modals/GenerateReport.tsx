import {
  Button,
  Collapse,
  CollapseProps,
  Combobox,
  Flex,
  Input,
  InputBase,
  MantineComponent,
  rem,
  Text,
  TextInput,
  useCombobox,
} from "@mantine/core";
import { DatePickerInput } from "@mantine/dates";
import { useForm, UseFormReturnType } from "@mantine/form";
import { ContextModalProps } from "@mantine/modals";
import { ReactElement, useEffect, useMemo } from "react";
import userController from "../../controllers/userController";
import { AsyncSelect } from "../combobox/AsyncSelect";
import { NoParamAsyncSelect } from "../combobox/NoParamAsyncSelect";
import { useSearchParams } from "react-router";
import { formatFullName } from "../../utils/formatters";

interface IReportTypeOptions {
  individual: IReportTypeOptionsEntries[];
  allEmployees: IReportTypeOptionsEntries[];
}

interface IReportTypeOptionsEntries {
  label: string;
  value: string;
}

export interface IGenerateReportForm {
  reportType: string;
  fileName: string;
  employeeFullName: string;
  employeeNumber: string;
  startDate: Date;
  endDate: Date;
}

function GenerateReport({ context, id, innerProps }: ContextModalProps<{}>) {
  // Controllers
  const { getUser } = userController();
  const [searchParams, setSearchParams] = useSearchParams("");

  const currentUser = getUser();

  useEffect(() => {
    context.updateModal({
      modalId: id,
      title: (
        <Text fw={700} size="md" lh={"h4"}>
          Generate Report
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
      reportType: value,
    });
    combobox.toggleDropdown();
  };

  const initialEmployeeFullName = useMemo(() => {
    const currentFullName = formatFullName(
      currentUser?.personalInfo.firstName,
      currentUser?.personalInfo.lastName
    );

    if (searchParams.size == 0) return currentFullName;

    return searchParams.get("fullName")?.toString() || "";
  }, [currentUser, searchParams]);

  const initialEmployeeNumber = useMemo(() => {
    const currentEmployeeNumber = currentUser?.employeeNumber.toString() || "";

    if (searchParams.size == 0) return currentEmployeeNumber;

    return searchParams.get("employeeNumber")?.toString() || "";
  }, [currentUser, searchParams]);

  // Hook for managing form
  const form = useForm({
    mode: "controlled",
    initialValues: {
      reportType: "",
      fileName: "",
      employeeFullName: initialEmployeeFullName,
      employeeNumber: initialEmployeeNumber,
      startDate: new Date(),
      endDate: new Date(),
    },
    validate: {
      reportType: (value) => {
        if (!value.trim()) {
          return "Leave type is required";
        }
      },
      fileName: (value: string) => {
        if (!value.trim()) {
          return "File name is required";
        }
      },
      employeeFullName: (value: string, values) => {
        if (!value.trim()) {
          return "Employee is required";
        }

        if (!values.employeeNumber.trim()) {
          return "Employee doesn't exist";
        }
      },
      employeeNumber: (value: string) => {
        if (!value.trim()) {
          return "Employee is required";
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

    form.reset();
    modalOnClose();
  };

  const reportTypeOptions: IReportTypeOptions = useMemo(() => {
    return {
      individual: [
        { label: "Payroll Report", value: "" },
        { label: "Annual Payroll Report", value: "" },
      ],
      allEmployees: [{ label: "Annual Payroll Summary", value: "" }],
    };
  }, []);

  return (
    <form onSubmit={form.onSubmit(() => handleSubmit())}>
      <Flex gap={"xs"} direction={"column"}>
        <TextInput
          label="File Name"
          placeholder="e.g. June_2025_Payroll"
          styles={{
            label: {
              paddingBottom: "5px",
            },
          }}
          key={form.key("fileName")}
          {...form.getInputProps("fileName")}
        />
        <Combobox
          store={combobox}
          onOptionSubmit={comboboxOnOptionSubmit}
          key={form.key("reportType")}
          {...form.getInputProps("reportType")}
        >
          <Combobox.Target>
            <InputBase
              styles={{
                label: { marginBottom: rem(5) },
              }}
              label="Report Type"
              component="button"
              type="button"
              pointer
              rightSection={<Combobox.Chevron />}
              onClick={() => combobox.toggleDropdown()}
              rightSectionPointerEvents="none"
            >
              {form.getValues().reportType || (
                <Input.Placeholder>Pick Report Type</Input.Placeholder>
              )}
            </InputBase>
          </Combobox.Target>
          <Combobox.Dropdown>
            <Combobox.Options>
              {Object.entries(reportTypeOptions).map(([group, options]) => (
                <Combobox.Group
                  key={group}
                  label={
                    group === "individual"
                      ? "Individual Employee Reports"
                      : "All Employees Reports"
                  }
                >
                  {options.map((option: IReportTypeOptionsEntries) => (
                    <Combobox.Option value={option.label} key={option.label}>
                      {option.label}
                    </Combobox.Option>
                  ))}
                </Combobox.Group>
              ))}
            </Combobox.Options>
          </Combobox.Dropdown>
        </Combobox>

        <CollapseSelectionFactory
          reportType={form.getValues().reportType}
          reportTypeOptions={reportTypeOptions}
          form={form}
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

function CollapseFlex({
  children,
  inProp,
  collapseProps,
}: {
  children: ReactElement;
  inProp: boolean;
  collapseProps?: MantineComponent<{
    props: CollapseProps;
    ref: HTMLDivElement;
  }>;
}) {
  return (
    <Collapse in={inProp} {...collapseProps}>
      <Flex gap={"xs"} direction={"column"}>
        {children}
      </Flex>
    </Collapse>
  );
}

function CollapseSelectionFactory({
  reportType,
  reportTypeOptions,
  form,
}: {
  reportType: string;
  reportTypeOptions: IReportTypeOptions;
  form: UseFormReturnType<IGenerateReportForm>;
}) {
  const isIndividualPayrollReport =
    reportType === reportTypeOptions.individual[0]?.label;
  const isAnyIndividualReport = reportTypeOptions.individual.some(
    (option) => option.label === reportType
  );

  console.log(form.getValues());

  return (
    <>
      <CollapseFlex inProp={isAnyIndividualReport}>
        <NoParamAsyncSelect label="Employee" externalForm={form} />
      </CollapseFlex>
      <CollapseFlex inProp={isIndividualPayrollReport}>
        <>
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
        </>
      </CollapseFlex>
    </>
  );
}

export default GenerateReport;
