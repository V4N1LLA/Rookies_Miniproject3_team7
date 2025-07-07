# python-emotion-api/app.py
from fastapi import FastAPI
from pydantic import BaseModel
from typing import List
from datetime import datetime

app = FastAPI()

# 요청 형식
class EmotionRequest(BaseModel):
    content: str

# 응답 형식
class EmotionResponse(BaseModel):
    domainEmotion: str
    scores: dict
    vector: List[float]
    dim: int
    createdAt: datetime

@app.post("/analyze", response_model=EmotionResponse)
def analyze_emotion(request: EmotionRequest):
    content = request.content

    # 실제 분석 모델은 추후 교체
    dummy_scores = {
        "SAD": 4.8,
        "FATIGUE": 2.1,
        "LONELY": 1.9
    }
    dummy_vector = [0.1, 0.2, -0.1, 0.05, 0.3]  # 예시

    return {
        "domainEmotion": "SAD",
        "scores": dummy_scores,
        "vector": dummy_vector,
        "dim": len(dummy_vector),
        "createdAt": datetime.utcnow()
    }
