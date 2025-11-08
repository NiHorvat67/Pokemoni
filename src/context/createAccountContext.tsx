import { createContext, useState } from "react";

export const CreateAccountContext = createContext({})

export const CreateAccountContextProvider = ({ children }: { children: any }) => {

  const [userData, setUserData] = useState({
    userFirstName: "sigma",
    userLastName: "boy",
    userLocation: "sigma street",
    accountRole: "",
    userContact: "123",
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


