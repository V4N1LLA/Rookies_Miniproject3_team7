import React from "react";
import { useEffect, useState } from "react";
import "../index.css";

function MainPage() {
  const [showStartButton, setShowStartButton] = useState(false);

  useEffect(() => {
    const timer = setTimeout(() => {
      setShowStartButton(true);
    }, 1000);
    return () => clearTimeout(timer);
  }, []);

  const start = () => {
    const token = localStorage.getItem("token");
    if (token) {
      window.location.href = "/diary";
    } else {
      window.location.href = "/login";
    }
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center text-white font-erica">
      {!showStartButton ? (
        <>
          <h1
            className="text-[52px] sm:text-[72px] md:text-[82px] text-[#6DD0F0] font-extrabold leading-tight tracking-wide font-erica stroke-[0.5px] transition-all duration-1000 transform scale-100 animate-grow"
            style={{ WebkitTextStroke: "0.5px white" }}
          >
            Moodiary
          </h1>
          <p className="text-xl text-black mt-4 font-light tracking-wide font-noto transition-opacity duration-1000">
            How’s your vibe today?
          </p>
        </>
      ) : (
        <>
          <h1
            className="text-[82px] text-[#6DD0F0] font-extrabold leading-tight tracking-wide font-erica stroke-[0.5px] transition-opacity duration-1000"
            style={{ WebkitTextStroke: "0.5px white" }}
          >
            Moodiary
          </h1>
          <button
            className="font-noto mt-8 px-8 py-3 rounded-full bg-gray-200 text-black shadow-md hover:scale-105 transition-transform duration-500"
            onClick={start}
          >
            START
          </button>
        </>
      )}
    </div>
  );
}

export default MainPage;
