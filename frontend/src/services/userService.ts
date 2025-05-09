import { BackendRoutes } from "../constants/backendRoutes";
import axiosInstance from "../factories/axiosInstance";
import { userCredentials } from "../types/login";
import { APIResponse, FetchedUser } from "../types/responses";

export const loginApi = async (
  username: userCredentials["username"],
  password: userCredentials["password"]
) => {
  const response = axiosInstance
    .post<APIResponse<FetchedUser>>(BackendRoutes.LOGIN, {
      username,
      password,
    })
    .then((res) => {
      if (res.status != 200) {
        throw new Error("Failed to login");
      }

      return res.data;
    });

  return response;
};

export const authenticateApi = async () => {
  const response = axiosInstance
    .post<APIResponse<FetchedUser>>(BackendRoutes.AUTHENTICATE)
    .then((res) => {
      if (res.status != 200) {
        throw new Error("Failed to authenticate");
      }

      return res.data;
    });

  return response;
};

export const logoutApi = async () => {
  const response = axiosInstance
    .post<APIResponse<null>>(BackendRoutes.LOGOUT)
    .then((res) => {
      if (res.status != 200) {
        throw new Error("Failed to logout");
      }

      return res.data;
    });

  return response;
};
