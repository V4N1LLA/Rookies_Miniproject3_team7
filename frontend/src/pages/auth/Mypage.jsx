import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const API_BASE_URL = import.meta.env.VITE_API_ANALYSIS_BASE_URL;

function Mypage() {
  const [analysisData, setAnalysisData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [visibleCount, setVisibleCount] = useState(5);
  const navigate = useNavigate();

  // 감정 분석 데이터 조회
  const fetchAnalysisData = async () => {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get(`${API_BASE_URL}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setAnalysisData(response.data.data);
      setLoading(false);
    } catch (error) {
      console.error("감정 분석 데이터 조회 실패:", error);
      setError("데이터를 불러오는데 실패했습니다.");
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAnalysisData();
  }, []);

  // 감정 통계 계산
  const calculateEmotionStats = () => {
    const emotionCounts = {};
    analysisData.forEach((item) => {
      const emotion = item.domainEmotion;
      emotionCounts[emotion] = (emotionCounts[emotion] || 0) + 1;
    });
    return emotionCounts;
  };

  // 감정 한글 매핑
  const emotionLabels = {
    HAPPY: "행복",
    SAD: "슬픔",
    ANGRY: "화남",
    FEAR: "두려움",
    SURPRISE: "놀람",
    DISGUST: "혐오",
    NEUTRAL: "중립",
  };

  // 감정별 색상
  const emotionColors = {
    HAPPY: "bg-yellow-400",
    SAD: "bg-blue-400",
    ANGRY: "bg-red-400",
    FEAR: "bg-purple-400",
    SURPRISE: "bg-orange-400",
    DISGUST: "bg-green-400",
    NEUTRAL: "bg-gray-400",
  };

  const emotionStats = calculateEmotionStats();
  const sortedAnalysisData = [...analysisData].sort(
    (a, b) => new Date(b.createdAt) - new Date(a.createdAt)
  );

  if (loading || error) {
    return (
      <div className="w-full max-w-6xl pb-16 px-8 pt-[80px] min-h-[calc(100vh-80px)] flex items-center justify-center">
        <div
          className={`text-lg font-['SejongGeulggot'] ${
            error ? "text-red-500" : ""
          }`}
        >
          {error ? error : "로딩 중..."}
        </div>
      </div>
    );
  }

  return (
    <div className="w-full max-w-6xl py-16 px-8  min-h-[calc(100vh-80px)]">
      <div className="w-full mx-auto">
        {/* 헤더 */}
        <div className="text-center mb-8">
          <p className="text-gray-600 text-2xl font-['SejongGeulggot']">
            나의 감정 분석 결과를 확인해보세요
          </p>
        </div>

        {/* 통계 카드 */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mb-8">
          <div className="bg-white rounded-lg shadow-md p-6">
            <h3 className="text-lg font-semibold mb-2">총 분석 횟수</h3>
            <p className="text-3xl font-bold text-blue-600">
              {analysisData.length}회
            </p>
          </div>

          <div className="bg-white rounded-lg shadow-md p-6">
            <h3 className="text-lg font-semibold mb-2">가장 많은 감정</h3>
            {Object.keys(emotionStats).length > 0 ? (
              <div className="flex items-center">
                <span
                  className={`w-4 h-4 rounded-full ${
                    emotionColors[
                      Object.keys(emotionStats).reduce((a, b) =>
                        emotionStats[a] > emotionStats[b] ? a : b
                      )
                    ]
                  } mr-2`}
                ></span>
                <p className="text-xl font-bold">
                  {
                    emotionLabels[
                      Object.keys(emotionStats).reduce((a, b) =>
                        emotionStats[a] > emotionStats[b] ? a : b
                      )
                    ]
                  }
                </p>
              </div>
            ) : (
              <p className="text-gray-500">데이터가 없습니다</p>
            )}
          </div>

          <div className="bg-white rounded-lg shadow-md p-6">
            <h3 className="text-lg font-semibold mb-2">감정 다양성</h3>
            <p className="text-3xl font-bold text-green-600">
              {Object.keys(emotionStats).length}가지
            </p>
          </div>
        </div>

        {/* 감정별 분포 차트 */}
        <div className="bg-white rounded-lg shadow-md p-6 mb-8">
          <h3 className="text-xl font-semibold mb-4">감정별 분포</h3>
          <div className="space-y-3">
            {Object.entries(emotionStats).map(([emotion, count]) => (
              <div key={emotion} className="flex items-center">
                <div className="w-24 text-sm font-medium">
                  {emotionLabels[emotion]}
                </div>
                <div className="flex-1 bg-gray-200 rounded-full h-4 mx-4">
                  <div
                    className={`h-4 rounded-full ${emotionColors[emotion]}`}
                    style={{
                      width: `${(count / analysisData.length) * 100}%`,
                    }}
                  ></div>
                </div>
                <div className="w-12 text-sm font-medium text-right">
                  {count}회
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* 최근 분석 결과 */}
        <div className="bg-white rounded-lg shadow-md p-6">
          <h3 className="text-xl font-semibold mb-4">최근 분석 결과</h3>
          <div className="space-y-4">
            {sortedAnalysisData.slice(0, visibleCount).map((item, index) => (
              <div
                key={item.analysisId}
                onClick={() => navigate(`/diary/DiaryDetail/${item.diaryId}`)}
                className="border-b border-gray-200 pb-4 last:border-b-0 cursor-pointer hover:bg-gray-50 transition"
              >
                <div className="flex items-center justify-between mb-2">
                  <div className="flex items-center">
                    <span
                      className={`w-3 h-3 rounded-full ${
                        emotionColors[item.domainEmotion]
                      } mr-2`}
                    ></span>
                    <span className="font-medium">
                      {emotionLabels[item.domainEmotion]}
                    </span>
                  </div>
                  <span className="text-sm text-gray-500">
                    분석 날짜: {new Date(item.createdAt).toLocaleDateString()}
                  </span>
                </div>
                <p className="text-gray-700 text-sm leading-relaxed">
                  {item.message.split('\n').map((line, i) => (
                    <React.Fragment key={i}>
                      {line}
                      <br />
                    </React.Fragment>
                  ))}
                </p>
              </div>
            ))}
          </div>

          {analysisData.length > visibleCount && (
            <div className="text-center mt-4">
              <button
                onClick={() => setVisibleCount((prev) => prev + 5)}
                className="text-blue-600 hover:text-blue-800 font-medium"
              >
                더보기 ↓
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default Mypage;
