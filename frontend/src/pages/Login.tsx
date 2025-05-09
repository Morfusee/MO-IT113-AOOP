import {
  Button,
  Container,
  Flex,
  Paper,
  PasswordInput,
  rem,
  Text,
  TextInput,
} from "@mantine/core";
import { useForm } from "@mantine/form";
import { useNavigate } from "react-router";
import userController from "../controllers/userController";
import { userCredentials } from "../types/login";
import useAuth from "../hooks/useAuth";
import { Routes } from "../constants/routes";

function Login() {
  const navigate = useNavigate();
  const { loginUser } = userController();
  const { isAuthenticated, loading } = useAuth();

  const form = useForm<userCredentials>({
    initialValues: {
      username: "jose.crisostomo",
      password: "password10001",
    },
    validate: {
      username: (value) => {
        if (!value.trim()) {
          return "Username is required";
        }
      },
      password: (value) => {
        if (!value.trim()) {
          return "Password is required";
        }
      },
    },
  });

  const handleLogin = async (
    username: userCredentials["username"],
    password: userCredentials["password"]
  ) => {
    await loginUser(username, password).then((response) => {
      if (response) navigate(Routes.EMPLOYEES, { replace: true });
    });
  };

  if (loading) {
    return null;
  }

  // Redirect to /attendance if user is authenticated
  if (isAuthenticated) {
    navigate(Routes.EMPLOYEES, { replace: true });
  }

  return (
    <Container className="h-screen">
      <Flex className="items-center justify-center h-full">
        <Paper
          shadow="xl"
          className="min-w-xl flex items-center justify-center p-10"
        >
          <Flex
            direction={"column"}
            className="items-center justify-center h-full w-full gap-5"
          >
            <Text size={rem(32)} fw={600} lts={"0.025rem"} py={rem(20)}>
              MotorPH Payroll
            </Text>
            <form
              onSubmit={form.onSubmit((values) => {
                handleLogin(values.username, values.password);
              })}
              className="w-full flex flex-col gap-5"
            >
              <Flex direction={"column"} className="w-full gap-3">
                <TextInput
                  size="md"
                  placeholder="Username"
                  {...form.getInputProps("username")}
                />
                <PasswordInput
                  size="md"
                  placeholder="Password"
                  {...form.getInputProps("password")}
                />
              </Flex>
              <Button size="md" fullWidth type="submit">
                Login
              </Button>
            </form>
          </Flex>
        </Paper>
      </Flex>
    </Container>
  );
}

export default Login;
