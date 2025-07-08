import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Calendar from "./pages/diary/Calendar";
import DiaryWrite from "./pages/diary/DiaryWrite";
import DiaryDetail from "./pages/diary/DiaryDetail";
import "./App.css";
import "./index.css";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/diary" element={<Calendar />} />
        <Route path="/diary/DiaryWrite" element={<DiaryWrite />} />
        <Route path="/diary/DiaryDetail/:id" element={<DiaryDetail />} />
      </Routes>
    </Router>
  );
}

export default App;
