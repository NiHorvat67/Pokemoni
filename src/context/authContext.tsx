import { createContext, useEffect, useReducer } from "react";

export const AuthContext = createContext({})

const authReducer = (state: any, action: any) => {
  switch (action.type) {
    case "LOGIN":
      // localStorage.setItem("user", JSON.stringify(action.payload))
      return { user: action.payload }
    case "LOGOUT":
      localStorage.removeItem("user")
      return { user: null }
    default:
      return { user: null }
  }
}


export const AuthContextProvider = ({ children }: { children: any }) => {

  const [state, dispatch] = useReducer(authReducer, { user: null })

  useEffect(() => {
    const user = JSON.parse(localStorage.getItem("user"))
    if (user) {
      dispatch({ type: "LOGIN", payload: user })
    }
  }, [])


  return (
    <AuthContext.Provider value={{ dispatch, ...state }}>
      {children}
    </AuthContext.Provider >
  )

}
