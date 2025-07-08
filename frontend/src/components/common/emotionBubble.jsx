import React, { useState } from "react";

const emotionMap = {
  angry: { color: "#FE4E4E", emoji: "😡" },
  happy: { color: "#FFC548", emoji: "🤣" },
  sad: { color: "#6DD0F0", emoji: "😭" },
  anxious: { color: "#634490", emoji: "😯" },
  nervous: { color: "#F88642", emoji: "😳" },
  tired: { color: "#008478", emoji: "😫" },
  bored: { color: "#9747FF", emoji: "🥱" },
  embarrassed: { color: "#FE76B4", emoji: "😵‍💫" },
};

function EmotionBubble({ emotion }) {
  const [showEmoji, setShowEmoji] = useState(false);

  const { color, emoji } = emotionMap[emotion] || {
    color: "#CCCCCC",
    emoji: "Unknown",
  };

  return (
    <div
      className="w-[60px] aspect-square rounded-full flex items-center justify-center cursor-pointer relative group"
      style={{ backgroundColor: color }}
    >
      <span className="absolute z-20 opacity-0 group-hover:opacity-100 text-[30px] animate-bounce transition-opacity duration-300">
        {emoji}
      </span>
    </div>
  );
}

export default EmotionBubble;
