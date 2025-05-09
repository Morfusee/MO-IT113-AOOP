import {
  CloseButton,
  Combobox,
  InputBase,
  Loader,
  rem,
  ScrollArea,
  useCombobox,
} from "@mantine/core";
import { useMemo, useState } from "react";
import employeeController from "../../controllers/employeeController";
import { FetchedUser } from "../../types/responses";
import { formatFullName } from "../../utils/formatters";
import { useSearchParams } from "react-router";
import userController from "../../controllers/userController";
import { isAdmin } from "../../utils/permissionUtils";

export function AsyncSelect() {
  const [searchParams, setSearchParams] = useSearchParams("");
  const [data, setData] = useState<FetchedUser[]>([]);
  const [loading, setLoading] = useState(false);

  const { getAllEmployees } = employeeController();
  const { getUser } = userController();

  const filteredOptions = useMemo(() => {
    const search = searchParams.get("fullName")?.toString();

    if (!search) return data;

    return data.filter((item) =>
      formatFullName(
        item.employee.personalInfo.firstName,
        item.employee.personalInfo.lastName
      )
        .toLowerCase()
        .includes(search.toLowerCase().trim())
    );
  }, [data, searchParams]);

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

  const handleSearchParamsCallback = (
    params: URLSearchParams,
    value: string
  ) => {
    // If the value is empty, remove the employee number from the search params
    if (value === "") {
      params.delete("fullName");
      params.delete("employeeNumber");
      return params;
    }

    params.set("fullName", value);
    params.set(
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
    return params;
  };

  const handleResetValues = () => {
    setSearchParams((params) => handleSearchParamsCallback(params, ""));
  };

  const handleOnChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    combobox.openDropdown();
    combobox.updateSelectedOptionIndex();
    setSearchParams((params) =>
      handleSearchParamsCallback(params, event.currentTarget.value)
    );
  };

  const handleOnBlur = () => {
    combobox.closeDropdown();
    setSearchParams((params) =>
      handleSearchParamsCallback(params, searchParams.get("fullName") || "")
    );
  };

  const handleOnOptionSubmit = (value: string) => {
    setSearchParams((params) => handleSearchParamsCallback(params, value));
    combobox.closeDropdown();
  };

  const memoizedIsAdmin = useMemo(() => {
    return isAdmin(getUser()?.employmentInfo.position);
  }, [getUser]);

  if (!memoizedIsAdmin) return null;

  return (
    <Combobox
      store={combobox}
      withinPortal={false}
      onOptionSubmit={handleOnOptionSubmit}
    >
      <Combobox.Target>
        <InputBase
          value={searchParams.get("fullName") || ""}
          rightSection={
            loading ? (
              <Loader size={18} />
            ) : searchParams.get("fullName") !== null ? (
              <CloseButton
                size="sm"
                onMouseDown={(event) => event.preventDefault()}
                onClick={handleResetValues}
                aria-label="Clear value"
              />
            ) : (
              <Combobox.Chevron />
            )
          }
          onClick={() => combobox.toggleDropdown()}
          onChange={handleOnChange}
          onBlur={handleOnBlur}
          placeholder="Search Employee"
          miw={rem(300)}
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
