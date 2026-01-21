import { createContext, useEffect, useReducer } from "react";
import { useMutation } from "@tanstack/react-query";
import axios from "axios";

interface AuthState {
  user: any | null
}

interface AuthContextType extends AuthState {
  dispatch: any
}

export const AuthContext = createContext<AuthContextType>({ user: null, dispatch: undefined })


const authReducer = (state: any, action: { payload: any, type: "LOGIN" | "LOGOUT" }) => {
  switch (action.type) {
    case "LOGIN":
      localStorage.setItem("user", JSON.stringify(action.payload))
      return { user: action.payload }
    case "LOGOUT":
      localStorage.removeItem("user")
      return { user: null }
    default:
      return { user: null }
  }
}


export const AuthContextProvider = ({ children }: { children: React.ReactNode }) => {

  const user = JSON.parse(localStorage.getItem("user") || "null")
  const [state, dispatch] = useReducer(authReducer, { user: user })

  const { mutate } = useMutation({
    mutationFn: async () => {
      return axios.get("/api/accounts/current")
        .then(res => {
          return res.data
        })
        .catch(err => {
          console.log(err)
          throw new Error(err)
          // throwa error da se onsucces ne runa
        })
    },
    onSuccess: result => {
      dispatch({ type: "LOGIN", payload: result })
    }
  })

  useEffect(() => {
    const user = JSON.parse(localStorage.getItem("user") || "null")
    // user storean u localstorage da se ne mora svaki puta raditi request na /accounts/current
    if (user) {
      dispatch({ type: "LOGIN", payload: user })
    } else {
      mutate()
    }

  }, [])


  return (
    <AuthContext.Provider value={{ dispatch, ...state }}>
      {children}
    </AuthContext.Provider >
  )

}
