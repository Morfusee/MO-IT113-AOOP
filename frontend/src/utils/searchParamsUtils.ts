import { URLSearchParamsInit, NavigateOptions } from "react-router";

export const deleteActionInSearchParams = (
  setSearchParams: (
    nextInit?:
      | URLSearchParamsInit
      | ((prev: URLSearchParams) => URLSearchParamsInit),
    navigateOpts?: NavigateOptions
  ) => void
) => {
  setSearchParams((params) => {
    params.delete("action");
    return params;
  });
};
