import { create } from "zustand";

export const diaryStore = create((set) => ({
  diary: null,
  analysisMap: {}, // { [id]: true }
  analysisDataMap: {}, // { [id]: data }

  setDiary: (diary) => set({ diary }),
  setAnalysisFor: (id, value) =>
    set((state) => ({
      analysisMap: { ...state.analysisMap, [id]: value },
    })),
  setAnalysisDataFor: (id, data) =>
    set((state) => ({
      analysisDataMap: { ...state.analysisDataMap, [id]: data },
      analysisMap: { ...state.analysisMap, [id]: true },
    })),
  clearAllAnalysis: () =>
    set({
      analysisMap: {},
      analysisDataMap: {},
    }),
}));
