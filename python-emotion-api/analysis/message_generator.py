from langchain_openai import ChatOpenAI
from langchain.prompts import PromptTemplate
import os
import re
from dotenv import load_dotenv

load_dotenv()

def generate_empathy_message(emotion: str, content: str) -> str:
    openai_api_key = os.getenv("OPENAI_API_KEY")

    if not openai_api_key:
        print("⚠️ OPENAI_API_KEY 없음 - 테스트용 메시지 반환")
        return f"[테스트용] '{emotion}' 감정에 공감합니다. 잠시 쉬어가는 것도 좋은 방법이에요."

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
            "사용자의 감정에 공감하는 따뜻한 메시지를 한 문장으로 작성하고,\n"
            "그 감정 상태에서 도움이 될 만한 조언이나 권유를 한 문장 덧붙이세요.\n"
            "총 두 문장으로 작성하고, 너무 길지 않게 150자 이내로 작성하세요."
        )
    )

    chain = prompt | llm

    try:
        result = chain.invoke({"emotion": emotion, "content": content})
        raw_text = result.content.strip()

        # ✅ "메시지:", "도움/권유:" 제거 + 공백 정리
        cleaned_text = re.sub(r"(메시지:\s*|도움/권유:\s*)", "", raw_text).strip()

        return cleaned_text[:150]

    except Exception as e:
        print(f"❌ 공감 메시지 생성 실패: {e}")
        return f"감정({emotion})을 느끼셨군요. 당신의 이야기에 공감합니다. 따뜻한 차 한 잔은 어떠세요?"
