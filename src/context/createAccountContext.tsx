import { createContext, useState } from "react";

interface UserDataType {
  userFirstName: string;
  userLastName: string;
  userLocation: string;
  accountRole: string;
  userContact: string;
}

interface CreateAccountContextType {
  userData: UserDataType,
  setUserDataField: <K extends keyof UserDataType>(
    field: K
  ) => (value: UserDataType[K]) => void
}



export const CreateAccountContext = createContext<CreateAccountContextType | undefined>(undefined)

export const CreateAccountContextProvider = ({ children }: { children: any }) => {

  const [userData, setUserData] = useState<UserDataType>({
    userFirstName: "",
    userLastName: "",
    userLocation: "",
    accountRole: "",
    userContact: "",
  })

  function setUserDataField<K extends keyof UserDataType>(field: K) {
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


