import os
from dotenv import load_dotenv
from fastapi import FastAPI
from fastapi import Request
from pydantic import BaseModel
from langchain_openai import ChatOpenAI
from langchain_core.messages import HumanMessage
from faiss_index import insert_vector


# Load .env
load_dotenv()
OPENAI_API_KEY = os.getenv("OPENAI_API_KEY")

# FastAPI app
app = FastAPI()

# LangChain LLM
llm = ChatOpenAI(model="gpt-3.5-turbo")

# Request schema
class ChatRequest(BaseModel):
    sessionId: int
    sender: str
    content: str

@app.post("/api/chat")
async def chat_endpoint(req: ChatRequest):
    human_message = HumanMessage(content=req.content)
    response = llm.invoke([human_message])
    return {"response": response.content}

@app.post("/api/vectors/save")
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
