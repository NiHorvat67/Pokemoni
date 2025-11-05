import Nav from "./components/Nav"
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom"
import { QueryClient, QueryClientProvider, useQuery } from "@tanstack/react-query"

import Homepage from "./pages/Homepage"
import ErrorPage from "./pages/ErrorPage"
import TraderProfile from "./pages/TraderProfile"
import Auth from "./pages/Auth"
import Advertisement from "./pages/Advertisement"
import Admin from "./pages/Admin"
import NewAdvertisement from "./pages/NewAdvertisement"
import { AuthContextProvider } from "./context/authContext"

import useAuthContext from "./hooks/useAuthContext"


function App() {

  const queryClient = new QueryClient()
  const { user } = useAuthContext()
  // zabraniti auth rute ako je user vec ulogiran. kasnije zbog lakseg testiranja

  return (
    <QueryClientProvider client={queryClient}>
      <AuthContextProvider>
        <BrowserRouter>
          <main className="relative">
            <Nav />
            <Routes>
              <Route path="/" element={<Homepage />}></Route>
              <Route path="/error" element={<ErrorPage />}></Route>
              <Route path="/profile/:userId" element={<TraderProfile />}></Route>
              <Route path="/auth/:step?" element={<Auth />}></Route>
              <Route path="/admin" element={<Admin />}></Route>
              <Route path="/new-advertisement" element={<NewAdvertisement />}></Route>
              <Route path="/advertisement/:advertisementId" element={<Advertisement />}></Route>
              <Route path="/advertisement/:advertisementId" element={<Advertisement />}></Route>
              <Route path="*" element={<Navigate to="/error" replace />}></Route>
            </Routes>
          </main>
        </BrowserRouter>
      </AuthContextProvider>
    </QueryClientProvider>
  )
}

export default App
