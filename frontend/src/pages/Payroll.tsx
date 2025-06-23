import {
  ActionIcon,
  Button,
  Container,
  Divider,
  Flex,
  NumberFormatter,
  Paper,
  rem,
  Stack,
  Text,
  Transition,
  useMantineTheme,
} from "@mantine/core";
import { DatesProvider, YearPickerInput } from "@mantine/dates";
import {
  IconArrowLeft,
  IconArrowRight,
  IconCaretDownFilled,
  IconDownload,
  IconPlus,
} from "@tabler/icons-react";
import { useEffect, useMemo, useState } from "react";
import { useSearchParams } from "react-router";
import { AsyncSelect } from "../components/combobox/AsyncSelect";
import {
  allowancesDetailsFallback,
  deductionsDetailsFallback,
  grossSalaryDetailsFallback,
  netSalaryDetailsFallback,
  salaryAfterTaxDetailsFallback,
  taxableSalaryDetailsFallback,
} from "../constants/payrollSections";
import payrollController from "../controllers/payrollController";
import userController from "../controllers/userController";
import {
  PayrollAllowancesDetails,
  PayrollDeductionsDetails,
  PayrollGrossSalaryDetails,
  PayrollNetSalaryDetails,
  PayrollSalaryAfterTaxDetails,
  PayrollTaxableSalaryDetails,
} from "../types/payroll";
import { FetchedPayrollMonths } from "../types/responses";
import { getLastDayOfMonth, stringToMonthString } from "../utils/dateUtils";
import {
  formatAllowances,
  formatDeductions,
  formatGrossSalary,
  formatNetSalary,
  formatSalaryAfterTax,
  formatTaxableSalary,
} from "../utils/formatters";
import { isAdmin } from "../utils/permissionUtils";
import { modals } from "@mantine/modals";
import reportController from "../controllers/reportController";

function Payroll() {
  const [selectedDate, setSelectedDate] = useState<Date>(new Date(2022, 1));
  const [payrollMonths, setPayrollMonths] = useState<FetchedPayrollMonths>([]);

  const { getPayrollMonths } = payrollController();

  useEffect(() => {
    getPayrollMonths(selectedDate).then((response) => {
      setPayrollMonths(response.data);
    });
  }, [selectedDate]);

  return (
    <Container
      w={"100%"}
      fluid
      px={rem(80)}
      py={rem(50)}
      style={{
        overflowY: "auto",
      }}
      className="relative"
    >
      <Flex mah={"100%"} w={"100%"} gap={rem(10)} direction={"column"}>
        <Flex gap={10} wrap={"wrap"}>
          <DatesProvider
            settings={{
              locale: "en",
              timezone: "UTC",
            }}
          >
            <YearPickerInput
              value={selectedDate}
              placeholder={"Select year"}
              rightSection={<IconCaretDownFilled />}
              rightSectionPointerEvents="none"
              w={rem(180)}
              onChange={(date) => setSelectedDate(date as Date)}
            />
          </DatesProvider>
          <AsyncSelect />
          <Button
            ml={"auto"}
            leftSection={<IconPlus size={15} />}
            variant="default"
            onClick={() =>
              modals.openContextModal({
                modal: "generateReport",
                innerProps: {},
              })
            }
          >
            Generate Report
          </Button>
        </Flex>
        <Flex gap={rem(10)} wrap={"wrap"}>
          {payrollMonths.map((month, index) => (
            <PayrollList month={month} key={index} />
          ))}
        </Flex>
      </Flex>

      <PayrollDetails />
      {/* </Flex> */}
    </Container>
  );
}

function PayrollList({ month }: { month: FetchedPayrollMonths[number] }) {
  const theme = useMantineTheme();
  const [searchParam, setSearchParam] = useSearchParams();

  const handleButtonClick = () => {
    setSearchParam((params) => {
      params.set("startDate", month);
      params.set("endDate", getLastDayOfMonth(month));
      return params;
    });
  };

  return (
    <Button
      onClick={handleButtonClick}
      h={rem(120)}
      justify="start"
      variant="default"
      styles={{
        label: {
          width: "100%",
        },
      }}
      bg={searchParam.get("startDate")?.toString() == month ? "dark.4" : ""}
      className="flex-[1_1_100%] lg:flex-[0_1_49%] xl:flex-[0_1_32%] 2xl:flex-[0_1_24%] 2xl:max-w-96"
    >
      <Flex
        direction={"column"}
        h={"100%"}
        py={rem(20)}
        gap={rem(5)}
        w={"100%"}
      >
        <Flex gap={rem(5)} justify={"space-between"} align={"center"}>
          <Text size={rem(20)} fw={600}>
            {stringToMonthString(month)}
          </Text>
          {/* <Text size={rem(17)} fw={600}>
            ₱ 15,200
          </Text> */}
        </Flex>
        <Text
          size={"xs"}
          mr={"auto"} // Align to the left
          lts={rem(0.4)}
          fw={600}
          c={theme.colors.gray[6]}
        >
          {month}
        </Text>
        <IconArrowRight
          className="mt-auto ml-auto"
          color={theme.colors.dark[2]}
        />
      </Flex>
    </Button>
  );
}

