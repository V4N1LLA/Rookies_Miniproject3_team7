import React from "react";
import { useLocation, useNavigate } from "react-router-dom";
import "../../index.css"; // bg-paper가 정의되어 있는 CSS
import axios from "axios";
import Toast from "../../components/common/Alert";
import { createDiary } from "../../services/diary";

function DiaryWrite() {
  const location = useLocation();
  const navigate = useNavigate();
  const params = new URLSearchParams(location.search);
  const date = params.get("date");
  const [year, month, day] = date ? date.split("-") : [];
  const [selectedWeather, setSelectedWeather] = React.useState(null);
  const [title, setTitle] = React.useState("");
  const [content, setContent] = React.useState("");
  const [showToast, setShowToast] = React.useState(false);

  const handleBack = () => {
    navigate("/diary");
  };
  const handleSubmit = async () => {
    if (!title || !content) {
      alert("제목, 내용을 모두 입력해주세요.");
      return;
    }

    try {
      // 중복 일기 체크
      const fetchDiaries = (await import("../../services/diary")).fetchDiaries;
      const existingDiaries = await fetchDiaries();
      const isDuplicate = existingDiaries.some(
        (entry) => entry.timestamp.slice(0, 10) === date
      );
      if (isDuplicate) {
        alert("해당 날짜에 이미 작성된 일기가 있습니다.");
        navigate("/diary");
        return;
      }

      await createDiary({
        timestamp: `${date}T00:00:00`, // ISO 포맷에 맞게 조정
        title: title,
        content: content,
      });

      setShowToast(true);

      setTimeout(() => {
        setShowToast(false);
        navigate(`/diary`);
      }, 2000);
    } catch (err) {
      console.error("일기 등록 실패:", err);
      if (err.status === 409) {
        alert("해당 날짜에 이미 작성된 일기가 있습니다.");
        navigate("/diary");
      } else {
        alert("일기 등록에 실패했습니다.");
      }
    }
  };

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
        {/* 제목 입력 */}
        <input
          type="text"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          placeholder="제목을 입력하세요"
          className="w-full mb-4 border border-gray-300 text-[#222222] rounded-lg px-6 py-3 font-['SejongGeulggot'] text-[20px] bg-white/80 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-gray-600"
        />

        {/* 일기 입력 */}
        <textarea
          rows="10"
          value={content}
          onChange={(e) => setContent(e.target.value)}
          className="w-full border focus:outline-none focus:ring-2 focus:ring-gray-600 border-gray-300 text-[#222222] rounded-lg p-6 font-['SejongGeulggot'] text-[20px] leading-relaxed bg-white/50 bg-[repeating-linear-gradient(to_bottom,transparent,transparent_35px,#ccc_35px,#ccc_36px)]"
          placeholder="오늘의 이야기를 적어보세요..."
        />

        <div className="flex justify-center gap-6 mt-6">
          <button
            onClick={handleBack}
            className="w-[200px] bg-gray-500 text-white px-6 py-3 rounded font-['SejongGeulggot'] text-[18px] shadow-[0_4px_4px_3px_rgba(0,0,0,0.25)]"
          >
            돌아가기
          </button>
          <button
            onClick={handleSubmit}
            className="w-[200px] bg-[#F5C451] text-white font-semibold px-6 py-3 rounded font-['SejongGeulggot'] text-[18px] hover:bg-yellow-400 shadow-[0_4px_4px_3px_rgba(0,0,0,0.25)]"
          >
            오늘의 일기 끝.
          </button>
        </div>
      </div>
      {showToast && (
        <Toast
          message="일기가 등록되었습니다!"
          onClose={() => setShowToast(false)}
        />
      )}
    </div>
  );
}

export default DiaryWrite;
