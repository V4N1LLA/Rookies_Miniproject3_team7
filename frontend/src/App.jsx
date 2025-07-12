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
import ChatRoomList from "./pages/chat/ChatRoomList";
import ChatRoom from "./pages/chat/ChatRoom";

function App() {
  return (
    <Router>
      <div className="min-h-screen flex flex-col justify-start">
        <Routes>
          {/* 로그인 / 회원가입은 헤더 없이 */}
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/" element={<MainPage />} />
          {/* 나머지 페이지는 헤더 포함 */}
          <Route
            path="*"
            element={
              <>
                <Header />
                <div className="flex flex-col justify-center items-center w-full">
                  <Routes>
                    <Route path="/diary" element={<Calendar />} />
                    <Route path="/diary/DiaryWrite" element={<DiaryWrite />} />
                    <Route
                      path="/diary/DiaryDetail/:id"
                      element={<DiaryDetail />}
                    />
                    <Route path="/chat" element={<ChatRoomList />} />
                    <Route path="/chat/:roomId" element={<ChatRoom />} />
                  </Routes>
                </div>
              </>
            }
          />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
