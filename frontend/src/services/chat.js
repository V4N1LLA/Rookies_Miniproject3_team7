// src/services/chat.js
import axios from "axios";

const API_CHAT_BASE_URL = import.meta.env.VITE_API_CHAT_BASE_URL;

const getAuthHeader = () => ({
  Authorization: `Bearer ${localStorage.getItem("token")}`,
});

// 채팅방 목록
export const fetchChatRooms = async () => {
  try {
    const response = await axios.get(`${API_CHAT_BASE_URL}/rooms`, {
      headers: getAuthHeader(),
    });
    return response.data.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

// 메시지 목록
export const fetchChatMessages = async (roomId) => {
  try {
    const response = await axios.get(`${API_CHAT_BASE_URL}/rooms/${roomId}/messages`, {
      headers: getAuthHeader(),
    });
    return response.data.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

// 메시지 전송
export const sendMessage = async (roomId, message) => {
  try {
    const response = await axios.post(
      `${API_CHAT_BASE_URL}/rooms/${roomId}/messages`,
      { message },
      { headers: getAuthHeader() }
    );
    return response.data.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};
