import {
  Avatar,
  Container,
  Flex,
  NumberFormatter,
  Paper,
  rem,
  Text,
} from "@mantine/core";
import { useMemo } from "react";
import userController from "../controllers/userController";
import { ProfileCardProps } from "../types/profile";
import { formatEmployeeDetails, formatFullName } from "../utils/formatters";

function Profile() {
  const { getUser } = userController();

  const user = getUser();

  const profileCards = useMemo(() => {
    if (!user) return [];

    const employeeDetails = formatEmployeeDetails(user);

    return employeeDetails;
  }, [user]);

  const userFullName = useMemo(() => {
    if (!user) return "";

    return formatFullName(
      user.personalInfo.firstName,
      user.personalInfo.lastName
    );
  }, [user]);

  const userPosition = user?.employmentInfo.position;

  return (
    <Container
      w={"100%"}
      size={"xl"}
      px={rem(80)}
      py={rem(50)}
      style={{
        overflowY: "auto",
      }}
    >
      <Flex w={"100%"} gap={rem(30)} direction={"column"}>
        <Flex align={"center"} gap={rem(20)}>
          <Avatar size={rem(45)} />
          <Flex direction={"column"}>
            <Text size={rem(25)} fw={700}>
              {userFullName}
            </Text>
            <Text size={"sm"} c={"dimmed"}>
              {userPosition}
            </Text>
          </Flex>
        </Flex>
        <Flex gap={rem(10)} wrap={"wrap"} w={"100%"}>
          {profileCards.map((profileCard, index) => (
            <ProfileCard key={index} profileCardProps={profileCard} />
          ))}
        </Flex>
      </Flex>
    </Container>
  );
}

export function ProfileCard({
  profileCardProps,
}: {
  profileCardProps: ProfileCardProps;
}) {
  return (
    <Paper p={rem(20)} className="flex-[1_1_100%] lg:flex-[1_1_49%]">
      <Flex direction={"column"} gap={rem(20)}>
        <Flex gap={"sm"} align={"center"}>
          <profileCardProps.icon size={20} />
          <Text fw={700} size="lg">
            {profileCardProps.title}
          </Text>
        </Flex>
        <Flex wrap={"wrap"} rowGap={rem(20)} columnGap={rem(10)}>
          {profileCardProps.details.map((info, index) => (
            <Flex
              direction={"column"}
              key={index}
              className="flex-[1_1_50%] xl:flex-[1_1_32%]"
            >
              <Text fw={700}>{info.title}</Text>
              {typeof info.value === "number" && info.title !== "Age" ? (
                <Text fw={100} c={"dark.1"}>
                  <NumberFormatter
                    value={info.value}
                    prefix="₱ "
                    thousandSeparator
                  />
                </Text>
              ) : (
                <Text lts={rem(0.2)} fw={100} c={"dark.1"}>
                  {info.value}
                </Text>
              )}
            </Flex>
          ))}
        </Flex>
      </Flex>
    </Paper>
  );
}

export default Profile;
