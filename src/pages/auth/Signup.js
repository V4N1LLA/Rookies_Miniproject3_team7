// src/pages/auth/Signup.jsx
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function Signup() {
  const [email, setEmail] = useState("");
  const [nickname, setNickname] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleSignup = async (e) => {
    e.preventDefault();

    try {
      const res = await fetch("http://localhost:8080/api/auth/signup", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, nickname, password }),
      });

      if (res.ok) {
        alert("회원가입이 완료되었습니다!");
        navigate("/login");
      } else {
        alert("회원가입에 실패했습니다.");
      }
    } catch (error) {
      alert("회원가입 중 오류 발생");
      console.error(error);
    }
  };

  return (
    <div className="min-h-screen w-screen bg-[#1C262B] flex justify-center items-center">
      <div className="bg-[#F5F5F5] w-[360px] rounded-2xl shadow-xl p-8 border border-gray-300 font-['SejongGeulggot']">
        
        {/* 감정/날씨 아이콘 */}
        <div className="flex justify-around items-center mb-6 border-b border-gray-300 pb-4">
          <img src="/icons/sun.svg" alt="sun" className="w-6 h-6" />
          <img src="/icons/cloud rain.svg" alt="cloud" className="w-6 h-6" />
          <img src="/icons/sun.svg" alt="sun" className="w-6 h-6" />
          <img src="/icons/cloud rain.svg" alt="rain" className="w-6 h-6" />
          <img src="/icons/cloud rain.svg" alt="rain" className="w-6 h-6" />
        </div>

        <h2 className="text-center text-xl font-bold mb-4">회원가입</h2>

        <form onSubmit={handleSignup} className="space-y-4">
          <div>
            <label className="block mb-1 text-sm text-gray-700">이름</label>
            <input
              type="text"
              placeholder="이름"
              className="w-full px-4 py-2 border rounded-md bg-white focus:outline-none focus:ring-2 focus:ring-blue-400 shadow-sm"
              value={nickname}
              onChange={(e) => setNickname(e.target.value)}
              required
            />
          </div>

          <div>
            <label className="block mb-1 text-sm text-gray-700">이메일</label>
            <input
              type="email"
              placeholder="이메일"
              className="w-full px-4 py-2 border rounded-md bg-white focus:outline-none focus:ring-2 focus:ring-blue-400 shadow-sm"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>

          <div>
            <label className="block mb-1 text-sm text-gray-700">비밀번호</label>
            <input
              type="password"
              placeholder="비밀번호"
              className="w-full px-4 py-2 border rounded-md bg-white focus:outline-none focus:ring-2 focus:ring-blue-400 shadow-sm"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>

          <button
            type="submit"
            className="w-full bg-gray-300 text-black py-2 rounded-md font-semibold shadow-inner hover:bg-gray-400 transition"
          >
            회원가입
          </button>
        </form>
      </div>
    </div>
  );
}
