import { Menu, rem } from "@mantine/core";
import { IconId, IconLogout } from "@tabler/icons-react";
import { useNavigate } from "react-router";
import { Routes } from "../../constants/routes";
import userController from "../../controllers/userController";

function LogoutMenu({ children }: { children: React.ReactNode }) {
  const { logoutUser } = userController();
  const navigate = useNavigate();

  const handleLogout = async () => {
    await logoutUser().then((res) => {
      if (res) {
        navigate(Routes.LOGIN);
      }
    });
  };

  const handleViewProfile = () => {
    navigate(Routes.PROFILE);
  };

  return (
    <Menu withArrow width={rem(170)}>
      <Menu.Target>{children}</Menu.Target>

      <Menu.Dropdown>
        <Menu.Item
          leftSection={<IconId size={14} />}
          onClick={handleViewProfile}
        >
          View Profile
        </Menu.Item>
        <Menu.Item
          color="red"
          leftSection={<IconLogout size={14} />}
          onClick={handleLogout}
        >
          Logout
        </Menu.Item>
      </Menu.Dropdown>
    </Menu>
  );
}

export default LogoutMenu;
