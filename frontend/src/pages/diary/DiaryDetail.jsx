import { useState } from "react";
import React from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import {
  fetchDiaryById,
  deleteDiary,
  requestAnalysisById,
  fetchAnaylsisById,
  fetchEmotionScores,
} from "../../services/diary";
import { getKoreanEmotion } from "../../components/common/emotionDictionary";
import { diaryStore } from "../../store/diaryStore";
import manifest from "../../../dist/manifest.json";

function DiaryDetail() {
  const { id } = useParams();
  const store = diaryStore();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [diary, setDiary] = useState(null);
  const [analyzing, setAnalyzing] = React.useState(false);
  const [emotionScores, setEmotionScores] = useState([]);
  const token = localStorage.getItem("token");

  React.useEffect(() => {
    // Completely reset all analysis state for fresh diary entry
    store.clearAllAnalysis();
    setDiary(null);
    setLoading(true);

    const fetchDiary = async () => {
      try {
        const diaryData = await fetchDiaryById(id);
        setDiary(diaryData);

        // Only fetch analysis result if explicitly marked as analyzed
        if (
          diaryData.analysisResult !== null &&
          diaryData.analysisResult !== undefined &&
          diaryData.analysisResult !== "" // avoid false positives
        ) {
          const result = await fetchAnaylsisById(id);
          store.setAnalysisDataFor(id, result);
          const scores = await fetchEmotionScores(id);
          setEmotionScores(scores);
          store.setAnalysisFor(id, true);
        }
      } catch (err) {
        console.error("다이어리 불러오기 실패:", err);
        setDiary(null);
      } finally {
        setLoading(false);
      }
    };

    if (token) fetchDiary();
  }, [
    id,
    token,
    store.setAnalysisDataFor,
    store.setAnalysisFor,
    store.clearAllAnalysis,
  ]);

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
    const token = localStorage.getItem("token");
    const name = localStorage.getItem("user");

    try {
      console.log(name, "의 토큰 :", token);

      const response = await requestAnalysisById(id);
      console.log("분석 요청 성공:", response.status);

      // 분석이 완료되었을 것이므로 새로고침 → useEffect에서 GET 요청 처리됨
      // 분석 요청 후 바로 결과와 점수 fetch
      const result = await fetchAnaylsisById(id);
      store.setAnalysisDataFor(id, result);
      const scores = await fetchEmotionScores(id);
      setEmotionScores(scores);
      store.setAnalysisFor(id, true);
    } catch (error) {
      console.error("감정 분석 실패:", error);

      if (
        error.response &&
        error.response.status === 400 &&
        error.response.data.message === "이미 분석된 일기입니다."
      ) {
        // 새로고침으로 분석 결과 자동 반영
        window.location.reload();
      } else {
        alert("감정 분석에 실패했습니다.");
      }
    } finally {
      setAnalyzing(false);
    }
  };

  if (loading) {
    return (
      <div className="w-full max-w-6xl py-12 px-4 sm:px-6 lg:px-8 ">
        <div className="w-full relative">
          <div className="w-full relative h-[30px] bg-[#F5C451] rounded-t-2xl shadow-[0_4px_4px_rgba(0,0,0,0.25)] top-0 left-0 right-0 z-30" />
          <div className="bg-paper py-[60px] bg-white/70 shadow-md rounded-b-xl relative flex flex-col items-center justify-center">
            <div className="text-gray-500 text-center font-['SejongGeulggot'] text-[25px]">
              일기 찾는중 ...{" "}
            </div>
          </div>
        </div>
      </div>
    );
  }

  if (!diary) {
    return (
      <div className="w-full max-w-6xl py-12 px-4 sm:px-6 lg:px-8">
        <div className="w-full relative text-center">
          <div className="w-full relative h-[30px] bg-[#F5C451] rounded-t-2xl shadow-[0_4px_4px_rgba(0,0,0,0.25)] top-0 left-0 right-0 z-30" />
          <div className="bg-paper py-[60px] bg-white/70 shadow-md rounded-b-xl relative">
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

  const analysis = store.analysisMap[id];
  const analysisData = store.analysisDataMap[id];

  // 분석 결과 로딩 상태
  const isAnalysisLoading = analysis && (!analysisData || emotionScores.length === 0);

  return (
    <div className="w-full max-w-6xl px-4 sm:px-6 lg:px-8 pb-12 bg-transparent">
      <div className="w-full relative">
        <div className="h-[30px] bg-[#F5C451] rounded-t-2xl shadow-[0_4px_4px_rgba(0,0,0,0.25)] absolute top-0 left-0 right-0 z-30" />
        <div className="bg-paper pt-[60px] bg-white/70 shadow-md rounded-b-xl p-10 relative">
          <div className="flex items-center justify-between mb-4">
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

          {isAnalysisLoading && (
            <div className="mb-6 flex flex-col items-center justify-center">
              <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-[#F5C451] mb-2" />
              <div className="text-gray-500 font-['SejongGeulggot'] text-lg">
                분석 결과 불러오는 중...
              </div>
            </div>
          )}

          {!isAnalysisLoading && analysisData &&
            (() => {
              const emotion = getKoreanEmotion(analysisData.domainEmotion);
              return (
                <div
                  className="mb-4 p-4 rounded-lg text-gray-800 font-noto"
                  style={{ backgroundColor: emotion.color + "CC" }}
                >
                  <div>
                    감정 분석 결과: {emotion.label} {emotion.emoji}
                  </div>
                </div>
              );
            })()}
          {!isAnalysisLoading && analysisData && (
            <div className="mb-6 p-4 rounded-lg bg-black/30 text-gray-800 font-noto text-lg shadow whitespace-pre-line">
              {analysisData.message.split("\n").map((line, idx) => (
                <React.Fragment key={idx}>
                  {line}
                  <br />
                </React.Fragment>
              ))}
            </div>
          )}
          {!isAnalysisLoading && emotionScores.length > 0 && (
            <div className="mb-6 p-4 rounded-lg bg-black/10 text-gray-800 font-noto text-lg shadow">
              <h3 className="text-xl mb-2 font-bold">감정 분석 점수</h3>
              <ul className="list-disc list-inside">
                {emotionScores.map((score, idx) => (
                  <p key={idx}>
                    {getKoreanEmotion(score.code).label}{" "}
                    {getKoreanEmotion(score.code).emoji}:{" "}
                    {score.score.toFixed(2)}
                  </p>
                ))}
              </ul>
            </div>
          )}

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
