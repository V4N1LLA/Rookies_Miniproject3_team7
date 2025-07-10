# 감정 라벨 매핑
label_map = {
    "기쁨": "HAPPY",
    "불안": "FEAR",
    "분노": "ANGRY",
    "슬픔": "SAD",
    "중립": "TENDER"
}

# 감정 보정 계수
adjustment = {
    "HAPPY": 2.0,
    "SAD": 1.2,
    "ANGRY": 1.0,
    "FEAR": 1.0,
    "TENDER": 0.2,
}

# 감정 키워드 가산
keyword_boost = {
    "SAD": ["눈물", "무거웠", "힘들었", "우울", "외로웠", "서러웠", "위로"],
    "HAPPY": ["기쁘", "행복", "웃었", "즐거웠", "좋았", "만족"],
    "ANGRY": ["화났", "짜증", "억울", "분노", "열받", "짜증나"],
    "FEAR": ["불안", "무서웠", "두려웠", "겁났"],
    "TENDER": ["중립", "평온", "차분", "괜찮", "무던"]
}

# 감정 우선순위 (동점일 때 사용)
emotion_priority = ["SAD", "ANGRY", "FEAR", "HAPPY", "TENDER"]

# 전체 감정 코드 목록
all_emotions = list(adjustment.keys())
