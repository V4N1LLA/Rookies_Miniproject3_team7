// src/pages/chat/ChatRoomList.jsx
import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { fetchChatRooms, startChatSession } from "../../services/chat";

export default function ChatRoomList() {
  const [rooms, setRooms] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadRooms = async () => {
      try {
        const data = await fetchChatRooms();
        setRooms(data);
        console.log("채팅방 목록 불러오기 성공:", data);
      } catch (err) {
        console.error("채팅방 불러오기 실패", err);
        alert("채팅방 목록을 불러오는 데 실패했습니다.");
      } finally {
        setLoading(false);
      }
    };
    loadRooms();
  }, []);

  return (
    <div className="bg-white relative overflow-hidden flex font-['SejongGeulggot']">
      <div className="absolute top-0 left-0 w-full h-full pointer-events-none overflow-hidden z-0">
        <div className="absolute w-24 h-24 bg-pink-200 opacity-40 rounded-full top-[10%] left-[15%]" />
        <div className="absolute w-16 h-16 bg-blue-200 opacity-30 rounded-full top-[50%] left-[60%]" />
        <div className="absolute w-20 h-20 bg-yellow-200 opacity-30 rounded-full top-[80%] left-[25%]" />
      </div>

      <div className="z-10 bg-[#f8f8f8] rounded-3xl shadow-2xl p-10 w-[600px] border border-gray-300">
        <h2 className="text-2xl font-bold text-center mb-6">채팅방 목록</h2>
        <div className="text-right mb-4">
          <button
            onClick={async () => {
              try {
                const sessionId = await startChatSession();
                window.location.href = `/chat/${sessionId}`;
              } catch (error) {
                alert("채팅 세션 시작 중 오류 발생");
                console.error(error);
              }
            }}
            className="bg-yellow-400 hover:bg-yellow-500 text-white font-bold py-2 px-4 rounded"
          >
            ➕ 새로운 채팅 시작하기
          </button>
        </div>
        {loading ? (
          <div className="text-center text-gray-500">
            채팅방 목록을 불러오는 중...
          </div>
        ) : (
          <ul className="space-y-4">
            {rooms.map((room) => (
              <li key={room.id}>
                <Link
                  to={`/chat/${room.id}`}
                  className="block w-full text-center p-4 bg-white rounded-xl shadow hover:bg-yellow-100 transition border border-gray-200"
                >
                  {`💬 ${new Date(room.createdAt).toLocaleString("ko-KR", {
                    year: "numeric",
                    month: "2-digit",
                    day: "2-digit",
                  })} 에 생성한 대화`}
                </Link>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
}
