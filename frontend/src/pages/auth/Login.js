import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import Toast from "../../components/common/Alert";

export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showToast, setShowToast] = React.useState(false);
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const res = await fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, password }),
      });

      if (res.ok) {
        const data = await res.json();
        localStorage.setItem("token", data.token);
        setShowToast(true);

        setTimeout(() => {
          setShowToast(false);
          navigate(`/diary`);
        }, 1000);
      } else {
        alert("이메일 또는 비밀번호가 올바르지 않습니다.");
      }
    } catch (error) {
      alert("로그인 중 오류가 발생했습니다.");
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
          <img src="/icons/sun.svg" alt="rain" className="w-6 h-6" />
          <img
            src="/icons/cloud rain.svg"
            alt="heavy rain"
            className="w-6 h-6"
          />
          <img
            src="/icons/cloud rain.svg"
            alt="heavy rain"
            className="w-6 h-6"
          />
        </div>

        {/* 로그인 타이틀 */}
        <h2 className="text-center text-xl font-bold mb-4">로그인</h2>

        {/* 로그인 입력 폼 */}
        <form onSubmit={handleLogin} className="space-y-4">
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
            로그인
          </button>
        </form>

        {/* 회원가입 링크 */}
        <p className="mt-6 text-sm text-center text-gray-600">
          계정이 없으신가요?{" "}
          <span
            className="text-blue-600 font-semibold underline cursor-pointer"
            onClick={() => navigate("/signup")}
          >
            회원가입
          </span>
        </p>
      </div>
      {showToast && (
        <Toast message="로그인 성공!" onClose={() => setShowToast(false)} />
      )}
    </div>
  );
}
