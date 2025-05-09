import { Navigate, Outlet } from "react-router";
import useAuth from "../hooks/useAuth";
import { Box, Loader } from "@mantine/core";

function ProtectedRoute() {
  // Check if the user is authenticated
  const { isAuthenticated, loading } = useAuth();

  if (loading) {
    return (
      <Box className="w-screen h-screen grid place-content-center">
        <Loader />
      </Box>
    );
  }

  return isAuthenticated ? <Outlet /> : <Navigate to="/" replace />;
}

export default ProtectedRoute;
