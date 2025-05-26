import { createContext } from "react";

export const MyUserContext = createContext();
export const MyDispatchContext = createContext();
export const NotificationContext = createContext({
  unreadCount: 0,
  setUnreadCount: () => {},
});