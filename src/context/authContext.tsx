import { createContext, useEffect, useReducer } from "react";
import { useMutation } from "@tanstack/react-query";
import axios from "axios";

export const AuthContext = createContext({})


const authReducer = (state: any, action: any) => {
  switch (action.type) {
    case "LOGIN":
      return { user: action.payload }
    case "LOGOUT":
      // api call za delete JSESSIONID cookiea
      return { user: null }
    default:
      console.log("default")
      return { user: null }
  }
}


export const AuthContextProvider = ({ children }: { children: any }) => {

  const [state, dispatch] = useReducer(authReducer, { user: null })

  const { mutate } = useMutation({
    mutationFn: async () => {
      return axios.get("/api/accounts/current")
        .then(res => {
          console.log(res.data)
          return res.data
        })
        .catch(err => {
          console.log(err)
          throw new Error(err)
          // throwa error da se onsucces ne runa
        })
    },
    onSuccess: result => {
      console.log(result)
      dispatch({ type: "LOGIN", payload: result })
    }
  })

  useEffect(() => {
    mutate()

  }, [])


  return (
    <AuthContext.Provider value={{ dispatch, ...state }}>
      {children}
    </AuthContext.Provider >
  )

}
