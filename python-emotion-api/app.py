from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List, Dict
from datetime import datetime
from dotenv import load_dotenv

from analysis.emotion_analyzer import analyze_emotion
from analysis.message_generator import generate_message

load_dotenv()
app = FastAPI()

class EmotionRequest(BaseModel):
    content: str

class EmotionResponse(BaseModel):
    domain_emotion: str
    scores: Dict[str, float]
    vector: List[float]
    dim: int
    encouragement_message: str
    created_at: datetime

@app.post("/analyze", response_model=EmotionResponse)
def analyze(request: EmotionRequest):
    try:
        emotion_result = analyze_emotion(request.content)
        message = generate_message(request.content, emotion_result["top_emotion"])

        return EmotionResponse(
            domain_emotion=emotion_result["top_emotion"],
            scores=emotion_result["scores"],
            vector=emotion_result["vector"],
            dim=emotion_result["dim"],
            encouragement_message=message,
            created_at=emotion_result["created_at"]
        )
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
