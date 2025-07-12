import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Toast } from "../../components/common/Alert";
import { login } from "../../services/auth";

export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showToast, setShowToast] = React.useState(false);
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const resBody = await login(email, password);
      const { token, user } = resBody.data;
      localStorage.setItem("token", token);
      localStorage.setItem("user", JSON.stringify(user));
      console.log("토큰:", token);
      setShowToast(true);

      setTimeout(() => {
        setShowToast(false);
        navigate(`/diary`);
      }, 1000);
    } catch (error) {
      alert(error.message || "로그인 중 오류가 발생했습니다.");
      console.error(error);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center px-4">
      <div className="bg-[#F5F5F5] w-full max-w-md rounded-2xl shadow-lg p-8 border border-gray-200 font-['SejongGeulggot']">
        {/* 감정/날씨 아이콘 */}
        <div className="flex justify-center gap-4 mb-6 border-b border-gray-200 pb-4">
          <img src="/icons/sun.svg" alt="sun" className="w-6 h-6" />
          <img src="/icons/cloud rain.svg" alt="cloud" className="w-6 h-6" />
          <img src="/icons/sun.svg" alt="rain" className="w-6 h-6" />
          <img src="/icons/cloud rain.svg" alt="heavy rain" className="w-6 h-6" />
          <img src="/icons/cloud rain.svg" alt="heavy rain" className="w-6 h-6" />
        </div>

        {/* 로그인 타이틀 */}
        <h2 className="text-center text-xl font-bold mb-3 text-gray-800">로그인</h2>

        {/* 로그인 입력 폼 */}
        <form onSubmit={handleLogin} className="space-y-5">
          <div>
            <label className="block mb-2 text-m text-gray-700">이메일</label>
            <input
              type="email"
              placeholder="이메일을 입력하세요."
              className="w-full px-4 py-2 border border-gray-300 rounded-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-400 text-m"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>

          <div>
            <label className="block mb-2 text-m text-gray-700">비밀번호</label>
            <input
              type="password"
              placeholder="비밀번호를 입력하세요."
              className="w-full px-4 py-2 border border-gray-300 rounded-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-400 text-m"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>

          <button
            type="submit"
            className="text-m w-full bg-blue-500 text-white py-2 rounded-lg font-semibold shadow hover:bg-blue-600 transition mt-6"
          >
            로그인
          </button>
        </form>

        {/* 회원가입 링크 */}
        <p className="mt-6 text-m text-center text-gray-600">
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
