import React, { useState } from "react";

const emotionMap = {
  ANGRY: { color: "#FE4E4E", emoji: "😡" },
  HAPPY: { color: "#FFC548", emoji: "😁" },
  SAD: { color: "#6DD0F0", emoji: "😭" },
  FEAR: { color: "#F88642", emoji: "😳" },
  TENDER: { color: "#008478", emoji: "🫠" },
};

function EmotionBubble({ emotion }) {
  const [showEmoji, setShowEmoji] = useState(false);

  const { color, emoji } = emotionMap[emotion?.toUpperCase()] || {
    color: "#CCCCCC",
    emoji: "🫥",
  };

  return (
    <div
      className="w-[60px] aspect-square rounded-full flex items-center justify-center cursor-pointer relative group"
      style={{ backgroundColor: color }}
    >
      <span className="absolute z-50 opacity-0 group-hover:opacity-100 text-[30px] animate-bounce transition-opacity duration-300">
        {emoji}
      </span>
    </div>
  );
}

export default EmotionBubble;
