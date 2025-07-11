import os
import sys

sys.path.append(os.path.dirname(os.path.abspath(__file__)))

from fastapi import FastAPI
from dotenv import load_dotenv
from openai import OpenAI, AuthenticationError
from fastapi.openapi.docs import get_swagger_ui_html
from fastapi.responses import HTMLResponse

# ✅ 분리된 라우터만 import
from api.empathy_api import router as emotion_router

# ✅ .env 로드
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

# ✅ 통합 라우터 등록 (/api/emotion, /api/empathy, /api/full)
app.include_router(emotion_router)

# ✅ OpenAI API Key 유효성 검사 (없으면 테스트 환경으로 우회)
@app.on_event("startup")
def validate_openai_key():
    api_key = os.getenv("OPENAI_API_KEY")

    if not api_key or api_key.startswith("sk-proj-") and len(api_key) < 40:
        print("⚠️ OPENAI_API_KEY 없음 또는 비정상 - 테스트 환경으로 실행됨")
        return

    try:
        client = OpenAI(api_key=api_key)
        _ = client.models.list()
        print("✅ OpenAI API Key 유효성 검사 통과")
    except AuthenticationError:
        print("❌ OpenAI Key 인증 실패 - 테스트 환경으로 우회")
    except Exception as e:
        print(f"❌ OpenAI 검사 중 오류 - {e} → 테스트 환경으로 우회")

# ✅ Swagger Docs 커스텀 경로
@app.get("/docs", include_in_schema=False)
async def custom_swagger_ui():
    return get_swagger_ui_html(openapi_url="/openapi.json", title="API 문서")

@app.get("/openapi.json", include_in_schema=False)
async def openapi_json():
    return app.openapi()
