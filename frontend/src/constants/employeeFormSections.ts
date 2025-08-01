import { FormSection as FormSectionType } from "../types/employee";

const employmentStatusOptions = [
  { label: "Regular", value: "Regular" },
  { label: "Probationary", value: "Probationary" },
];

export const employeeFormSections = [
  {
    title: "Personal Information",
    fields: [
      {
        type: "text",
        label: "First Name",
        placeholder: "John",
        required: true,
        name: "firstName",
        className: "flex-[1_1_49%]",
      },
      {
        type: "text",
        label: "Last Name",
        placeholder: "Doe",
        required: true,
        name: "lastName",
        className: "flex-[1_1_49%]",
      },
      {
        type: "text",
        label: "Username",
        placeholder: "johndoe",
        required: true,
        name: "username",
        className: "flex-[1_1_49%]",
      },
      {
        type: "date",
        label: "Birthdate",
        placeholder: "January 1, 2000",
        required: true,
        name: "birthdate",
        className: "flex-[1_1_49%]",
      },
      {
        type: "number",
        label: "Phone Number",
        placeholder: "+639123456789",
        required: true,
        name: "phoneNumber",
        className: "flex-[1_1_49%]",
      },
      {
        type: "text",
        label: "Address",
        placeholder: "1234 Main St., City, Country",
        required: true,
        name: "address",
        className: "flex-[1_1_100%]",
      },
    ],
  },
  {
    title: "Employment Details",
    fields: [
      {
        type: "text",
        label: "Position",
        placeholder: "HR Manager",
        required: true,
        name: "position",
        className: "flex-[1_1_49%]",
      },
      {
        type: "text",
        label: "Supervisor",
        placeholder: "Jane Doe",
        required: true,
        name: "supervisor",
        className: "flex-[1_1_49%]",
      },
      {
        type: "select",
        label: "Employment Status",
        placeholder: "Regular",
        required: true,
        name: "employmentStatus",
        className: "flex-[1_1_49%]",
        data: employmentStatusOptions,
      },
    ],
  },
  {
    title: "Government Identification",
    fields: [
      {
        type: "text",
        label: "SSS",
        placeholder: "12345678901234567890",
        required: true,
        name: "sss",
        className: "flex-[1_1_49%]",
      },
      {
        type: "text",
        label: "PhilHealth",
        placeholder: "12345678901234567890",
        required: true,
        name: "philhealth",
        className: "flex-[1_1_49%]",
      },
      {
        type: "text",
        label: "Pag-IBIG",
        placeholder: "12345678901234567890",
        required: true,
        name: "pagibig",
        className: "flex-[1_1_49%]",
      },
      {
        type: "text",
        label: "TIN",
        placeholder: "12345678901234567890",
        required: true,
        name: "tin",
        className: "flex-[1_1_49%]",
      },
    ],
  },
  {
    title: "Compensation Information",
    fields: [
      {
        type: "number",
        label: "Basic Salary",
        placeholder: "100.03",
        required: true,
        name: "basicSalary",
        className: "flex-[1_1_49%]",
      },
      {
        type: "number",
        label: "Gross Semi-Monthly Rate",
        placeholder: "100.03",
        required: true,
        name: "grossSemiMonthlyRate",
        className: "flex-[1_1_49%]",
      },
      {
        type: "number",
        label: "Rice Subsidy",
        placeholder: "100.03",
        required: true,
        name: "riceSubsidy",
        className: "flex-[1_1_49%]",
      },
      {
        type: "number",
        label: "Phone Allowance",
        placeholder: "100.03",
        required: true,
        name: "phoneAllowance",
        className: "flex-[1_1_49%]",
      },
      {
        type: "number",
        label: "Clothing Allowance",
        placeholder: "100.03",
        required: true,
        name: "clothingAllowance",
        className: "flex-[1_1_49%]",
      },
      {
        type: "number",
        label: "Hourly Rate",
        placeholder: "100.03",
        required: true,
        name: "hourlyRate",
        className: "flex-[1_1_49%]",
      },
    ],
  },
] as FormSectionType[];
