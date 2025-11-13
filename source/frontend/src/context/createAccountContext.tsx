import { createContext, useState } from "react";

export const CreateAccountContext = createContext({})

export const CreateAccountContextProvider = ({ children }: { children: any }) => {

  const [userData, setUserData] = useState({
    userFirstName: "",
    userLastName: "",
    userLocation: "",
    accountRole: "",
    userContact: "",
  })

  function setUserDataField(field: any) {
    return function (value: any) {
      const newUserData = { ...userData }
      newUserData[field] = value
      setUserData(newUserData)
    }
  }

  return (
    <CreateAccountContext.Provider value={{ userData, setUserDataField }}>
      {children}
    </CreateAccountContext.Provider>
  );
}


