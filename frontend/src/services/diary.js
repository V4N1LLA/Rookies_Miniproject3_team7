import axios from "axios";
const API_BASE_URL = "http://localhost:8080/api/diaries";

export const fetchDiaries = async (token) => {
  try {
    const response = await axios.get(API_BASE_URL, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

export const createDiary = async (diary) => {
  const token = localStorage.getItem("token");
  try {
    const response = await axios.post(API_BASE_URL, diary, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

export const fetchDiaryById = async (id) => {
  const token = localStorage.getItem("token");

  try {
    const response = await axios.get(`${API_BASE_URL}/${id}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

export const deleteDiary = async (id) => {
  const token = localStorage.getItem("token");

  try {
    const response = await axios.delete(`${API_BASE_URL}/${id}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};
