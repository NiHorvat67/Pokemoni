import Nav from "./components/Nav"
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom"

import Homepage from "./pages/Homepage"
import ErrorPage from "./pages/ErrorPage"
import TraderProfile from "./pages/TraderProfile"
import Auth from "./pages/Auth"
import Advertisement from "./pages/Advertisement"
import Admin from "./pages/Admin"
import NewAdvertisement from "./pages/NewAdvertisement"

import useAuthContext from "./hooks/useAuthContext"


function App() {

  const { user } = useAuthContext()
  // zabraniti auth rute ako je user vec ulogiran. kasnije zbog lakseg testiranja
  console.log(user)
  return (
    <BrowserRouter>
      <main className="relative">
        <Nav />
        <Routes>
          <Route path="/" element={<Homepage />} />
          <Route path="/error" element={<ErrorPage />} />
          <Route path="/profile/:userId" element={<TraderProfile />} />
          <Route path="/auth/:step?" element={user ? <Navigate to="/" replace /> : <Auth />} />
          <Route path="/admin" element={<Admin />} />
          <Route path="/new-advertisement" element={user ? <NewAdvertisement /> : <Navigate to="/auth" replace />} />
          <Route path="/advertisement/:advertisementId" element={<Advertisement />} />
          <Route path="*" element={<Navigate to="/error" replace />} />
        </Routes>
      </main>
    </BrowserRouter>
  )
}

export default App
