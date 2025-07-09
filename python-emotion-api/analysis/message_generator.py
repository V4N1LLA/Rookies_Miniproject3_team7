# ✅ message_generator.py

from langchain_openai import ChatOpenAI
from langchain.prompts import PromptTemplate
import os
from dotenv import load_dotenv

load_dotenv()

def generate_empathy_message(emotion: str, content: str) -> str:
    openai_api_key = os.getenv("OPENAI_API_KEY")
    if not openai_api_key:
        raise ValueError("OPENAI_API_KEY가 .env에 없습니다.")

    llm = ChatOpenAI(
        temperature=0.7,
        model_name="gpt-3.5-turbo",
        openai_api_key=openai_api_key
    )

    prompt = PromptTemplate(
        input_variables=["emotion", "content"],
        template=(
            "당신은 감정 전문가입니다.\n"
            "감정: {emotion}\n"
            "일기 내용: {content}\n\n"
            "감정을 공감하는 따뜻한 한국어 메시지를 한 문장으로 작성하세요."
        )
    )

    chain = prompt | llm
    result = chain.invoke({"emotion": emotion, "content": content})

    return result.content  # ✅ 문자열만 추출해서 반환
