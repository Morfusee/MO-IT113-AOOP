import {
  IconCalendarWeek,
  IconExclamationCircle,
  IconBriefcase2,
  IconArrowDown,
  IconArrowUp,
  IconClockPause,
} from "@tabler/icons-react";

export const attendanceAnalytics = {
  present: {
    title: "Present",
    icon: IconCalendarWeek,
    value: 0,
  },
  lates: {
    title: "Lates",
    icon: IconClockPause,
    value: 0,
  },
  absent: {
    title: "Absent",
    icon: IconExclamationCircle,
    value: 0,
  },
  renderedHours: {
    title: "Rendered Hours",
    icon: IconBriefcase2,
    value: 0,
  },
  averageCheckIn: {
    title: "Average Check In",
    icon: IconArrowDown,
    value: "00:00:00",
  },
  averageCheckOut: {
    title: "Average Check Out",
    icon: IconArrowUp,
    value: "00:00:00",
  },
};
