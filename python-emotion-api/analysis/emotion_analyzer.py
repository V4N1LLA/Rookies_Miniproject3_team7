from transformers import pipeline, AutoTokenizer, AutoModel
import numpy as np
from datetime import datetime
import torch

# ✅ 감정 분석 모델
classifier = pipeline(
    "text-classification",
    model="beomi/KcELECTRA-base",
    return_all_scores=True
)

# ✅ KoSBERT 임베딩 모델
tokenizer = AutoTokenizer.from_pretrained("jhgan/ko-sbert-nli")
model = AutoModel.from_pretrained("jhgan/ko-sbert-nli")

def get_sentence_embedding(text: str) -> list:
    inputs = tokenizer(text, return_tensors="pt", truncation=True, padding=True)
    with torch.no_grad():
        outputs = model(**inputs)
    embedding = outputs.last_hidden_state[:, 0, :]  # [CLS] token
    return embedding.squeeze().tolist()

# ✅ 라벨 매핑
label_map = {
    "기쁨": "HAPPY",
    "불안": "FEAR",
    "당황": "EMBARR",
    "분노": "ANGRY",
    "슬픔": "SAD",
    "상처": "TIRED",
    "중립": "TENSE"
}

# ✅ 감정 보정 계수
adjustment = {
    "HAPPY": 2.0,
    "SAD": 1.2,
    "ANGRY": 1.0,
    "FEAR": 1.0,
    "TIRED": 1.0,
    "TENSE": 0.2,
    "EMBARR": 1.0
}

# ✅ 키워드 기반 감정 가산
keyword_boost = {
    "SAD": ["눈물", "무거웠", "힘들었", "우울", "외로웠", "서러웠", "위로"],
    "HAPPY": ["기쁘", "행복", "웃었", "즐거웠", "좋았", "만족"],
    "ANGRY": ["화났", "짜증", "억울", "분노", "열받", "짜증나"],
    "FEAR": ["불안", "무서웠", "두려웠", "겁났"],
    "TIRED": ["지쳤", "피곤", "기운 없", "힘 빠졌", "힘들었"],
    "EMBARR": ["당황", "창피", "부끄", "민망"],
    "TENSE": ["긴장", "불편", "신경", "초조"]
}

# ✅ 감정 우선순위 그룹
emotion_priority = ["SAD", "ANGRY", "FEAR", "TIRED", "HAPPY", "EMBARR", "TENSE"]

def analyze_emotion(content: str) -> dict:
    results = classifier(content)[0]

    # 기본 감정 점수 집계
    emotion_scores = {}
    for item in results:
        raw_label = item["label"]
        score = item["score"]
        domain_emotion = label_map.get(raw_label, "TENSE")
        adjusted_score = score * adjustment.get(domain_emotion, 1.0)
        emotion_scores[domain_emotion] = emotion_scores.get(domain_emotion, 0.0) + adjusted_score

    # 키워드 가산
    for emo, keywords in keyword_boost.items():
        if any(kw in content for kw in keywords):
            emotion_scores[emo] = emotion_scores.get(emo, 0.0) + 0.3

    # top 감정 판단: 가장 우선순위 높은 감정 중 최고 점수
    sorted_emotions = sorted(
        emotion_scores.items(),
        key=lambda x: (-x[1], emotion_priority.index(x[0]) if x[0] in emotion_priority else 999)
    )
    top_emotion = sorted_emotions[0][0]

    # 실제 임베딩 벡터 추출
    vector = get_sentence_embedding(content)
    dim = len(vector)

    return {
        "top_emotion": top_emotion,
        "scores": {k: round(v, 4) for k, v in emotion_scores.items()},
        "vector": vector,
        "dim": dim,
        "created_at": datetime.utcnow()
    }
