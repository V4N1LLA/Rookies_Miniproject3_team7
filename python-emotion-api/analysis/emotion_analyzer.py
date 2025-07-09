from transformers import pipeline, AutoTokenizer, AutoModel
from datetime import datetime
import torch
import re
import os

from config.emotion_config import label_map, adjustment, keyword_boost, emotion_priority, all_emotions

# ✅ 라벨 → 한국어 감정명
label_name_map = {
    "LABEL_0": "분노",
    "LABEL_1": "불안",
    "LABEL_2": "기쁨",
    "LABEL_3": "다정",
    "LABEL_4": "슬픔"
}

# ✅ 감정 분류 모델 로딩 (koBERT-Senti5 with trust_remote_code)
classifier = pipeline(
    "text-classification",
    model="jeonghyeon97/koBERT-Senti5",
    tokenizer="monologg/kobert",  # 커스텀 tokenizer 사용
    trust_remote_code=True,
    top_k=None
)

# ✅ KoSBERT 임베딩 모델 로딩
embedding_tokenizer = AutoTokenizer.from_pretrained("jhgan/ko-sbert-nli")
embedding_model = AutoModel.from_pretrained("jhgan/ko-sbert-nli")

def get_sentence_embedding(text: str) -> list:
    inputs = embedding_tokenizer(text, return_tensors="pt", truncation=True, padding=True)
    with torch.no_grad():
        outputs = embedding_model(**inputs)
    cls_embedding = outputs.last_hidden_state[:, 0, :]
    return cls_embedding.squeeze().tolist()

def analyze_emotion(content: str) -> dict:
    """주어진 일기 내용에 대해 감정 분석 수행"""
    results = classifier(content)[0]
    emotion_scores = {}

    for item in results:
        label = item["label"]
        kor_emotion = label_name_map.get(label, "불안")
        domain_emotion = label_map.get(kor_emotion, "TENDER")
        score = item["score"]
        adjusted = score * adjustment.get(domain_emotion, 1.0)
        emotion_scores[domain_emotion] = emotion_scores.get(domain_emotion, 0.0) + adjusted

    # ✅ 키워드 보정
    for emo, keywords in keyword_boost.items():
        if any(re.search(rf"\\b{re.escape(kw)}\\b", content) for kw in keywords):
            emotion_scores[emo] = emotion_scores.get(emo, 0.0) + 0.3

    complete_scores = {emo: round(emotion_scores.get(emo, 0.0), 4) for emo in all_emotions}

    sorted_emotions = sorted(
        complete_scores.items(),
        key=lambda x: (-x[1], emotion_priority.index(x[0]) if x[0] in emotion_priority else 999)
    )
    top_emotion = sorted_emotions[0][0]

    vector = get_sentence_embedding(content)

    return {
        "top_emotion": top_emotion,
        "scores": complete_scores,
        "vector": vector,
        "dim": len(vector),
        "created_at": datetime.utcnow()
    }