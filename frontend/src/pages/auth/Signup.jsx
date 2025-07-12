// src/pages/auth/Signup.jsx
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { signup } from "../../services/auth";

export default function Signup() {
  const [email, setEmail] = useState("");
  const [name, setName] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleSignup = async (e) => {
    e.preventDefault();

    try {
      const resBody = await signup(email, name, password);
      alert("회원가입이 완료되었습니다!");
      navigate("/login");
    } catch (error) {
      alert("회원가입에 실패했습니다.");
      console.error(error);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center px-4">
      <div className="bg-[#F5F5F5] w-full max-w-md rounded-2xl shadow-lg p-8 border border-gray-200 font-['SejongGeulggot']">
        {/* 감정/날씨 아이콘 */}
        <div className="flex justify-around items-center mb-6 border-b border-gray-300 pb-4">
          <img src="/icons/sun.svg" alt="sun" className="w-6 h-6" />
          <img src="/icons/cloud rain.svg" alt="cloud" className="w-6 h-6" />
          <img src="/icons/sun.svg" alt="sun" className="w-6 h-6" />
          <img src="/icons/cloud rain.svg" alt="rain" className="w-6 h-6" />
          <img src="/icons/cloud rain.svg" alt="rain" className="w-6 h-6" />
        </div>

        <h2 className="text-center text-xl font-bold mb-3">회원가입</h2>

        <form onSubmit={handleSignup} className="space-y-4">
          <div>
            <label className="block mb-1 text-sm text-gray-700">이름</label>
            <input
              type="text"
              placeholder="이름을 입력하세요."
              className="w-full px-4 py-2 border rounded-md bg-white focus:outline-none focus:ring-2 focus:ring-blue-400 shadow-sm"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
            />
          </div>

          <div>
            <label className="block mb-1 text-sm text-gray-700">이메일</label>
            <input
              type="email"
              placeholder="이메일을 입력하세요."
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
              placeholder="비밀번호를 입력하세요."
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
        {/* 로그인 돌아가기 링크 */}
        <p className="mt-6 text-l text-center text-gray-600">
          계정이 있으신가요?{" "}
          <span
            className="text-blue-600 font-semibold underline cursor-pointer"
            onClick={() => navigate("/login")}
          >
            로그인
          </span>
        </p>
      </div>
    </div>
  );
}
