import "./App.css";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./pages/auth/Login";
import Signup from "./pages/auth/Signups";

function App() {
  return (
    <Router>
      <Routes>
        {/* <Route path="/" element={<MainPage />} /> */}
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        {/* <Route path="/diary" element={<Calendar />} />
        <Route path="/diary/DiaryWrite" element={<DiaryWrite />} />
        <Route path="/diary/DiaryDetail/:id" element={<DiaryDetail />} /> */}
      </Routes>
    </Router>
  );
}

export default App;
