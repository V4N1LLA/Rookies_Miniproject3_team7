from fastapi import FastAPI, Request
from pydantic import BaseModel
from langchain_openai import ChatOpenAI
from langchain_core.messages import HumanMessage
from dotenv import load_dotenv
from faiss_index import insert_vector
import os
import json

# Load .env
load_dotenv()
OPENAI_API_KEY = os.getenv("OPENAI_API_KEY")

# FastAPI app with Swagger metadata
app = FastAPI(
    title="FastAPI Chat & Vector API",
    description="GPT 기반 대화 및 감정 분석 벡터 저장 API",
    version="1.0.0"
)

# LangChain LLM
llm = ChatOpenAI(model="gpt-3.5-turbo")

# Request schema
class ChatRequest(BaseModel):
    sessionId: int
    sender: str
    content: str

@app.post(
    "/api/chat",
    summary="LLM 응답 생성",
    description="LangChain과 OpenAI GPT 모델을 활용해 사용자 메시지에 대한 응답을 생성합니다."
)
async def chat_endpoint(req: ChatRequest):
    human_message = HumanMessage(content=req.content)
    response = llm.invoke([human_message])
    return {"response": response.content}

@app.post(
    "/api/vectors/save",
    summary="감정 벡터 저장",
    description="분석 ID와 함께 전달된 벡터를 FAISS Index에 저장합니다."
)
async def save_vector(request: Request):
    data = await request.json()
    print("Received vector data:", data)

    vector_json = data['vector']
    if isinstance(vector_json, str):
        vector = json.loads(vector_json)
    else:
        vector = vector_json
    analysis_id = data['analysis_id']
    insert_vector(analysis_id, vector)

    return {"message": "Vector saved successfully"}
