import React from "react";

function Toast({ message, onClose }) {
  return (
    <div className="fixed bottom-6 left-1/2 transform -translate-x-1/2 bg-black/80 text-white px-6 py-3 rounded-lg shadow-lg z-50 font-['SejongGeulggot'] text-[16px] animate-fadeInUp">
      {message}
      <button
        onClick={onClose}
        className="ml-4 text-sm underline hover:text-yellow-300"
      >
        닫기
      </button>
    </div>
  );
}

function LoadingToast({ message = "로딩중 ..." }) {
  return (
    <div className="fixed bottom-6 left-1/2 transform -translate-x-1/2 bg-black/80 text-white px-6 py-3 rounded-lg shadow-lg z-50 font-['SejongGeulggot'] text-[16px] animate-fadeInUp">
      {message}
    </div>
  );
}

export { Toast, LoadingToast };
