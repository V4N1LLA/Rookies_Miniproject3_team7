import sys
import os
sys.path.append(os.path.dirname(os.path.abspath(__file__)))

from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List, Dict
from datetime import datetime
from dotenv import load_dotenv

from api.empathy_api import router as empathy_router
from analysis.emotion_analyzer import analyze_emotion

# ✅ OpenAI SDK 유효성 검사용
from openai import OpenAI, AuthenticationError

# ✅ Swagger 문서용
from fastapi.openapi.utils import get_openapi
from fastapi.openapi.docs import get_swagger_ui_html
from fastapi.responses import HTMLResponse
from fastapi.openapi.models import Contact

load_dotenv()

app = FastAPI(
    title="감정 분석 & 공감 메시지 API",
    description="일기 감정 분석과 공감 메시지 생성을 위한 FastAPI 서비스입니다.",
    version="1.0.0",
    contact={
        "name": "Team7 - Moodiary",
        "email": "team7@example.com"
    }
)

# ✅ 공감 메시지 라우터 등록
app.include_router(empathy_router)

# ✅ OpenAI API Key 유효성 검사
@app.on_event("startup")
def validate_openai_key():
    api_key = os.getenv("OPENAI_API_KEY")
    if not api_key:
        raise RuntimeError("❌ .env 파일에 OPENAI_API_KEY가 설정되어 있지 않습니다.")
    try:
        client = OpenAI(api_key=api_key)
        _ = client.models.list()
        print("✅ OpenAI API Key 유효성 검사 통과")
    except AuthenticationError:
        raise RuntimeError("❌ OpenAI API Key가 유효하지 않습니다.")
    except Exception as e:
        raise RuntimeError(f"OpenAI API Key 검사 중 오류 발생: {e}")


# 📌 감정 분석 요청 형식
class EmotionRequest(BaseModel):
    content: str


# 📌 감정 분석 응답 형식
class EmotionResponse(BaseModel):
    domain_emotion: str
    scores: Dict[str, float]
    vector: List[float]
    dim: int
    encouragement_message: str  # ✅ 현재는 빈 문자열로 반환됨
    created_at: datetime


# ✅ 감정 분석 API (공감 메시지는 제거됨)
@app.post("/analyze", response_model=EmotionResponse, tags=["감정 분석"])
async def analyze(request: EmotionRequest):
    try:
        emotion_result = analyze_emotion(request.content)

        return EmotionResponse(
            domain_emotion=emotion_result["top_emotion"],
            scores=emotion_result["scores"],
            vector=emotion_result["vector"],
            dim=emotion_result["dim"],
            encouragement_message="",  # ✅ 이제는 공감 메시지 X
            created_at=emotion_result["created_at"]
        )
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


# ✅ Swagger Docs 커스텀 라우팅 (선택사항)
@app.get("/docs", include_in_schema=False)
async def custom_swagger_ui():
    return get_swagger_ui_html(openapi_url="/openapi.json", title="API 문서")

@app.get("/openapi.json", include_in_schema=False)
async def openapi_json():
    return app.openapi()
