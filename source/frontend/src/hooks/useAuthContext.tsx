import { useContext } from "react";
import { AuthContext } from "@/context/authContext";


const useAuthContext = () => {
  const context = useContext(AuthContext)
  if (!context) {
    throw Error("useAuthContext must be used within AuthContext")
  }
  return context
}

export default useAuthContext
