import React from "react";
import { useNavigate } from "react-router-dom";

const Header = () => {
  const navigate = useNavigate();
  const token = localStorage.getItem("token");

  const handleLogout = () => {
    if (!window.confirm("로그아웃 할까요?")) return;
    localStorage.removeItem("token");
    navigate("/");
  };

  if (!token) return null;

  return (
    <header className="w-full px-6 pt-6 flex justify-between items-center font-['SejongGeulggot'] z-50 max-w-screen-xl mx-auto">
      {/* 왼쪽 로고 영역 */}
      <div
        className="text-[25px] text-[#6DD0F0] font-extrabold font-erica stroke-[0.5px] cursor-pointer"
        onClick={() => navigate("/")}
      >
        Moodiary
      </div>

      {/* 오른쪽 버튼 영역 */}
      <div className="space-x-4 flex flex-wrap justify-end items-center">
        <button
          onClick={() => navigate("/diary")}
          className="text-[20px] text-gray-700  hover:underline"
        >
          다이어리
        </button>
        <button
          onClick={() => navigate("/chat")}
          className="text-[20px] text-gray-700 hover:underline"
        >
          채팅
        </button>
        <button
          onClick={handleLogout}
          className="text-[20px] text-red-500 hover:underline"
        >
          로그아웃
        </button>
      </div>
    </header>
  );
};

export default Header;
