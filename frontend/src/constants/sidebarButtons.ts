import {
  IconUsers,
  IconLayoutDashboard,
  IconCoin,
  IconCalendarWeekFilled,
  IconId,
} from "@tabler/icons-react";
import { ISidebarButton } from "../types/sidebar";

export const sidebarButtons: ISidebarButton[] = [
  {
    name: "Employees",
    path: "/employees",
    icon: IconUsers,
    isAdminOnly: true,
  },
  {
    name: "Attendance",
    path: "/attendance",
    icon: IconLayoutDashboard,
    isAdminOnly: false,
  },
  {
    name: "Payroll",
    path: "/payroll",
    icon: IconCoin,
    isAdminOnly: false,
  },
  {
    name: "Leave Request",
    path: "/leave-request",
    param: "?status=Pending",
    icon: IconCalendarWeekFilled,
    isAdminOnly: false,
  },
  {
    name: "Profile",
    path: "/profile",
    icon: IconId,
    isAdminOnly: false,
  },
];
