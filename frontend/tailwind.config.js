/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{js,jsx,ts,tsx}"],
  theme: {
    extend: {
      fontFamily: {
        erica: ['"Erica One"', "cursive"],
        noto: ['"Noto Sans KR"', "sans-serif"],
      },
    },
  },
  plugins: [],
};
