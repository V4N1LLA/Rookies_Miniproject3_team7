import React from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import { fetchDiaryById, deleteDiary } from "../../services/diary";
import { getKoreanEmotion } from "../../components/common/emotionDictionary";

function DiaryDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [diary, setDiary] = React.useState(null);
  const [loading, setLoading] = React.useState(true);
  const token =
    "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLsobDsnYDsp4AiLCJpYXQiOjE3NTIwNDI2MDQsImV4cCI6MTc1MjEyOTAwNH0.hX7rizanl6bw4f2rMRlotR4f7sibnGhG7n8FMQ-dHxo";

  const [analysis, setAnalysis] = React.useState(null);
  const [analyzing, setAnalyzing] = React.useState(false);

  React.useEffect(() => {
    const fetchDiary = async () => {
      try {
        const data = await fetchDiaryById(id);
        console.log("일기 데이터:", data);
        setDiary(data);
        if (data.analysisResult) {
          setAnalysis(data.analysisResult);
        }
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

  const handleDelete = async () => {
    if (!window.confirm("정말 삭제하시겠습니까?")) return;

    try {
      await deleteDiary(id);
      alert("일기가 삭제되었습니다.");
      navigate("/diary");
    } catch (error) {
      console.error("일기 삭제 실패:", error);
      alert("일기 삭제에 실패했습니다.");
    }
  };

  const handleAnalysis = async () => {
    setAnalyzing(true);
    try {
      const response = await axios.post(
        `http://localhost:8080/api/diaries/${id}/analyze`,
        {},
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      setAnalysis(response.data.data);
    } catch (error) {
      console.error("감정 분석 실패:", error);
      alert("감정 분석에 실패했습니다.");
    } finally {
      setAnalyzing(false);
    }
  };

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

         {analysis && (() => {
           const emotion = getKoreanEmotion(analysis.domainEmotion);
           return (
             <div
               className="mb-6 p-4 rounded-lg text-gray-800 font-['SejongGeulggot']"
               style={{ backgroundColor: emotion.color + "CC" }}
             >
               <div className="text-xl mb-2 font-semibold">감정 분석 결과</div>
               <div>
                 감정: {emotion.label} {emotion.emoji}
               </div>
               <div>감정 세기 (dim): {analysis.dim}</div>
             </div>
           );
         })()}

          <div className="flex justify-center gap-4">
            <button
              onClick={handleBack}
              className="bg-gray-500 text-white px-4 py-2 rounded font-['SejongGeulggot'] shadow-[0_4px_4px_3px_rgba(0,0,0,0.25)]"
            >
              돌아가기
            </button>
            <button
              onClick={handleDelete}
              className="bg-red-500 text-white px-4 py-2 rounded font-['SejongGeulggot'] shadow-[0_4px_4px_3px_rgba(0,0,0,0.25)]"
            >
              삭제하기
            </button>
            {!analysis &&
              (analyzing ? (
                <button
                  disabled
                  className="bg-gray-300 text-white px-4 py-2 rounded font-['SejongGeulggot'] shadow"
                >
                  분석 중...
                </button>
              ) : (
                <button
                  onClick={handleAnalysis}
                  className="bg-[#F5C451] text-white px-4 py-2 rounded font-['SejongGeulggot'] shadow-[0_4px_4px_3px_rgba(0,0,0,0.25)]"
                >
                  오늘 나의 무드는?
                </button>
              ))}
          </div>
        </div>
      </div>
    </div>
  );
}

export default DiaryDetail;
