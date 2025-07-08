# analysis/emotion_analyzer.py
from transformers import pipeline
import numpy as np
from datetime import datetime

# 감정 분석 모델 초기화
classifier = pipeline("sentiment-analysis", model="distilbert-base-uncased-finetuned-sst-2-english")

def analyze_emotion(content: str) -> dict:
    result = classifier(content)[0]
    label = result["label"]  # 'POSITIVE' or 'NEGATIVE'
    score = result["score"]  # 확률

    # 벡터, dim은 테스트용 랜덤 생성 (향후 임베딩 모델로 대체 가능)
    vector = np.random.rand(5).tolist()
    dim = len(vector)

    # domainEmotion 라벨 정규화
    domain_emotion = "HAPPY" if label == "POSITIVE" else "SAD"

    return {
        "top_emotion": domain_emotion,
        "scores": {label: round(score, 4)},
        "vector": vector,
        "dim": dim,
        "created_at": datetime.utcnow(),
    }
