import React from "react";
import "./App.css";
import "./index.css";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./pages/auth/Login";
import Signup from "./pages/auth/Signup";
import Calendar from "./pages/diary/Calendar";
import DiaryWrite from "./pages/diary/DiaryWrite";
import DiaryDetail from "./pages/diary/DiaryDetail";
import MainPage from "./pages/Main";
import Header from "./components/layout/Header";

function App() {
  return (
    <div className="min-h-screen bg-emotion-dots">
      <Router>
        <Header></Header>
        <Routes>
          <Route path="/" element={<MainPage />} />

          {/* 로그인 / 회원가입 라우트 */}
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />

          {/* 다이어리 라우트 */}
          <Route path="/diary" element={<Calendar />} />
          <Route path="/diary/DiaryWrite" element={<DiaryWrite />} />
          <Route path="/diary/DiaryDetail/:id" element={<DiaryDetail />} />
        </Routes>
      </Router>
    </div>
  );
}

export default App;
