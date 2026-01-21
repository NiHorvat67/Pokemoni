import Nav from "./components/Nav"
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom"

import Homepage from "./pages/Homepage"
import ErrorPage from "./pages/ErrorPage"
import Profile from "./pages/Profile"
import Auth from "./pages/Auth"
import Advertisement from "./pages/Advertisement"
import Admin from "./pages/Admin"
import NewAdvertisement from "./pages/NewAdvertisement"
import Settings from "./pages/Settings"

import useAuthContext from "./hooks/useAuthContext"


function App() {

  const { user } = useAuthContext()
  // zabraniti auth rute ako je user vec ulogiran. kasnije zbog lakseg testiranja
  return (
    <BrowserRouter>
      <main className="relative">
        <Nav />
        <Routes>
          <Route path="/" element={<Homepage />} />
          <Route path="/error" element={<ErrorPage />} />
          <Route path="/profile/:userId" element={<Profile />} />
          <Route path="settings" element={user ? <Settings /> : <Navigate to="/auth" replace />} />
          <Route path="/auth/:step?" element={user ? <Navigate to="/" replace /> : <Auth />} />
          <Route path="/admin" element={user?.accountRole === "admin" ? <Admin /> : <Navigate to="/" replace />} />
          <Route path="/new-advertisement" element={user ? (user.accountRole === "trader" ? <NewAdvertisement /> : <Navigate to="/" replace />) : <Navigate to="/auth" replace />} />
          <Route path="/advertisement/:advertisementId" element={<Advertisement />} />
          <Route path="*" element={<Navigate to="/error" replace />} />
        </Routes>
      </main>
    </BrowserRouter>
  )
}

export default App
