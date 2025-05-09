import { authenticateApi, loginApi, logoutApi } from "../services/userService";
import { useUserStore } from "../stores/useUserStore";
import { userCredentials } from "../types/login";

function userController() {
  const selectedUser = useUserStore((state) => state.currentUser);
  const setCurrentUser = useUserStore((state) => state.setCurrentUser);

  const authenticateUser = async (): Promise<boolean> => {
    try {
      const authenticateUserResponse = await authenticateApi();

      setCurrentUser(authenticateUserResponse.data.employee);

      return authenticateUserResponse.status == 200;
    } catch (error) {
      console.log(error);
      return false;
    }
  };

  const loginUser = async (
    username: userCredentials["username"],
    password: userCredentials["password"]
  ): Promise<boolean> => {
    try {
      const loginResponse = await loginApi(username, password);

      console.log(loginResponse);

      setCurrentUser(loginResponse.data.employee);

      return loginResponse.status == 200;
    } catch (error) {
      console.log(error);
      return false;
    }
  };

  const logoutUser = async (): Promise<boolean> => {
    try {
      const logoutResponse = await logoutApi();

      return logoutResponse.status == 200;
    } catch (error) {
      console.log(error);
      return false;
    }
  };

  const getUser = () => {
    return selectedUser;
  };

  return {
    authenticateUser,
    loginUser,
    logoutUser,
    getUser,
  };
}

export default userController;
