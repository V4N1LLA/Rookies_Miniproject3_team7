import axios from "axios";
const API_AUTH_BASE_URL = import.meta.env.VITE_API_AUTH_BASE_URL;

export const login = async (email, password) => {
  const response = await axios.post(
    `${API_AUTH_BASE_URL}/login`,
    {
      email,
      password,
    },
    {
      headers: {
        "Content-Type": "application/json",
      },
    }
  );
  return response.data;
};

export const signup = async (email, name, password) => {
  try {
    const response = await axios.post(
      `${API_AUTH_BASE_URL}/signup`,
      {
        email,
        name,
        password,
      },
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    return response.data;
  } catch (error) {
    throw error;
  }
};
