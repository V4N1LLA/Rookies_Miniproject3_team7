from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from typing import List, Dict
from analysis.emotion_analyzer import analyze_emotion
from analysis.message_generator import generate_empathy_message
from datetime import datetime
import logging

router = APIRouter(
    prefix="/api",  # ✅ 최상위 /api
    tags=["감정 분석 및 공감 메시지"]
)

# =======================
# 📌 Request / Response DTO
# =======================

class EmotionRequest(BaseModel):
    content: str

class EmotionResponse(BaseModel):
    domain_emotion: str
    scores: Dict[str, float]
    vector: List[float]
    dim: int
    encouragement_message: str
    created_at: datetime

class EmpathyRequest(BaseModel):
    emotion: str
    content: str

class EmpathyResponse(BaseModel):
    message: str

# =======================
# ✅ 1. 감정 분석만
# =======================
@router.post("/emotion", response_model=EmotionResponse, summary="감정 분석")
def analyze_only(request: EmotionRequest):
    """
    일기 내용을 기반으로 감정 분석만 수행합니다.
    """
    try:
        result = analyze_emotion(request.content)
        return EmotionResponse(
            domain_emotion=result["top_emotion"],
            scores=result["scores"],
            vector=result["vector"],
            dim=result["dim"],
            encouragement_message="",  # 메시지는 포함하지 않음
            created_at=result["created_at"]
        )
    except Exception as e:
        logging.exception("감정 분석 실패")
        raise HTTPException(status_code=500, detail=str(e))

# =======================
# ✅ 2. 공감 메시지만
# =======================
@router.post("/empathy", response_model=EmpathyResponse, summary="공감 메시지 생성")
def create_message(request: EmpathyRequest):
    """
    감정(emotion)과 일기 내용을 바탕으로 공감 메시지를 생성합니다.
    """
    try:
        message = generate_empathy_message(request.emotion, request.content)
        return EmpathyResponse(message=message)
    except Exception as e:
        logging.exception("공감 메시지 생성 실패")
        raise HTTPException(status_code=500, detail=str(e))

# =======================
# ✅ 3. 감정 분석 + 메시지 통합
# =======================
@router.post("/full", response_model=EmotionResponse, summary="감정 분석 및 공감 메시지 생성")
def analyze_with_message(request: EmotionRequest):
    """
    감정 분석과 공감 메시지를 동시에 수행하는 통합 API입니다.
    """
    try:
        result = analyze_emotion(request.content)
        message = generate_empathy_message(result["top_emotion"], request.content)
        return EmotionResponse(
            domain_emotion=result["top_emotion"],
            scores=result["scores"],
            vector=result["vector"],
            dim=result["dim"],
            encouragement_message=message,
            created_at=result["created_at"]
        )
    except Exception as e:
        logging.exception("전체 분석 및 메시지 생성 실패")
        raise HTTPException(status_code=500, detail=str(e))
