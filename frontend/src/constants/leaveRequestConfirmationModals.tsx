import { modals } from "@mantine/modals";
import { Text } from "@mantine/core";

type openConfirmModalAction = "approve" | "deny" | "delete";

export const openConfirmModal = (
  action: openConfirmModalAction,
  onConfirm: () => void
) => {
  const titles = {
    approve: "Approve Leave Request",
    deny: "Deny Leave Request",
    delete: "Delete Leave Request",
  };

  const messages = {
    approve: "Are you sure you want to approve this leave request?",
    deny: "Are you sure you want to deny this leave request?",
    delete: "Are you sure you want to delete this leave request?",
  };

  const buttonLabels = {
    approve: "Approve",
    deny: "Deny",
    delete: "Delete",
  };

  modals.openConfirmModal({
    centered: true,
    title: (
      <Text fw={700} size="md" lh={"h4"}>
        {titles[action]}
      </Text>
    ),
    children: messages[action],
    labels: { confirm: buttonLabels[action], cancel: "Cancel" },
    cancelProps: { variant: "subtle" },
    confirmProps: { variant: "filled" },
    onConfirm: onConfirm,
    onCancel: () => null,
  });
};
