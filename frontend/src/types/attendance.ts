import { IconProps, Icon } from "@tabler/icons-react";

export interface AttendanceStats {
  present: number;
  absent: number;
  lates: number;
  renderedHours: number;
  averageCheckIn: string;
  averageCheckOut: string;
}

export interface AttendanceStatsFrontend {
  present: AttendanceStatsObj;
  absent: AttendanceStatsObj;
  lates: AttendanceStatsObj;
  renderedHours: AttendanceStatsObj;
  averageCheckIn: AttendanceStatsObj;
  averageCheckOut: AttendanceStatsObj;
}

export interface AttendanceStatsObj {
  icon: React.ForwardRefExoticComponent<IconProps & React.RefAttributes<Icon>>;
  title: string;
  value: string | number;
}

export interface AttendanceData {
  date: string;
  employeeNumber: number;
  overtimeHours: number;
  status: string;
  timeIn: string;
  timeOut: string;
  totalHours: number;
}
