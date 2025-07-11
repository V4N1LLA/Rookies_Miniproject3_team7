import { create } from "zustand";

export const diaryStore = create((set) => ({
  diary: null,
  analysisMap: {}, // { [id]: true }
  analysisDataMap: {}, // { [id]: data }
  currentDiaryId: null,
  setCurrentDiaryId: (id) => set({ currentDiaryId: id }),

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
  resetAnalysisFor: (id) =>
    set((state) => ({
      analysisMap: { ...state.analysisMap, [id]: false },
      analysisDataMap: { ...state.analysisDataMap, [id]: null },
    })),
  clearAllAnalysis: () =>
    set({
      analysisMap: {},
      analysisDataMap: {},
    }),
  resetAnalysis: () =>
    set((state) => ({
      analysisMap: {
        ...state.analysisMap,
        [state.currentDiaryId]: false,
      },
      analysisDataMap: {
        ...state.analysisDataMap,
        [state.currentDiaryId]: null,
      },
    })),
}));
