// src/services/chat.js
import axios from "axios";

const API_CHAT_BASE_URL = import.meta.env.VITE_API_CHAT_BASE_URL;

const getAuthHeader = () => ({
  Authorization: `Bearer ${localStorage.getItem("token")}`,
});

// 메시지 전송 (새로운 /chat 엔드포인트 사용)
export const sendMessage = async (sessionId, sender, content) => {
  const token = localStorage.getItem("token");
  console.log("sendMessage 토큰: ", token);

  try {
    const response = await axios.post(
      `${API_CHAT_BASE_URL}`,
      { sessionId, sender, content },
      { headers: { Authorization: `Bearer ${token}` } }
    );
    return response.data.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

// 채팅 세션 시작
export const startChatSession = async () => {
  const token = localStorage.getItem("token");
  try {
    const response = await axios.post(
      `${API_CHAT_BASE_URL}/start`, // ex: /ai/chat/start
      null,
      { headers: { Authorization: `Bearer ${token}` } }
    );
    return response.data.data; // sessionId
  } catch (error) {
    throw error.response?.data || error;
  }
};

// 피드백 전송
export const sendFeedback = async (messageId, feedback) => {
  const token = localStorage.getItem("token");

  try {
    const response = await axios.post(
      `${API_CHAT_BASE_URL}/${messageId}/feedback`,
      { feedback },
      {
        headers: { Authorization: `Bearer ${token}` },
      }
    );
    return response.data.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

// 채팅 히스토리 조회
export const fetchChatHistory = async (userId) => {
  const token = localStorage.getItem("token");
  try {
    const response = await axios.get(`${API_CHAT_BASE_URL}/history`, {
      headers: { Authorization: `Bearer ${token}` },
      params: { userId },
    });
    return response.data.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

// 벡터 검색
export const searchVector = async (query, chatMessageId) => {
  try {
    const response = await axios.post(
      `${API_CHAT_BASE_URL}/chat/vector?chatMessageId=${chatMessageId}`,
      query,
      { headers: getAuthHeader() }
    );
    return response.data.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};
