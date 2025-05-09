import { useEffect, useState } from "react";
import userController from "../controllers/userController";

function useAuth() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);
  const { authenticateUser } = userController();

  useEffect(() => {
    authenticateUser()
      .then((response) => {
        setIsAuthenticated(response);
        setLoading(false);
      })
      .catch((error) => {
        setIsAuthenticated(false);
        setLoading(false);
      });
  }, []);

  return { isAuthenticated, loading };
}

export default useAuth;
