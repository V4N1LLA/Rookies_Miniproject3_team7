// src/pages/chat/ChatRoom.jsx
import React, { useState, useEffect, useRef } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { sendMessage, sendFeedback } from "../../services/chat";

export default function ChatRoom() {
  const { sessionId } = useParams();
  console.log("✅ 현재 세션 ID:", sessionId);
  const navigate = useNavigate();

  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSend = async (e) => {
    e.preventDefault();
    if (!input.trim()) return;

    try {
      setLoading(true);
      console.log("✉️ 메시지 전송 요청:", input);
      const user = localStorage.getItem("user");
      console.log("📦 POST 요청 내용:", {
        sessionId,
        sender: user,
        content: input,
      });
      if (!sessionId) {
        alert("세션 ID가 유효하지 않습니다.");
        return;
      }

      const newMsg = await sendMessage(sessionId, user, input);
      console.log("📨 메시지 응답 데이터:", newMsg);
      setMessages((prev) => [
        ...prev,
        {
          id: newMsg.messageId,
          sender: newMsg.sender,
          text: newMsg.content,
        },
      ]);
      setInput("");
      // 자동 피드백 요청
      console.log("🧠 자동 피드백 요청 시작: messageId =", newMsg.messageId);
      const feedback = await sendFeedback(newMsg.messageId, "HELPFUL");

      console.log("✅ 자동 피드백 응답:", feedback);
      console.log("📩 피드백 전송 완료:", feedback);
    } catch (err) {
      console.error("❌ 피드백 전송 실패:", err);
      alert("메시지 전송 실패: " + (err?.message || "오류 발생"));
    } finally {
      setLoading(false);
    }
  };

  const user = localStorage.getItem("user");
  return (
    <div className="min-h-screen bg-white relative flex justify-center items-center font-['SejongGeulggot']">
      <div className="absolute top-0 left-0 w-full h-full pointer-events-none z-0">
        <div className="absolute w-24 h-24 bg-pink-200 opacity-40 rounded-full top-[10%] left-[15%]" />
        <div className="absolute w-16 h-16 bg-blue-200 opacity-30 rounded-full top-[50%] left-[60%]" />
        <div className="absolute w-20 h-20 bg-yellow-200 opacity-30 rounded-full top-[80%] left-[25%]" />
      </div>

      <div className="z-10 bg-[#f8f8f8] rounded-3xl shadow-2xl p-8 w-[650px] border border-gray-300">
        <button
          onClick={() => navigate("/chat")}
          className="text-sm text-blue-600 mb-4 hover:underline"
        >
          ← 채팅방 목록으로
        </button>

        <h2 className="text-xl font-bold mb-4 text-center">세션 {sessionId}</h2>

        <div className="bg-white p-4 rounded-lg shadow-md h-[60vh] overflow-y-auto mb-4">
          {messages.map((msg) => (
            <div
              key={msg.id}
              className={`mb-2 ${
                msg.sender === user ? "text-right" : "text-left"
              }`}
            >
              <span
                className={`inline-block px-4 py-2 rounded-lg ${
                  msg.sender === user
                    ? "bg-blue-200 text-black"
                    : "bg-gray-300 text-black"
                }`}
              >
                {msg.text}
              </span>
            </div>
          ))}
        </div>

        {loading && (
          <p className="text-sm text-gray-500 text-center mb-2">
            응답을 기다리는 중...
          </p>
        )}

        <form onSubmit={handleSend} className="flex gap-2">
          <input
            type="text"
            className="flex-grow border border-gray-400 rounded px-3 py-2"
            placeholder="메시지를 입력하세요"
            value={input}
            onChange={(e) => setInput(e.target.value)}
          />
          <button
            type="submit"
            className="bg-blue-500 text-white px-4 py-2 rounded"
          >
            전송
          </button>
        </form>
      </div>
    </div>
  );
}
