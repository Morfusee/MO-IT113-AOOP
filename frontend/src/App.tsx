import { MantineProvider } from "@mantine/core";
import { ModalsProvider } from "@mantine/modals";
import { Notifications } from "@mantine/notifications";
import { Route, Routes } from "react-router";
import CreateOrEditEmployee from "./components/modals/CreateOrEditEmployee";
import CreateLeaveRequest from "./components/modals/CreateLeaveRequest";
import ViewEmployee from "./components/modals/ViewEmployee";
import ThemeContext from "./contexts/ThemeContext";
import Layout from "./layouts/Layout";
import Attendance from "./pages/Attendance";
import Employees from "./pages/Employees";
import LeaveRequest from "./pages/LeaveRequest";
import Login from "./pages/Login";
import Payroll from "./pages/Payroll";
import Profile from "./pages/Profile";
import ProtectedRoute from "./routes/ProtectedRoute";
import AdminRoutes from "./routes/AdminRoutes";

const modals = {
  createLeaveRequest: CreateLeaveRequest,
  viewEmployee: ViewEmployee,
  createOrEditEmployee: CreateOrEditEmployee,
};
declare module "@mantine/modals" {
  export interface MantineModalsOverride {
    modals: typeof modals;
  }
}

function App() {
  return (
    <MantineProvider defaultColorScheme="dark" forceColorScheme="dark">
      <ThemeProvider>
        <ModalsProvider modals={modals}>
          <Notifications />
          <Routing />
        </ModalsProvider>
      </ThemeProvider>
    </MantineProvider>
  );
}

function Routing() {
  return (
    <Routes>
      <Route path="/" element={<Login />} />
      <Route element={<ProtectedRoute />}>
        <Route element={<Layout />}>
          <Route path="attendance" element={<Attendance />} />
          <Route path="payroll" element={<Payroll />} />
          <Route path="profile" element={<Profile />} />
          <Route path="leave-request" element={<LeaveRequest />} />
          <Route element={<AdminRoutes />}>
            <Route path="employees" element={<Employees />} />
          </Route>
        </Route>
      </Route>
    </Routes>
  );
}

function ThemeProvider({ children }: { children: React.ReactNode }) {
  const theme = ThemeContext();

  return <MantineProvider theme={theme}>{children}</MantineProvider>;
}

export default App;
