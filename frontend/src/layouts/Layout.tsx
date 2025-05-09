import { Outlet } from "react-router";
import Sidebar from "./Sidebar";
import { Flex } from "@mantine/core";

function Layout() {
  return (
    <Flex
      mih={"100dvh"}
      mah={"100dvh"}
      direction={"row"}
      style={{
        overflowY: "hidden",
      }}
    >
      <Sidebar />
      <Outlet />
    </Flex>
  );
}

export default Layout;
