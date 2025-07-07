import React from "react";
import { useLocation } from "react-router-dom";
import "../../index.css"; // bg-paper가 정의되어 있는 CSS

function DiaryWrite() {
  const location = useLocation();
  const params = new URLSearchParams(location.search);
  const date = params.get("date");
  const [year, month, day] = date ? date.split("-") : [];
  const [selectedWeather, setSelectedWeather] = React.useState(null);

  return (
    <div className="min-h-screen flex justify-center items-center py-12 px-4">
      <div className="w-full bg-paper  max-w-5xl bg-white/70 shadow-md rounded-xl p-10">
        {/* 날짜 및 날씨 선택 영역 */}
        <div className="flex items-center justify-center gap-20 mb-6">
          <div className="text-[25px] px-2 py-1 tracking-widest font-['SejongGeulggot'] border-b border-gray-400">
            {year}년 {month}월 {day}일
          </div>
          <div className="flex gap-4 text-[25px] font-['SejongGeulggot']">
            {["☀", "🌧", "⛅", "❄"].map((icon, idx) => (
              <button
                key={idx}
                onClick={() => setSelectedWeather(icon)}
                className={`px-4 py-2 rounded-full transition duration-200 ${
                  selectedWeather === icon
                    ? "bg-transparent border-2 border-black/50 shadow-[0_0_6px_2px_rgba(0,0,0,0.2)]"
                    : "bg-transparent hover:bg-white/40"
                }`}
              >
                {icon}
              </button>
            ))}
          </div>
        </div>

        {/* 일기 입력 */}
        <textarea
          rows="10"
          className="w-full border focus:outline-none focus:ring-2 focus:ring-gray-600 border-gray-300 text-[#222222] rounded-lg p-6 font-['SejongGeulggot'] text-[25px] leading-relaxed bg-white/50 bg-[repeating-linear-gradient(to_bottom,transparent,transparent_35px,#ccc_35px,#ccc_36px)]"
          placeholder="오늘의 이야기를 적어보세요..."
        />

        {/* 제출 버튼 */}
        <div className="text-center mt-6 font-['SejongGeulggot']">
          <button className="bg-[#F5C451] text-white font-semibold px-6 py-2 rounded-lg hover:bg-yellow-400 shadow-[0_4px_4px_3px_rgba(0,0,0,0.25)]">
            오늘의 일기 끝.
          </button>
        </div>
      </div>
    </div>
  );
}

export default DiaryWrite;
