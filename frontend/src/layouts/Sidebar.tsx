import { Avatar, Button, Flex, Paper, Text, rem } from "@mantine/core";
import { useLocation, useNavigate } from "react-router";
import LogoutMenu from "../components/menus/LogoutMenu";
import userController from "../controllers/userController";
import { formatFullName } from "../utils/formatters";
import { sidebarButtons } from "../constants/sidebarButtons";
import { getRoutesByPermission } from "../utils/permissionUtils";
import { useMemo } from "react";

function Sidebar() {
  return (
    <Paper radius={"xs"} miw={rem(280)} maw={rem(280)} p={rem(15)} py={rem(30)}>
      <Flex
        h={"100%"}
        w={"100%"}
        align={"center"}
        gap={rem(5)}
        direction={"column"}
      >
        <Text
          size={rem(32)}
          fw={600}
          lts={"0.025rem"}
          pt={rem(20)}
          pb={rem(40)}
        >
          MotorPH
        </Text>
        <SidebarButtons />
        <ProfileButtonGroup />
      </Flex>
    </Paper>
  );
}

function SidebarButtons() {
  // React Router
  const location = useLocation();
  const navigate = useNavigate();

  const navigateToPage = (path: string, param?: string) => {
    // Don't navigate again to the same path if it's already the current path
    if (path === location.pathname) return;

    // Handle query parameters if there are any
    navigate(path + (param ? param : ""));
  };

  const { getUser } = userController();
  const positions = getUser()?.employmentInfo?.position;

  const memoizedRoutes = useMemo(() => {
    return getRoutesByPermission(sidebarButtons, positions);
  }, [positions, sidebarButtons]);

  return (
    <>
      {memoizedRoutes.map((button, index) => (
        <Button
          key={index}
          component="div"
          fullWidth
          size="md"
          justify="flex-start"
          leftSection={<button.icon />}
          style={{
            "--button-padding-x": "0.7rem",
            "--button-bg":
              location.pathname === button.path
                ? "var(--mantine-color-dark-5)"
                : "none",
            "--button-hover-color": "var(--mantine-color-gray-3)",
            "--button-color":
              location.pathname === button.path
                ? "var(--mantine-color-gray-3"
                : "var(--mantine-color-gray-5)",
            paddingLeft: "0.75rem",
          }}
          onClick={() => navigateToPage(button.path, button.param)}
        >
          {button.name}
        </Button>
      ))}
    </>
  );
}

function ProfileButtonGroup() {
  const { getUser } = userController();
  const personalInformation = getUser()?.personalInfo;
  const employeeInformation = getUser()?.employmentInfo;

  return (
    <LogoutMenu>
      <Button
        component="div"
        w={"100%"}
        mt={"auto"}
        h={rem(60)}
        justify={"flex-start"}
        variant="subtle"
      >
        <Flex align={"center"} gap={rem(10)}>
          <Avatar radius="xl" />
          <Flex direction={"column"}>
            <Text
              className="flex justify-start"
              truncate="end"
              fw={500}
              size="md"
            >
              {formatFullName(
                personalInformation?.firstName,
                personalInformation?.lastName
              )}
            </Text>
            <Text
              className="flex justify-start"
              truncate="end"
              fw={300}
              size="sm"
            >
              {employeeInformation?.position}
            </Text>
          </Flex>
        </Flex>
      </Button>
    </LogoutMenu>
  );
}

export default Sidebar;
