// src/services/chat.js
import axios from "axios";

const API_CHAT_BASE_URL = import.meta.env.VITE_API_CHAT_BASE_URL;

const getAuthHeader = () => ({
  Authorization: `Bearer ${localStorage.getItem("token")}`,
});

// 채팅방 목록
export const fetchChatRooms = async () => {
  try {
    const response = await axios.get(`${API_CHAT_BASE_URL}/chat/sessions`, {
      headers: getAuthHeader(),
    });
    return response.data.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

// 메시지 목록
export const fetchChatMessages = async (sessionId) => {
  try {
    const response = await axios.get(
      `${API_CHAT_BASE_URL}/chat/messages?sessionId=${sessionId}`,
      {
        headers: getAuthHeader(),
      }
    );
    console.log("📨 fetchChatMessages response:", response);
    return response.data.data;
  } catch (error) {
    console.error("❌ fetchChatMessages error:", error);
    throw error.response?.data || error;
  }
};

// 메시지 전송
export const sendMessage = async (roomId, message) => {
  try {
    const response = await axios.post(
      `${API_CHAT_BASE_URL}/chat`,
      {
        sessionId: parseInt(roomId),
        content: message,
      },
      { headers: getAuthHeader() }
    );
    return response.data.data; // 👈 배열로 받음
  } catch (error) {
    throw error.response?.data || error;
  }
};

// 채팅 세션 시작
export const startChatSession = async () => {
  try {
    const response = await axios.post(`${API_CHAT_BASE_URL}/chat/start`, null, {
      headers: getAuthHeader(),
    });
    return response.data.data; // sessionId 반환
  } catch (error) {
    throw error.response?.data || error;
  }
};

// 채팅 히스토리 조회
export const fetchChatHistory = async () => {
  try {
    const response = await axios.get(`${API_CHAT_BASE_URL}/chat/history`, {
      headers: getAuthHeader(),
    });
    return response.data.data; // history 객체 반환
  } catch (error) {
    throw error.response?.data || error;
  }
};
