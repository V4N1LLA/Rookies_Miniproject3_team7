import os
from dotenv import load_dotenv
from fastapi import FastAPI
from pydantic import BaseModel
from langchain_openai import ChatOpenAI
from langchain_core.messages import HumanMessage

# Load .env
load_dotenv()
OPENAI_API_KEY = os.getenv("OPENAI_API_KEY")

# FastAPI app
app = FastAPI()

# LangChain LLM
llm = ChatOpenAI(model="gpt-3.5-turbo")

# Request schema
class ChatRequest(BaseModel):
    message: str

@app.post("/chat")
async def chat_endpoint(req: ChatRequest):
    human_message = HumanMessage(content=req.message)
    response = llm.invoke([human_message])
    return {"response": response.content}
