import axios from "axios";
const API_DIARY_BASE_URL = import.meta.env.VITE_API_DIARY_BASE_URL;
const API_ANALYSIS_BASE_URL = import.meta.env.VITE_API_ANALYSIS_BASE_URL;

export const fetchDiaries = async () => {
  const token = localStorage.getItem("token"); // 추가
  console.log("Token:", localStorage.getItem("token"));
  try {
    const response = await axios.get(API_DIARY_BASE_URL, {
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
    const response = await axios.post(API_DIARY_BASE_URL, diary, {
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
    const response = await axios.get(`${API_DIARY_BASE_URL}/${id}`, {
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
    const response = await axios.delete(`${API_DIARY_BASE_URL}/${id}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

export const fetchAnaylsisById = async (id) => {
  const token = localStorage.getItem("token");
  try {
    const response = await axios.get(`${API_ANALYSIS_BASE_URL}/${id}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

export const requestAnalysisById = async (id) => {
  const token = localStorage.getItem("token");
  try {
    const response = await axios.post(
      `${API_ANALYSIS_BASE_URL}/${id}`,
      {},
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    return response;
  } catch (error) {
    throw error.response?.data || error;
  }
};


export const fetchEmotionScores = async (diaryId) => {
  const token = localStorage.getItem("token");
  try {
    const response = await axios.get(`${API_ANALYSIS_BASE_URL}/${diaryId}/scores`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};