function PayrollDetails() {
  const [searchParam, setSearchParam] = useSearchParams();
  const { getPayroll } = payrollController();
  const { getUser } = userController();
  const { getEmployeePayrollReport } = reportController();

  // Local Vars
  const startDate = searchParam.get("startDate")?.toString()!;
  const endDate = searchParam.get("endDate")?.toString()!;

  // Use States
  const [payrollDetails, setPayrollDetails] = useState({
    grossSalary: grossSalaryDetailsFallback,
    allowances: allowancesDetailsFallback,
    deductions: deductionsDetailsFallback,
    taxableSalary: taxableSalaryDetailsFallback,
    salaryAfterTax: salaryAfterTaxDetailsFallback,
    netSalary: netSalaryDetailsFallback,
  });

  // Funcs
  const employeeToFetch = useMemo(() => {
    const user = getUser();
    if (!user) return undefined;
    if (
      isAdmin(user.employmentInfo.position) &&
      searchParam.get("employeeNumber")
    ) {
      return parseInt(searchParam.get("employeeNumber")!);
    }
    return user.employeeNumber;
  }, [searchParam]);

  const memoizedPayroll = useMemo(() => {
    // const user = getUser();
    // let employeeToFetch = user?.employeeNumber;
    const startDate = searchParam.get("startDate");
    const endDate = searchParam.get("endDate");

    if (!employeeToFetch || !startDate || !endDate) {
      return;
    }

    // if (
    //   isAdmin(user?.employmentInfo.position) &&
    //   searchParam.get("employeeNumber")
    // ) {
    //   employeeToFetch = parseInt(searchParam.get("employeeNumber")!);
    // }

    getPayroll(employeeToFetch, startDate, endDate).then((response) => {
      setPayrollDetails({
        grossSalary: formatGrossSalary(response.data),
        allowances: formatAllowances(response.data),
        deductions: formatDeductions(response.data),
        taxableSalary: formatTaxableSalary(response.data),
        salaryAfterTax: formatSalaryAfterTax(response.data),
        netSalary: formatNetSalary(response.data),
      });
    });
  }, [searchParam]);

  const isPayrollDetailsMounted: boolean = useMemo(() => {
    const startDate = searchParam.get("startDate");
    const endDate = searchParam.get("endDate");

    return startDate && endDate ? true : false;
  }, [searchParam]);

  const handleClosePayrollDetails = () => {
    setSearchParam((params) => {
      params.delete("startDate");
      params.delete("endDate");
      return params;
    });
  };

  return (
    <Transition
      mounted={isPayrollDetailsMounted}
      transition="slide-left"
      duration={250}
      timingFunction="ease"
    >
      {(styles) => (
        <Paper
          className="absolute right-10 top-10 bottom-10"
          miw={rem(330)}
          p={rem(20)}
          style={{
            ...styles,
            zIndex: 10,
            overflowY: "auto",
          }}
        >
          <Flex w={"100%"} h={"100%"} direction={"column"} gap={rem(17)}>
            <Flex align={"center"} h={"fit-content"} gap={rem(10)}>
              <ActionIcon
                variant="transparent"
                onClick={handleClosePayrollDetails}
              >
                <IconArrowLeft />
              </ActionIcon>
              <Stack gap={5}>
                <Text size={rem(20)} fw={600}>
                  Payslip Summary
                </Text>
                <Text size={rem(14)} fw={600} c={"dark.2"}>
                  {stringToMonthString(
                    searchParam.get("startDate")?.toString()!
                  )}
                </Text>
              </Stack>
              <ActionIcon
                variant="subtle"
                ml={"auto"}
                onClick={() =>
                  getEmployeePayrollReport(
                    "Employee Payroll Report",
                    employeeToFetch?.toString()!,
                    startDate,
                    endDate
                  )
                }
              >
                <IconDownload />
              </ActionIcon>
            </Flex>

            <PayrollSummaryGrossSalary
              grossSalaryDetails={payrollDetails.grossSalary}
            />
            <PayrollSummaryAllowances
              allowancesDetails={payrollDetails.allowances}
            />
            <PayrollSummaryDeductions
              deductionsDetails={payrollDetails.deductions}
            />
            <Divider />
            <PayrollSummaryTaxableSalary
              taxableSalaryDetails={payrollDetails.taxableSalary}
            />
            <PayrollSummarySalaryAfterTax
              salaryAfterTaxDetails={payrollDetails.salaryAfterTax}
            />
            <Divider />
            <PayrollSummaryNetSalary
              netSalaryDetails={payrollDetails.netSalary}
            />
          </Flex>
        </Paper>
      )}
    </Transition>
  );
}

