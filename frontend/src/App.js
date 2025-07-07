import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Calendar from "./pages/diary/Calendar";
import DiaryWrite from "./pages/diary/DiaryWrite";
import "./App.css";
import "./index.css";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/diary" element={<Calendar />} />
        <Route path="/diary/DiaryWrite" element={<DiaryWrite />} />
      </Routes>
    </Router>
  );
}

export default App;
