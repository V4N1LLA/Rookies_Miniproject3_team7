import React from "react";
import { useNavigate } from "react-router-dom";

const Header = () => {
  const token = localStorage.getItem("token");
  const navigate = useNavigate();

  const handleLogout = () => {
    if (!window.confirm("로그아웃 할까요?")) return;
    localStorage.removeItem("token");
    navigate("/");
  };

  return (
    <header
      style={{
        // paddingTop: "5px",
        //background: "#f5f5f5",
        display: "flex",
        justifyContent: "flex-end",
      }}
    >
      {token && (
        <button
          onClick={handleLogout}
          style={{ padding: "0.5rem 1rem", cursor: "pointer" }}
        >
          Logout
        </button>
      )}
    </header>
  );
};

export default Header;
