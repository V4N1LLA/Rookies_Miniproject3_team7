import axios from "axios";
const token = localStorage.getItem("token");
const API_BASE_URL = "http://localhost:8080/api/diaries";

export const fetchDiaries = async () => {
  try {
    const response = await axios.get(API_BASE_URL);
    return response.data.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

export const createDiary = async (diary) => {
  try {
    const response = await axios.post(API_BASE_URL, diary);
    return response.data.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

export const fetchDiaryById = async (id) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/${id}`);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

export const deleteDiary = async (id) => {
  try {
    const response = await axios.delete(`${API_BASE_URL}/${id}`);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};
