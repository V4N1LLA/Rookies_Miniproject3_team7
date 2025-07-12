import React from "react";
import { useCalendarStore } from "../../store/calendarStore";
import { useNavigate, useLocation } from "react-router-dom";
import "../../index.css";
import EmotionBubble from "../../components/common/emotionBubble";
import { useEffect, useState } from "react";
import { fetchDiaries } from "../../services/diary";
import { ChevronRightIcon } from "@heroicons/react/24/solid";
import { ChevronLeftIcon } from "@heroicons/react/24/solid";
import { LoadingToast } from "../../components/common/Alert";
import { diaryStore } from "../../store/diaryStore";

function getDaysInMonth(year, month) {
  return new Date(year, month, 0).getDate();
}

function isFutureDate(year, month, day) {
  const selected = new Date(year, month - 1, day);
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  return selected > today;
}

function Calendar() {
  const { year, month, setYear, setMonth } = useCalendarStore();
  const navigate = useNavigate();
  const location = useLocation();
  const [diaryDates, setDiaryDates] = useState([]);
  const [loading, setLoading] = useState(false);
  useEffect(() => {
    const token = localStorage.getItem("token");
    setLoading(true);

    fetchDiaries()
      .then((entries) => {
        const dates = entries.map((entry) => ({
          date: entry.timestamp.slice(0, 10),
          id: entry.diary_id,
          emotion: entry.analysis_result?.domain_emotion,
        }));
        setDiaryDates(dates);
      })
      .catch((err) => {
        console.error("다이어리 로딩 실패:", err);
      })
      .finally(() => {
        setLoading(false);
      });
  }, [location.pathname]);

  const changeMonth = (increment) => {
    const newMonth = month + increment;
    if (newMonth > 12) {
      setYear(year + 1);
      setMonth(1);
    } else if (newMonth < 1) {
      setYear(year - 1);
      setMonth(12);
    } else {
      setMonth(newMonth);
    }
  };

  const handleDayClick = (day) => {
    if (!day || isFutureDate(year, month, day)) return;
    const dateStr = `${year}-${String(month).padStart(2, "0")}-${String(
      day
    ).padStart(2, "0")}`;
    const diaryEntry = diaryDates.find((d) => d.date === dateStr);
    if (diaryEntry) {
      navigate(`/diary/DiaryDetail/${diaryEntry.id}`);
    } else {
      navigate(`/diary/DiaryWrite?date=${dateStr}`);
    }
  };

  const daysInMonth = getDaysInMonth(year, month);
  const firstDay = new Date(year, month - 1, 1).getDay();
  const days = [
    ...Array(firstDay).fill(null),
    ...Array.from({ length: daysInMonth }, (_, i) => i + 1),
  ];

  return (
    <>
      {loading && <LoadingToast message="다이어리 불러오는 중..." />}
      <div className="min-h-screen flex justify-center items-center bg-transparent rounded-xl">
        <div className="w-full max-w-5xl relative bg-transparent rounded-xl">
          <div className="h-[30px] bg-[#F5C451] rounded-t-2xl shadow-[0_4px_4px_rgba(0,0,0,0.25)] absolute top-0 left-0 right-0 z-30 " />
          <div className="bg-paper pt-[60px] max-w-5xl shadow-md rounded-xl p-10 relative">
            <div className="absolute left-4 top-1/2 z-20">
              <button
                onClick={() => changeMonth(-1)}
                className="w-7 h-7 text-gray-600"
              >
                <ChevronLeftIcon />
              </button>
            </div>
            <div className="absolute right-4 top-1/2  z-20">
              <button
                onClick={() => changeMonth(1)}
                className="w-7 h-7 text-gray-600"
              >
                <ChevronRightIcon />
              </button>
            </div>
            <div className="text-center tracking-wider text-[25px] pb-4 font-medium mb-4 font-['Noto_Sans_KR','sans-serif'] text-[20px] flex justify-center gap-x-10">
              <span>{year}년</span>
              <span>{String(month).padStart(2, "0")}월</span>
            </div>
            <div className="grid grid-cols-7 justify-items-center gap-y-[20px] text-center text-[25px] font-light font-['Noto_Sans_KR','sans-serif']">
              {" "}
              {["일", "월", "화", "수", "목", "금", "토"].map((d) => (
                <div
                  key={d}
                  className="font-light font-['Noto_Sans_KR','sans-serif']"
                >
                  {d}
                </div>
              ))}
              {days.map((day, idx) => {
                const dateStr = day
                  ? `${year}-${String(month).padStart(2, "0")}-${String(
                      day
                    ).padStart(2, "0")}`
                  : null;
                const diaryEntry =
                  dateStr && diaryDates.find((d) => d.date === dateStr);
                const hasDiary = !!diaryEntry;

                return (
                  <div
                    key={idx}
                    className={`w-[52px] h-[52px] flex items-center justify-center rounded-full cursor-pointer relative
        ${
          day && !isFutureDate(year, month, day)
            ? "hover:ring-2 hover:ring-yellow-400"
            : ""
        }
      `}
                    onClick={() => handleDayClick(day)}
                  >
                    <div className="relative w-full h-full flex items-center justify-center">
                      {" "}
                      <span className="absolute z-40">{day || ""}</span>
                      {hasDiary && (
                        <div className="absolute w-full h-full flex items-center justify-center">
                          <EmotionBubble
                            emotion={diaryEntry.emotion?.toUpperCase()}
                          />
                        </div>
                      )}
                    </div>
                  </div>
                );
              })}
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default Calendar;