function PayrollSummaryGrossSalary({
  grossSalaryDetails,
}: {
  grossSalaryDetails: PayrollGrossSalaryDetails;
}) {
  return (
    <Flex gap={rem(15)} direction={"column"}>
      <Flex justify={"space-between"}>
        <Text size={rem(20)} fw={600}>
          Gross Salary
        </Text>
        <Text size={rem(20)} fw={600}>
          <NumberFormatter
            decimalScale={2}
            prefix="₱ "
            // value={Object.values(grossSalaryDetails).reduce(
            //   (acc, curr) => acc * curr,
            //   1
            // )}
            value={grossSalaryDetails.grossSalary}
            thousandSeparator
          />
        </Text>
      </Flex>
      <Flex direction={"column"} gap={rem(5)} pl={rem(20)}>
        <Flex justify={"space-between"}>
          <Text fw={300}>Hours Rendered</Text>
          <Text fw={300}>
            <NumberFormatter
              decimalScale={2}
              value={grossSalaryDetails.totalHoursRendered}
              thousandSeparator
            />
          </Text>
        </Flex>
        <Flex justify={"space-between"}>
          <Text fw={300}>Hourly Rate</Text>
          <Text fw={300}>
            <NumberFormatter
              decimalScale={2}
              prefix="₱ "
              value={grossSalaryDetails.hourlyRate}
              thousandSeparator
            />
          </Text>
        </Flex>
      </Flex>
    </Flex>
  );
}

function PayrollSummaryAllowances({
  allowancesDetails,
}: {
  allowancesDetails: PayrollAllowancesDetails;
}) {
  const subsections = useMemo(
    () => [
      {
        title: "Rice Subsidy",
        value: allowancesDetails.riceSubsidy,
      },
      {
        title: "Phone Allowance",
        value: allowancesDetails.phoneAllowance,
      },
      {
        title: "Clothing Allowance",
        value: allowancesDetails.clothingAllowance,
      },
    ],
    [allowancesDetails]
  );

  return (
    <Flex gap={rem(15)} direction={"column"}>
      <Flex justify={"space-between"}>
        <Text size={rem(20)} fw={600}>
          Allowances
        </Text>
        <Text size={rem(20)} fw={600}>
          <NumberFormatter
            decimalScale={2}
            prefix="₱ "
            value={allowancesDetails.totalAllowances}
            thousandSeparator
          />
        </Text>
      </Flex>
      <Flex direction={"column"} gap={rem(5)} pl={rem(20)}>
        {subsections.map((subsection, index) => (
          <Flex justify={"space-between"} key={index}>
            <Text fw={300}>{subsection.title}</Text>
            <Text fw={300}>
              <NumberFormatter
                decimalScale={2}
                prefix="₱ "
                value={subsection.value}
                thousandSeparator
              />
            </Text>
          </Flex>
        ))}
      </Flex>
    </Flex>
  );
}

function PayrollSummaryDeductions({
  deductionsDetails,
}: {
  deductionsDetails: PayrollDeductionsDetails;
}) {
  const subsections = useMemo(
    () => [
      {
        title: "SSS",
        value: deductionsDetails.sssDeduction,
      },
      {
        title: "PhilHealth",
        value: deductionsDetails.philhealthDeduction,
      },
      {
        title: "Pag-IBIG",
        value: deductionsDetails.pagibigDeduction,
      },
    ],
    [deductionsDetails]
  );

  return (
    <Flex gap={rem(15)} direction={"column"}>
      <Flex justify={"space-between"}>
        <Text size={rem(20)} fw={600}>
          Deductions
        </Text>
        <Text size={rem(20)} fw={600}>
          <NumberFormatter
            decimalScale={2}
            prefix="₱ "
            // value={Object.values(deductionsDetails).reduce(
            //   (acc, curr) => acc + curr,
            //   0
            // )}
            value={deductionsDetails.totalDeductions}
            thousandSeparator
          />
        </Text>
      </Flex>
      <Flex direction={"column"} gap={rem(5)} pl={rem(20)}>
        {subsections.map((subsection, index) => (
          <Flex justify={"space-between"} key={index}>
            <Text fw={300}>{subsection.title}</Text>
            <Text fw={300}>
              <NumberFormatter
                decimalScale={2}
                prefix="₱ "
                value={subsection.value}
                thousandSeparator
              />
            </Text>
          </Flex>
        ))}
      </Flex>
    </Flex>
  );
}

