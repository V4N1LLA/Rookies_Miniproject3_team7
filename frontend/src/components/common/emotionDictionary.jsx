const emotionDictionary = {
    SAD: { label: "슬픔", color: "#6DD0F0", emoji: "😭" },
    ANGRY: { label: "화남", color: "#FE4E4E", emoji: "😡" },
    HAPPY: { label: "행복함", color: "#FFC548", emoji: "😁" },
    FEAR: { label: "불안", color: "#F88642", emoji: "😳" },
    TENSE: { label: "긴장됨", color: "#634490", emoji: "😯" },
    TIRED: { label: "피로함", color: "#008478", emoji: "😫" },
    BORED: { label: "지루함", color: "#9747FF", emoji: "🥱" },
    EMBARR: { label: "당황스러움", color: "#FE76B4", emoji: "😵‍💫" },
    HAP: { label: "행복" },
    ANG: { label: "분노" },
    FEA: { label: "불안" },
    POSITIVE: { label: "POSITIVE" },
    NEGATIVE: { label: "NEGATIVE" },
};

export function getKoreanEmotion(emotionCode) {
    return emotionDictionary[emotionCode] || { label: "", color: "#CCCCCC", emoji: "🫥" };
}