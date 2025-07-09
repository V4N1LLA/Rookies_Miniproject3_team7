import React from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";

function DiaryDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [diary, setDiary] = React.useState(null);
  const [loading, setLoading] = React.useState(true);
  const token = localStorage.getItem("token");

  React.useEffect(() => {
    const fetchDiary = async () => {
      try {
        const res = await axios.get(`http://localhost:8080/api/diaries/${id}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        setDiary(res.data.data);
      } catch (err) {
        console.error("다이어리 불러오기 실패:", err);
        setDiary(null);
      } finally {
        setLoading(false);
      }
    };

    if (token) fetchDiary();
  }, [id, token]);

  const handleBack = () => {
    navigate("/diary");
  };

  const handleAnalysis = () => {};

  if (loading) {
    return (
      <div className="min-h-screen flex justify-center items-center py-12 px-4">
        <div className="w-full max-w-5xl relative">
          <div className="h-[30px] bg-[#F5C451] rounded-t-2xl shadow-[0_4px_4px_rgba(0,0,0,0.25)] absolute top-0 left-0 right-0 z-30" />
          <div className="bg-paper pt-[60px] max-w-5xl bg-white/70 shadow-md rounded-b-xl p-10 relative flex flex-col items-center justify-center">
            <div className="text-gray-500 text-center font-['SejongGeulggot'] text-[25px]">
              일기찾는중 ...{" "}
            </div>
          </div>
        </div>
      </div>
    );
  }

  if (!diary) {
    return (
      <div className="min-h-screen flex justify-center items-center py-12 px-4">
        <div className="w-full max-w-5xl relative">
          <div className="h-[30px] bg-[#F5C451] rounded-t-2xl shadow-[0_4px_4px_rgba(0,0,0,0.25)] absolute top-0 left-0 right-0 z-30" />
          <div className="bg-paper pt-[60px] max-w-5xl bg-white/70 shadow-md rounded-b-xl p-10 relative">
            <h2 className="font-['SejongGeulggot'] text-[25px] font-bold mb-4">
              다이어리 없음
            </h2>
            <div className="mb-4 text-gray-700 font-['SejongGeulggot']">
              {diary?.timestamp &&
                new Date(diary.timestamp).toLocaleDateString("ko-KR", {
                  year: "numeric",
                  month: "2-digit",
                  day: "2-digit",
                  weekday: "long",
                })}
            </div>
            <p className="text-gray-500 mb-4 font-['SejongGeulggot']">
              해당 날짜에 작성된 다이어리가 없습니다.
            </p>
            <button
              onClick={handleBack}
              className="bg-gray-500 text-white px-4 py-2 rounded mr-2 font-['SejongGeulggot'] shadow-[0_4px_4px_3px_rgba(0,0,0,0.25)]"
            >
              돌아가기
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen flex justify-center items-center py-12 px-4">
      <div className="w-full max-w-5xl relative">
        <div className="h-[30px] bg-[#F5C451] rounded-t-2xl shadow-[0_4px_4px_rgba(0,0,0,0.25)] absolute top-0 left-0 right-0 z-30" />
        <div className="bg-paper pt-[60px] max-w-5xl bg-white/70 shadow-md rounded-b-xl p-10 relative">
          <div className="flex items-center justify-between mb-4">
            {/* <h2 className="font-['SejongGeulggot'] text-[25px] font-bold">다이어리</h2> */}
            <div className="flex items-center space-x-2">
              <span className="text-2xl">{diary.mood}</span>
              <span className="text-xl">{diary.weather}</span>
            </div>
          </div>

          <div className="mb-4 font-['SejongGeulggot'] text-[20px] text-gray-700">
            {diary?.timestamp &&
              new Date(diary.timestamp).toLocaleDateString("ko-KR", {
                year: "numeric",
                month: "2-digit",
                day: "2-digit",
                weekday: "long",
              })}
          </div>

          <div className="mb-6">
            <div className="w-full mb-4 text-[#222222] rounded-lg px-6 py-3 font-['SejongGeulggot'] text-[25px] bg-white/50 bg-[repeating-linear-gradient(to_bottom,transparent,transparent_35px,#ccc_35px,#ccc_36px)] whitespace-pre-wrap">
              제목: {diary.title}
            </div>

            <div className="p-6 min-h-[300px] font-['SejongGeulggot'] text-[20px] leading-relaxed bg-white/50 bg-[repeating-linear-gradient(to_bottom,transparent,transparent_35px,#ccc_35px,#ccc_36px)] rounded-lg text-gray-800 whitespace-pre-wrap">
              {diary.content}
            </div>
          </div>

          <div className="flex justify-center gap-4">
            <button
              onClick={handleBack}
              className="bg-gray-500 text-white px-4 py-2 rounded font-['SejongGeulggot'] shadow-[0_4px_4px_3px_rgba(0,0,0,0.25)]"
            >
              돌아가기
            </button>
            <button
              onClick={handleAnalysis}
              className="bg-[#F5C451] text-white px-4 py-2 rounded font-['SejongGeulggot'] shadow-[0_4px_4px_3px_rgba(0,0,0,0.25)]"
            >
              오늘 나의 무드는?
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default DiaryDetail;
