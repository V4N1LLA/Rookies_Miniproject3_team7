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
    <header className="w-full px-4 py-2 flex justify-end items-center font-['SejongGeulggot'] z-50">
      <div className="space-x-6">
        <button
          onClick={() => navigate("/diary")}
          className="text-lg text-gray-700 hover:underline"
        >
          다이어리
        </button>
        <button
          onClick={() => navigate("/chat")}
          className="text-lg text-gray-700 hover:underline"
        >
          채팅
        </button>
        <button
          onClick={handleLogout}
          className="text-lg text-red-500 hover:underline"
        >
          로그아웃
        </button>
      </div>
    </header>
  );
};

export default Header;
