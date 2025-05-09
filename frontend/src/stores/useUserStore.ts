import { create } from "zustand";
import { devtools } from "zustand/middleware";
import { IEmployee } from "../types/employee";

interface IUserState {
  currentUser: IEmployee | null;
}

interface IUserActions {
  setCurrentUser: (user: IEmployee) => void;
}

export const useUserStore = create<IUserState & IUserActions>()(
  devtools(
    (set) => ({
      currentUser: null,

      setCurrentUser: (user) => set(() => ({ currentUser: user })),
    }),
    {
      name: "userStore",
    }
  )
);
