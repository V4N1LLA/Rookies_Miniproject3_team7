import { create } from "zustand";

export const useCalendarStore = create((set) => ({
  year: new Date().getFullYear(),
  month: new Date().getMonth() + 1,
  setYear: (year) => set({ year }),
  setMonth: (month) => set({ month }),
}));
