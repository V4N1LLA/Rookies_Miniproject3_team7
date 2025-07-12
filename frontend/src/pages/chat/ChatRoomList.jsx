import React from "react";
import { useNavigate } from "react-router-dom";
import { startChatSession } from "../../services/chat";

export default function ChatRoomList() {
  const navigate = useNavigate();

  const handleStartChat = async () => {
    try {
      const sessionId = await startChatSession();
      navigate(`/chat/${sessionId}`);
    } catch (error) {
      console.error("❌ 채팅 세션 생성 실패:", error);
      alert("채팅 세션을 시작할 수 없습니다.");
    }
  };

  return (
    <div className="min-h-screen bg-white relative overflow-hidden flex justify-center items-center font-['SejongGeulggot']">
      <div className="absolute top-0 left-0 w-full h-full pointer-events-none overflow-hidden z-0">
        <div className="absolute w-24 h-24 bg-pink-200 opacity-40 rounded-full top-[10%] left-[15%]" />
        <div className="absolute w-16 h-16 bg-blue-200 opacity-30 rounded-full top-[50%] left-[60%]" />
        <div className="absolute w-20 h-20 bg-yellow-200 opacity-30 rounded-full top-[80%] left-[25%]" />
      </div>

      <div className="z-10 bg-[#f8f8f8] rounded-3xl shadow-2xl p-10 w-[600px] border border-gray-300 text-center">
        <h2 className="text-2xl font-bold mb-6">AI 채팅 시작하기</h2>
        <button
          onClick={handleStartChat}
          className="px-6 py-3 bg-yellow-400 text-white rounded-xl font-semibold shadow-md hover:bg-yellow-500 transition"
        >
          새로운 채팅 시작
        </button>
      </div>
    </div>
  );
}
