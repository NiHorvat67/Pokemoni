import Nav from "./components/Nav"
import { BrowserRouter, Routes, Route } from "react-router-dom"
import { QueryClient, QueryClientProvider, useQuery } from "@tanstack/react-query"

import Homepage from "./pages/Homepage"
import ErrorPage from "./pages/ErrorPage"
import TraderProfile from "./pages/TraderProfile"
import Auth from "./pages/auth/Auth"


function App() {

  const queryClient = new QueryClient()

  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <main className="relative">
          <Nav />
          <Routes>
            <Route path="/" element={<Homepage />}></Route>
            <Route path="/error" element={<ErrorPage />}></Route>
            <Route path="/profile/:userId" element={<TraderProfile />}></Route>
            <Route path="/auth" element={<Auth />}></Route>
          </Routes>
        </main>
      </BrowserRouter>
    </QueryClientProvider>
  )
}

export default App
