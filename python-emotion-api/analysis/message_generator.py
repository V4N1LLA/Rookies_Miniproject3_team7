# analysis/message_generator.py

def generate_message(content: str, emotion: str) -> str:
    if emotion == "HAPPY":
        return "기분 좋은 하루를 보내셨군요! 오늘의 행복이 오래가길 바라요 🌞"
    elif emotion == "SAD":
        return "오늘 힘든 일이 있었군요. 내일은 더 좋은 하루가 될 거예요 🌈"
    else:
        return "당신의 감정을 이해하고 있어요. 스스로에게 친절하게 대해주세요 💙"
