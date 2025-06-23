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
import {
  DatesProvider,
  MonthPickerInput,
  YearPickerInput,
} from "@mantine/dates";
import { useForm, UseFormReturnType } from "@mantine/form";
import { ContextModalProps } from "@mantine/modals";
import { ReactElement, useEffect, useMemo } from "react";
import { useSearchParams } from "react-router";
import userController from "../../controllers/userController";
import { formatFullName } from "../../utils/formatters";
import { NoParamAsyncSelect } from "../combobox/NoParamAsyncSelect";
import reportController from "../../controllers/reportController";
import { dateToString, getLastDayOfMonth } from "../../utils/dateUtils";
import { isAdmin } from "../../utils/permissionUtils";

interface IReportTypeOptions {
  individual: IReportTypeOptionsEntries[];
  allEmployees: IReportTypeOptionsEntries[];
}

interface IReportTypeOptionsEntries {
  label: string;
  value: string;
  isAdminOnly: boolean;
}

export interface IGenerateReportForm {
  reportType: string;
  reportTitle: string;
  employeeFullName: string;
  employeeNumber: string;
  month: Date;
  year: Date;
}

function GenerateReport({ context, id, innerProps }: ContextModalProps<{}>) {
  // Controllers
  const { getUser } = userController();
  const {
    getEmployeePayrollReport,
    getAnnualEmployeePayrollReport,
    getAllEmployeesPayrollSummaryReport,
  } = reportController();
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
      reportTitle: "",
      employeeFullName: initialEmployeeFullName,
      employeeNumber: initialEmployeeNumber,
      month: new Date(Date.UTC(2022, 8)),
      year: new Date(Date.UTC(2022)),
    },
    validate: {
      reportType: (value) => {
        if (!value.trim()) {
          return "Leave type is required";
        }
      },
      reportTitle: (value: string) => {
        if (!value.trim()) {
          return "Report title is required";
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
      month: (value: Date) => {
        if (!(value instanceof Date) || isNaN(value.getTime())) {
          return "Month is required";
        }
      },
      year: (value: Date) => {
        if (!(value instanceof Date) || isNaN(value.getTime())) {
          return "Year is required";
        }
      },
    },
  });

  const modalOnClose = () => {
    context.closeModal(id);
  };

  const getSelectedReportTypeValue = () => {
    const allOptions = [
      ...reportTypeOptions.individual,
      ...reportTypeOptions.allEmployees,
    ];
    const selected = allOptions.find(
      (option) => option.label === form.getValues().reportType
    );
    return selected?.value;
  };

  const handleSubmit = async () => {
    if (!form.isValid) return;

    const reportType = getSelectedReportTypeValue();
    const formValues = form.getValues();

    form.reset();
    modalOnClose();

    switch (reportType) {
      case "/payroll":
        const startDate = dateToString(formValues.month);
        const endDate = getLastDayOfMonth(dateToString(formValues.month));

        return getEmployeePayrollReport(
          formValues.reportTitle,
          formValues.employeeNumber,
          startDate,
          endDate
        );

      case "/payroll/annual":
        return getAnnualEmployeePayrollReport(
          formValues.reportTitle,
          formValues.employeeNumber,
          formValues.year.getFullYear().toString()
        );

      case "/payroll/annual/summary":
        return getAllEmployeesPayrollSummaryReport(
          formValues.reportTitle,
          formValues.year.getFullYear().toString()
        );
      default:
        break;
    }
  };

  const reportTypeOptions: IReportTypeOptions = useMemo(() => {
    return {
      individual: [
        { label: "Payroll Report", value: "/payroll", isAdminOnly: false },
        {
          label: "Annual Payroll Report",
          value: "/payroll/annual",
          isAdminOnly: false,
        },
      ],
      allEmployees: [
        {
          label: "Annual Payroll Summary",
          value: "/payroll/annual/summary",
          isAdminOnly: true,
        },
      ],
    };
  }, []);

  const memoizedIsAdmin = useMemo(() => {
    return isAdmin(getUser()?.employmentInfo.position);
  }, [getUser]);

  return (
    <form onSubmit={form.onSubmit(() => handleSubmit())}>
      <Flex gap={"xs"} direction={"column"}>
        <TextInput
          label="Report Title"
          placeholder="e.g. Crisostomo Payroll Report"
          styles={{
            label: {
              paddingBottom: "5px",
            },
          }}
          key={form.key("reportTitle")}
          {...form.getInputProps("reportTitle")}
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
                    <Combobox.Option
                      disabled={!memoizedIsAdmin && option.isAdminOnly}
                      value={option.label}
                      key={option.label}
                    >
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
  const isAnnualIndividualReport =
    reportType === reportTypeOptions.individual[1].label;

  return (
    <DatesProvider
      settings={{
        locale: "en",
        timezone: "UTC",
      }}
    >
      <CollapseFlex inProp={isAnyIndividualReport}>
        <NoParamAsyncSelect label="Employee" externalForm={form} />
      </CollapseFlex>
      <CollapseFlex inProp={isAnnualIndividualReport}>
        <YearPickerInput
          styles={{
            label: { marginBottom: rem(5) },
          }}
          label="Year"
          placeholder="Pick year"
          key={form.key("year")}
          {...form.getInputProps("year")}
          minDate={new Date(Date.UTC(2022))}
          maxDate={new Date(Date.UTC(2022))}
        />
      </CollapseFlex>
      <CollapseFlex inProp={isIndividualPayrollReport}>
        <MonthPickerInput
          styles={{
            label: { marginBottom: rem(5) },
          }}
          label="Month"
          placeholder="Pick month"
          key={form.key("month")}
          {...form.getInputProps("month")}
          minDate={new Date(Date.UTC(2022, 8))}
          maxDate={new Date(Date.UTC(2022, 11))}
        />
      </CollapseFlex>
    </DatesProvider>
  );
}

export default GenerateReport;
