// src/pages/chat/ChatRoomList.jsx
import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { fetchChatRooms } from "../../services/chat";

export default function ChatRoomList() {
  const [rooms, setRooms] = useState([]);

  useEffect(() => {
    const loadRooms = async () => {
      try {
        const data = await fetchChatRooms();
        setRooms(data);
      } catch (err) {
        console.error("채팅방 불러오기 실패", err);
      }
    };
    loadRooms();
  }, []);

  return (
    <div className="min-h-screen bg-white relative overflow-hidden flex justify-center items-center font-['SejongGeulggot']">
      {/* 배경 원 */}
      <div className="absolute top-0 left-0 w-full h-full pointer-events-none overflow-hidden z-0">
        <div className="absolute w-24 h-24 bg-pink-200 opacity-40 rounded-full top-[10%] left-[15%]" />
        <div className="absolute w-16 h-16 bg-blue-200 opacity-30 rounded-full top-[50%] left-[60%]" />
        <div className="absolute w-20 h-20 bg-yellow-200 opacity-30 rounded-full top-[80%] left-[25%]" />
      </div>

      {/* 채팅방 카드 */}
      <div className="z-10 bg-[#f8f8f8] rounded-3xl shadow-2xl p-10 w-[600px] border border-gray-300">
        <h2 className="text-2xl font-bold text-center mb-6">채팅방 목록</h2>
        <ul className="space-y-4">
          {rooms.map((room) => (
            <li key={room.id}>
              <Link
                to={`/chat/${room.id}`}
                className="block w-full text-center p-4 bg-white rounded-xl shadow hover:bg-yellow-100 transition border border-gray-200"
              >
                {room.name}
              </Link>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
}
