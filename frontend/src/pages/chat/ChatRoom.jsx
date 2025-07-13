// src/pages/chat/ChatRoom.jsx
import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { fetchChatMessages, sendMessage } from "../../services/chat";

export default function ChatRoom() {
  const { roomId } = useParams();
  const navigate = useNavigate();

  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const loadMessages = async () => {
      try {
        const data = await fetchChatMessages(roomId);
        setMessages(data);
        console.log("불러온 메시지 목록:", data);
      } catch (err) {
        console.error("채팅 메시지 불러오기 실패", err);
        alert("채팅 메시지를 불러오는 데 실패했습니다.");
      }
    };
    loadMessages();
  }, [roomId]);

  const handleSend = async (e) => {
    e.preventDefault();
    if (!input.trim()) return;

    const userMessage = {
      content: input,
      sender: "USER",
    };

    setMessages((prev) => [...prev, userMessage]);
    setInput("");
    setLoading(true);

    try {
      const newMessages = await sendMessage(roomId, input);
      console.log("전송 후 새 메시지 목록:", newMessages);
      setMessages((prev) => [...prev.slice(0, -1), ...newMessages]);
    } catch (err) {
      console.error("메시지 전송 실패", err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-white relative flex justify-center items-center font-['SejongGeulggot']">
      {/* 배경 원 */}
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

        <h2 className="text-xl font-bold mb-4 text-center">채팅방</h2>

        <div className="bg-white p-4 rounded-lg shadow-md h-[60vh] overflow-y-auto mb-4">
          {messages.map((msg, index) => (
            <div
              key={msg.messageId || msg.id || index}
              className={`mb-2 ${
                msg.sender === "USER" ? "text-right" : "text-left"
              }`}
            >
              <span
                className={`inline-block px-4 py-2 rounded-lg ${
                  msg.sender === "USER" ? "bg-blue-200" : "bg-gray-300"
                }`}
              >
                {msg.content.split("\n").map((line, idx) => (
                  <React.Fragment key={idx}>
                    {line}
                    <br />
                  </React.Fragment>
                ))}
              </span>
            </div>
          ))}
          {loading && (
            <div className="text-center text-gray-500 italic mb-2">응답을 기다리는 중...</div>
          )}
        </div>

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
