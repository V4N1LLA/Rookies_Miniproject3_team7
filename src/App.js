// src/App.js

import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";

import Login from "./pages/auth/Login";
import Signup from "./pages/auth/Signup";

import "./index.css"; // Tailwind CSS 포함되어 있어야 함

function App() {
  return (
    <Router>
      <Routes>
        {/* 기본 경로 접속 시 → 로그인으로 이동 */}
        <Route path="/" element={<Navigate to="/login" />} />

        {/* 로그인 / 회원가입 라우트 */}
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
      </Routes>
    </Router>
  );
}

export default App;
