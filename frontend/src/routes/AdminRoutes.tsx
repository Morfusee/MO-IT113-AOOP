import { useMemo } from "react";
import userController from "../controllers/userController";
import { isAdmin } from "../utils/permissionUtils";
import { Navigate, Outlet } from "react-router";
import { Routes } from "../constants/routes";

function AdminRoutes() {
  const { getUser } = userController();

  const memoizedIsAdmin = useMemo(() => {
    return isAdmin(getUser()?.employmentInfo.position);
  }, [getUser]);

  return memoizedIsAdmin ? <Outlet /> : <Navigate to={Routes.ATTENDANCE} />;
}

export default AdminRoutes;
