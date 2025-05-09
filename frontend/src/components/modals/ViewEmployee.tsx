import { Container, em, Flex, rem, ScrollArea, Text } from "@mantine/core";
import { ContextModalProps } from "@mantine/modals";
import {
  IconBriefcase,
  IconCash,
  IconIdBadge2,
  IconUser,
} from "@tabler/icons-react";
import { useEffect, useMemo } from "react";
import { ProfileCard } from "../../pages/Profile";
import { FetchedUser } from "../../types/responses";
import { formatEmployeeDetails } from "../../utils/formatters";

function ViewEmployee({
  context,
  id,
  innerProps,
}: ContextModalProps<{
  employee: FetchedUser;
}>) {
  const { employee } = innerProps;

  // Set up the context
  useEffect(() => {
    context.updateModal({
      modalId: id,
      title: (
        <Text fw={700} size="md" lh={"h4"}>
          Employee Profile
        </Text>
      ),
      size: "auto",
      scrollAreaComponent: ScrollArea.Autosize,
      styles: {
        content: {
          overflowY: "hidden",
        },
      },
      centered: true,
    });
  }, [id]);

  const profileCards = useMemo(() => {
    if (!employee) return [];

    const employeeDetails = formatEmployeeDetails(employee.employee);

    return employeeDetails;
  }, [employee]);

  return (
    <Container w={"100%"} size={"lg"} py={rem(20)}>
      <Flex w={"100%"} gap={rem(10)} direction={"column"}>
        <Flex gap={rem(10)} wrap={"wrap"} w={"100%"}>
          {profileCards.map((profileCard, index) => (
            <ProfileCard key={index} profileCardProps={profileCard} />
          ))}
        </Flex>
      </Flex>
    </Container>
  );
}

export default ViewEmployee;
