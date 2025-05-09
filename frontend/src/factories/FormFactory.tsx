import {
  Divider,
  Flex,
  NumberInput,
  NumberInputProps,
  Select,
  SelectProps,
  Text,
  TextInput,
  TextInputProps,
} from "@mantine/core";
import {
  DatePickerInput,
  DatePickerInputFactory
} from "@mantine/dates";
import { UseFormReturnType } from "@mantine/form";
import { Fragment } from "react/jsx-runtime";
import {
  EmployeeFormValues,
  FormField,
  FormSection as FormSectionType
} from "../types/employee";

function FormFactory({
  formSections,
  form,
}: {
  formSections: FormSectionType[];
  form: UseFormReturnType<
    EmployeeFormValues,
    (values: EmployeeFormValues) => EmployeeFormValues
  >;
}) {
  return (
    <>
      {formSections.map((section, index) => (
        <Fragment key={index}>
          <Text fw={600} size="lg">
            {section.title}
          </Text>
          <Flex w={"100%"} gap={10} wrap="wrap">
            {section.fields.map((field, index_2) => (
              <FormInputFactory key={index_2} field={field} form={form} />
            ))}
          </Flex>
          {index !== formSections.length - 1 && <Divider />}
        </Fragment>
      ))}
    </>
  );
}

function FormInputFactory({
  field,
  form,
}: {
  field: FormField;
  form: UseFormReturnType<
    EmployeeFormValues,
    (values: EmployeeFormValues) => EmployeeFormValues
  >;
}) {
  const fieldData = () => {
    const { type, name, ...desField } = field;
    return desField;
  };

  const fieldProps = fieldData();

  switch (field.type) {
    case "text":
      return (
        <TextInput
          {...(fieldProps as TextInputProps)}
          {...form.getInputProps(field.name)}
        />
      );
    case "number":
      return (
        <NumberInput
          {...(fieldProps as NumberInputProps)}
          {...form.getInputProps(field.name)}
        />
      );
    case "date":
      return (
        <DatePickerInput
          {...(fieldProps as unknown as Omit<DatePickerInputFactory, "ref">)}
          {...form.getInputProps(field.name)}
        />
      );
    case "select":
      return (
        <Select
          {...(fieldProps as SelectProps)}
          {...form.getInputProps(field.name)}
        />
      );
  }
}

export default FormFactory;
