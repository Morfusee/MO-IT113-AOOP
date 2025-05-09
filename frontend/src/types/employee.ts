/* TYPE OF FORM */
export type EmployeeFormValues = PersonalInformation &
  EmploymentDetails &
  GovernmentIdentification &
  CompensationInformation;

export interface PersonalInformation {
  firstName: string;
  lastName: string;
  username: string;
  birthdate: Date;
  phoneNumber: string;
  address: string;
}

export interface EmploymentDetails {
  position: string;
  supervisor: string;
  employmentStatus: string;
  hourlyRate: number;
}

export interface GovernmentIdentification {
  sss: string;
  philhealth: string;
  pagibig: string;
  tin: string;
}

export interface CompensationInformation {
  basicSalary: number;
  grossSemiMonthlyRate: number;
  riceSubsidy: number;
  phoneAllowance: number;
  clothingAllowance: number;
}

export type FormInputType = "text" | "number" | "date" | "select";

export interface FormSection {
  title: string;
  fields: FormField[];
}

export interface FormField {
  type: "text" | "number" | "date" | "select";
  name: string;
  label: string;
  placeholder?: string;
  required?: boolean;
  disabled?: boolean;
  className?: string;
  data?: { label: string; value: string }[];
}

/* TYPE FOR THE RESPONSE PAYLOAD */
export interface EmployeeData extends IEmployee {}

/* TYPE FOR THE ZUSTAND */
export interface IEmployee {
  userId: number;
  username: string;
  employeeNumber: number;
  personalInfo: IPersonalInformation;
  employmentInfo: IEmploymentInformation;
  governmentIds: IGovernmentIds;
  compensation: ICompensation;
}

export interface IPersonalInformation {
  lastName: string;
  firstName: string;
  birthday: string;
  address: string;
  phoneNumber: string;
}

export interface IEmploymentInformation {
  status: string;
  position: string;
  immediateSupervisor: string;
}

export interface IGovernmentIds {
  governmentId: number;
  philhealth: string;
  sss: string;
  pagibig: string;
  tin: string;
}

export interface ICompensation {
  compensationId: number;
  basicSalary: number;
  riceSubsidy: number;
  phoneAllowance: number;
  clothingAllowance: number;
  grossSemiMonthlyRate: number;
  hourlyRate: number;
}

/* TYPE FOR THE REQUEST */
export interface EmployeeRequest {
  user: {
    username: string;
  };
  personalInfo: IPersonalInformation;
  employmentInfo: IEmploymentInformation;
  governmentIds: Omit<IGovernmentIds, "governmentId">;
  compensation: Omit<ICompensation, "compensationId">;
}