function PayrollSummaryTaxableSalary({
  taxableSalaryDetails,
}: {
  taxableSalaryDetails: PayrollTaxableSalaryDetails;
}) {
  const subsections = useMemo(
    () => [
      {
        title: "Deductions",
        value: taxableSalaryDetails.totalDeductions,
      },
      {
        title: "Gross Salary",
        value: taxableSalaryDetails.grossSalary,
      },
    ],
    [taxableSalaryDetails]
  );

  return (
    <Flex gap={rem(15)} direction={"column"}>
      <Flex justify={"space-between"}>
        <Text size={rem(20)} fw={600}>
          Taxable Salary
        </Text>
        <Text size={rem(20)} fw={600}>
          <NumberFormatter
            decimalScale={2}
            prefix="₱ "
            value={taxableSalaryDetails.taxableSalary}
            thousandSeparator
          />
        </Text>
      </Flex>
      <Flex direction={"column"} gap={rem(5)} pl={rem(20)}>
        {subsections.map((subsection, index) => (
          <Flex justify={"space-between"} key={index}>
            <Text fw={300}>{subsection.title}</Text>
            <Text fw={300}>
              <NumberFormatter
                decimalScale={2}
                prefix="₱ "
                value={subsection.value}
                thousandSeparator
              />
            </Text>
          </Flex>
        ))}
      </Flex>
    </Flex>
  );
}

function PayrollSummarySalaryAfterTax({
  salaryAfterTaxDetails,
}: {
  salaryAfterTaxDetails: PayrollSalaryAfterTaxDetails;
}) {
  const subsections = useMemo(
    () => [
      {
        title: "Taxable Salary",
        value: salaryAfterTaxDetails.taxableSalary,
      },
      {
        title: "Withholding Tax",
        value: salaryAfterTaxDetails.withHoldingTax,
      },
    ],
    [salaryAfterTaxDetails]
  );

  return (
    <Flex gap={rem(15)} direction={"column"}>
      <Flex justify={"space-between"}>
        <Text size={rem(20)} fw={600}>
          Salary After Tax
        </Text>
        <Text size={rem(20)} fw={600}>
          <NumberFormatter
            decimalScale={2}
            prefix="₱ "
            value={salaryAfterTaxDetails.salaryAfterTax}
            thousandSeparator
          />
        </Text>
      </Flex>
      <Flex direction={"column"} gap={rem(5)} pl={rem(20)}>
        {subsections.map((subsection, index) => (
          <Flex justify={"space-between"} key={index}>
            <Text fw={300}>{subsection.title}</Text>
            <Text fw={300}>
              <NumberFormatter
                decimalScale={2}
                prefix="₱ "
                value={subsection.value}
                thousandSeparator
              />
            </Text>
          </Flex>
        ))}
      </Flex>
    </Flex>
  );
}

function PayrollSummaryNetSalary({
  netSalaryDetails,
}: {
  netSalaryDetails: PayrollNetSalaryDetails;
}) {
  const subsections = useMemo(
    () => [
      {
        title: "Salary After Tax",
        value: netSalaryDetails.salaryAfterTax,
      },
      {
        title: "Allowances",
        value: netSalaryDetails.totalAllowances,
      },
    ],
    [netSalaryDetails]
  );

  return (
    <Flex gap={rem(15)} direction={"column"} pb={rem(20)}>
      <Flex justify={"space-between"}>
        <Text size={rem(20)} fw={600}>
          Net Salary
        </Text>
        <Text size={rem(20)} fw={600}>
          <NumberFormatter
            decimalScale={2}
            prefix="₱ "
            value={netSalaryDetails.netSalary}
            thousandSeparator
          />
        </Text>
      </Flex>
      <Flex direction={"column"} gap={rem(5)} pl={rem(20)}>
        {subsections.map((subsection, index) => (
          <Flex justify={"space-between"} key={index}>
            <Text fw={300}>{subsection.title}</Text>
            <Text fw={300}>
              <NumberFormatter
                decimalScale={2}
                prefix="₱ "
                value={Number(subsection.value)}
                thousandSeparator
              />
            </Text>
          </Flex>
        ))}
      </Flex>
    </Flex>
  );
}

export default Payroll;
