import { Menu, rem, Text } from "@mantine/core";
import { modals } from "@mantine/modals";
import { IconEdit, IconTrash } from "@tabler/icons-react";
import { useSearchParams } from "react-router";
import employeeController from "../../controllers/employeeController";
import { EmployeeData, IEmployee } from "../../types/employee";
import { deleteActionInSearchParams } from "../../utils/searchParamsUtils";

function EmployeeMenu({
  children,
  employee,
  employeeNum,
}: {
  children: React.ReactNode;
  employee: IEmployee;
  employeeNum: EmployeeData["employeeNumber"];
}) {
  const [searchParams, setSearchParams] = useSearchParams();

  const { deleteEmployee } = employeeController();

  const handleOpenDeleteModal = (
    e: React.MouseEvent<HTMLButtonElement, MouseEvent>
  ) => {
    e.stopPropagation();

    // This is just for triggering the re-render
    setSearchParams((params) => {
      params.set("action", "deleteEmployee");
      return params;
    });

    // Open confirm modal
    const modalId = modals.openConfirmModal({
      centered: true,
      title: (
        <Text fw={700} size="md" lh={"h4"}>
          Delete Employee
        </Text>
      ),
      children: "Are you sure you want to delete this employee?",
      labels: { confirm: "Delete", cancel: "Cancel" },
      cancelProps: { variant: "subtle" },
      confirmProps: { variant: "filled" },
      onConfirm: () => handleDelete(modalId),
      onClose: () => deleteActionInSearchParams(setSearchParams),
      closeOnConfirm: false,
    });
  };

  const handleDelete = async (modalId: string) => {
    // Delete employee
    await deleteEmployee(employeeNum)
      .then(() => {
        // Close modal after deleting
        modals.close(modalId);
      })
      .catch((error) => {
        console.error(error);
      });
  };

  const handleOpenEditModal = async (
    e: React.MouseEvent<HTMLButtonElement, MouseEvent>
  ) => {
    e.stopPropagation();

    // This is just for triggering the re-render
    setSearchParams((params) => {
      params.set("action", "createOrEditEmployee");
      return params;
    });

    modals.openContextModal({
      modal: "createOrEditEmployee",
      innerProps: { employeeToEdit: employee },
    });
  };

  return (
    <Menu withArrow width={rem(170)}>
      <Menu.Target>{children}</Menu.Target>

      <Menu.Dropdown>
        <Menu.Item
          leftSection={<IconEdit size={14} />}
          onClick={handleOpenEditModal}
        >
          Edit
        </Menu.Item>
        <Menu.Item
          color="red"
          leftSection={<IconTrash size={14} />}
          onClick={handleOpenDeleteModal}
        >
          Delete
        </Menu.Item>
      </Menu.Dropdown>
    </Menu>
  );
}

export default EmployeeMenu;
