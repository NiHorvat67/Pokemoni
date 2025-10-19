import Nav from "./components/Nav"
import { BrowserRouter, Routes, Route } from "react-router-dom"
import Homepage from "./pages/Homepage"
import ErrorPage from "./pages/ErrorPage"

function App() {

  return (
    <BrowserRouter>
      <main className="relative">
        <Nav />
        <Routes>
          <Route path="/" element={<Homepage />}></Route>
          <Route path="/error" element={<ErrorPage />}></Route>
        </Routes>
      </main>
    </BrowserRouter>
  )
}

export default App
