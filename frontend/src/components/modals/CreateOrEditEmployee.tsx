import { Button, Flex, ScrollArea, Stack, Text } from "@mantine/core";
import { useForm } from "@mantine/form";
import { ContextModalProps } from "@mantine/modals";
import { useEffect } from "react";
import { useSearchParams } from "react-router";
import { employeeFormSections } from "../../constants/employeeFormSections";
import employeeController from "../../controllers/employeeController";
import FormSection from "../../factories/FormFactory";
import { EmployeeFormValues, IEmployee } from "../../types/employee";
import { formatEmployeeRequest } from "../../utils/formatters";
import { deleteActionInSearchParams } from "../../utils/searchParamsUtils";

function CreateOrEditEmployee({
  context,
  id,
  innerProps,
}: ContextModalProps<{
  employeeToEdit?: IEmployee;
}>) {
  const { employeeToEdit } = innerProps;
  const [searchParams, setSearchParams] = useSearchParams();

  const { createEmployee, editEmployee } = employeeController();

  const isEditMode = !!employeeToEdit;

  useEffect(() => {
    // This is just for triggering the re-render
    setSearchParams((params) => {
      params.set("action", "createOrEditEmployee");
      return params;
    });

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
      onClose: () => deleteActionInSearchParams(setSearchParams),
      centered: true,
    });
  }, [id]);

  const getInitialValues = (employee?: IEmployee): EmployeeFormValues => ({
    firstName: employee?.personalInfo.firstName || "",
    lastName: employee?.personalInfo.lastName || "",
    username: employee?.username || "",
    birthdate: new Date(employee?.personalInfo.birthday || new Date()),
    phoneNumber: employee?.personalInfo.phoneNumber || "",
    address: employee?.personalInfo.address || "",
    position: employee?.employmentInfo.position || "",
    supervisor: employee?.employmentInfo.immediateSupervisor || "",
    employmentStatus: employee?.employmentInfo.status || "",
    hourlyRate: employee?.compensation.hourlyRate || 0,
    basicSalary: employee?.compensation.basicSalary || 0,
    grossSemiMonthlyRate: employee?.compensation.grossSemiMonthlyRate || 0,
    riceSubsidy: employee?.compensation.riceSubsidy || 0,
    phoneAllowance: employee?.compensation.phoneAllowance || 0,
    clothingAllowance: employee?.compensation.clothingAllowance || 0,
    sss: employee?.governmentIds.sss || "",
    philhealth: employee?.governmentIds.philhealth || "",
    pagibig: employee?.governmentIds.pagibig || "",
    tin: employee?.governmentIds.tin || "",
  });

  const validateGovernmentIds = (value: string) =>
    /^[0-9-]+$/.test(value) ? null : "Must contain only numbers and dashes";

  const form = useForm<EmployeeFormValues>({
    initialValues: getInitialValues(employeeToEdit),
    validate: {
      sss: validateGovernmentIds,
      philhealth: validateGovernmentIds,
      pagibig: validateGovernmentIds,
      tin: validateGovernmentIds,
    },
  });

  const createMockData = () => {
    const mockData = {
      firstName: "Mark",
      lastName: "Markel",
      username: "Markel.mark",
      birthdate: new Date("1999-09-09"),
      phoneNumber: "090609082407",
      address: "Mactan Avenue, Cebu City",
      position: "Admin",
      supervisor: "N/A",
      employmentStatus: "Regular",
      hourlyRate: 123,
      sss: "09090222323123",
      philhealth: "09090232323232",
      pagibig: "0909023232323",
      tin: "23232323232",
      basicSalary: 16708.09,
      grossSemiMonthlyRate: 8000.0,
      riceSubsidy: 1000.0,
      phoneAllowance: 500.0,
      clothingAllowance: 500.0,
    };

    form.setValues(mockData);
  };

  const handleSubmit = async (values: EmployeeFormValues) => {
    if (!isEditMode) {
      return handleCreateEmployee(values);
    }

    return handleEditEmployee(values);
  };

  const handleCreateEmployee = async (values: EmployeeFormValues) => {
    // Create employee
    await createEmployee(formatEmployeeRequest(values))
      .then(() => {
        // Close modal
        context.closeModal(id);
      })
      .catch((error) => {
        console.error(error);
      });
  };

  const handleEditEmployee = async (values: EmployeeFormValues) => {
    if (!employeeToEdit) return;

    // Edit emplyoee
    await editEmployee(
      employeeToEdit?.employeeNumber,
      formatEmployeeRequest(values)
    )
      .then(() => {
        context.closeModal(id);
      })
      .catch((error) => {
        console.error(error);
      });
  };

  return (
    <form onSubmit={form.onSubmit((values) => handleSubmit(values))}>
      <Stack w={"100%"} gap={20}>
        <FormSection formSections={employeeFormSections} form={form} />
        <Flex gap="md" justify="flex-end" mt="md">
          <Button hidden={isEditMode} variant="subtle" onClick={createMockData}>
            Create Mock Data
          </Button>
          <Button variant="subtle" onClick={() => context.closeModal(id)}>
            Cancel
          </Button>
          <Button type="submit">
            {isEditMode ? "Edit Employee" : "Create Employee"}
          </Button>
        </Flex>
      </Stack>
    </form>
  );
}

export default CreateOrEditEmployee;
