import { IconProps, Icon } from "@tabler/icons-react";
import { ISidebarButton } from "../types/sidebar";

export const isAdmin = (positions: string | undefined): boolean => {
  if (!positions) return false;
  positions = positions.toLowerCase();
  switch (positions) {
    case "hr manager":
      return true;
    default:
      return false;
  }
};

export const getRoutesByPermission = (
  sidebarButtons: ISidebarButton[],
  positions: string | undefined
) => {
  return sidebarButtons.filter((button) => {
    if (button.isAdminOnly) {
      return isAdmin(positions);
    }

    return true;
  });
};
