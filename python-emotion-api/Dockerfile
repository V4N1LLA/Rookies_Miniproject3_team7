FROM python:3.11-slim

WORKDIR /app

COPY requirements.txt .

# ✅ requirements 먼저 설치
RUN pip install --no-cache-dir -r requirements.txt

# ✅ protobuf 명시적으로 설치 (감정 모델 관련 ImportError 방지)
RUN pip install protobuf

# ✅ curl 설치 (healthcheck용)
RUN apt-get update && apt-get install -y curl && apt-get clean

COPY . .

EXPOSE 8000

CMD ["uvicorn", "app:app", "--host", "0.0.0.0", "--port", "8000"]
