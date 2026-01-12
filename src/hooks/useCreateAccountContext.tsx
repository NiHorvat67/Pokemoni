import { useContext } from "react";
import { CreateAccountContext } from "@/context/createAccountContext";



const useCreateAccountContext = () => {
  const context = useContext(CreateAccountContext)
  if (!context) {
    throw Error("useCreateAccountContext must be used within CreateAccountContext")
  }
  return context
}

export default useCreateAccountContext
