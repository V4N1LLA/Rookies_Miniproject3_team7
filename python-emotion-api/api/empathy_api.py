from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from analysis.message_generator import generate_empathy_message
import logging

router = APIRouter(
    prefix="/api/message",
    tags=["공감 메시지 생성"]
)

class EmpathyRequest(BaseModel):
    emotion: str
    content: str

class EmpathyResponse(BaseModel):
    message: str

@router.post("", response_model=EmpathyResponse)
def create_message(request: EmpathyRequest):
    try:
        message = generate_empathy_message(request.emotion, request.content)
        return EmpathyResponse(message=message)
    except Exception as e:
        logging.exception("공감 메시지 생성 중 예외 발생")
        raise HTTPException(status_code=500, detail=f"공감 메시지 생성 실패: {str(e)}")
