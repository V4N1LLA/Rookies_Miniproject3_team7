from fastapi import APIRouter
from pydantic import BaseModel
from analysis.message_generator import generate_empathy_message

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
    message = generate_empathy_message(request.emotion, request.content)
    return EmpathyResponse(message=message)
