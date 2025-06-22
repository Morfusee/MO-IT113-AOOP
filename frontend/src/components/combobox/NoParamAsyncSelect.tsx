import {
  CloseButton,
  Combobox,
  InputBase,
  Loader,
  rem,
  ScrollArea,
  useCombobox,
} from "@mantine/core";
import { UseFormReturnType } from "@mantine/form";
import { useMemo, useState } from "react";
import employeeController from "../../controllers/employeeController";
import userController from "../../controllers/userController";
import { FetchedUser } from "../../types/responses";
import { formatFullName } from "../../utils/formatters";
import { IGenerateReportForm } from "../modals/GenerateReport";
import { isAdmin } from "../../utils/permissionUtils";

export function NoParamAsyncSelect({
  label,
  externalForm,
}: {
  label?: string;
  externalForm: UseFormReturnType<IGenerateReportForm>;
}) {
  const [data, setData] = useState<FetchedUser[]>([]);
  const [loading, setLoading] = useState(false);

  const { getAllEmployees } = employeeController();
  const { getUser } = userController();

  const employeeFullName = externalForm.getValues().employeeFullName;

  const filteredOptions = useMemo(() => {
    const search = employeeFullName;

    if (!search) return data;

    return data.filter((item) =>
      formatFullName(
        item.employee.personalInfo.firstName,
        item.employee.personalInfo.lastName
      )
        .toLowerCase()
        .includes(search.toLowerCase().trim())
    );
  }, [data, employeeFullName]);

  const combobox = useCombobox({
    onDropdownClose: () => combobox.resetSelectedOption(),
    onDropdownOpen: () => {
      if (data.length === 0 && !loading) {
        setLoading(true);
        getAllEmployees().then((response) => {
          setData(response.data);
          setLoading(false);
          combobox.resetSelectedOption();
        });
      }
    },
  });

  const handleExternalFormCallback = (
    form: UseFormReturnType<IGenerateReportForm>,
    value: string
  ) => {
    // If the value is empty, remove the employee number from the search params
    if (value === "") {
      form.setFieldValue("employeeFullName", "");
      return form;
    }

    form.setFieldValue("employeeFullName", value);
    form.setFieldValue(
      "employeeNumber",
      data
        .find(
          (item) =>
            formatFullName(
              item.employee.personalInfo.firstName,
              item.employee.personalInfo.lastName
            )
              .toLowerCase()
              .trim() === value.toLowerCase().trim()
        )
        ?.employee.employeeNumber.toString() || ""
    );
    return form;
  };

  const handleResetValues = () => {
    externalForm.setFieldValue("employeeFullName", "");
    externalForm.setFieldValue("employeeNumber", "");
  };

  const handleOnChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    combobox.openDropdown();
    combobox.updateSelectedOptionIndex();
    handleExternalFormCallback(externalForm, event.currentTarget.value);
  };

  const handleOnBlur = () => {
    combobox.closeDropdown();
    handleExternalFormCallback(
      externalForm,
      externalForm.getValues().employeeFullName || ""
    );
  };

  const handleOnOptionSubmit = (value: string) => {
    handleExternalFormCallback(externalForm, value);
    combobox.closeDropdown();
  };

  const memoizedIsAdmin = useMemo(() => {
    return isAdmin(getUser()?.employmentInfo.position);
  }, [getUser]);

  return (
    <Combobox store={combobox} onOptionSubmit={handleOnOptionSubmit}>
      <Combobox.Target>
        <InputBase
          key={externalForm.key("employeeFullName")}
          {...externalForm.getInputProps("employeeFullName")}
          value={employeeFullName || ""}
          disabled={!memoizedIsAdmin}
          rightSection={
            loading ? (
              <Loader size={18} />
            ) : employeeFullName !== null ? (
              <CloseButton
                size="sm"
                onMouseDown={(event) => event.preventDefault()}
                onClick={handleResetValues}
                aria-label="Clear value"
                disabled={!memoizedIsAdmin}
              />
            ) : (
              <Combobox.Chevron />
            )
          }
          onClick={() => combobox.toggleDropdown()}
          onBlur={handleOnBlur}
          placeholder="Search Employee"
          miw={rem(300)}
          label={label}
          styles={{
            label: {
              paddingBottom: label ? "5px" : "0px",
            },
          }}
          onChange={handleOnChange}
        />
      </Combobox.Target>

      <Combobox.Dropdown>
        <Combobox.Options>
          <ScrollArea.Autosize mah={300} type="scroll">
            {loading ? (
              <Combobox.Empty>Loading....</Combobox.Empty>
            ) : (
              <ComboboxOption filteredOptions={filteredOptions} />
            )}
          </ScrollArea.Autosize>
        </Combobox.Options>
      </Combobox.Dropdown>
    </Combobox>
  );
}

function ComboboxOption({
  filteredOptions,
}: {
  filteredOptions: FetchedUser[];
}) {
  return (
    <>
      {filteredOptions.map((item) => (
        <Combobox.Option
          value={formatFullName(
            item.employee.personalInfo.firstName,
            item.employee.personalInfo.lastName
          )}
          key={item.employee.userId}
        >
          {formatFullName(
            item.employee.personalInfo.firstName,
            item.employee.personalInfo.lastName
          )}
        </Combobox.Option>
      ))}
    </>
  );
}
