FROM python:3.11-slim

WORKDIR /app

COPY requirements.txt .


# ✅ pip 업그레이드 추가
RUN pip install --upgrade pip


# ✅ requirements 먼저 설치
RUN pip install --no-cache-dir -r requirements.txt \
    && pip install --no-cache-dir protobuf

# ✅ curl 설치 (healthcheck용)
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

COPY . .

EXPOSE 8000

CMD ["uvicorn", "app:app", "--host", "0.0.0.0", "--port", "8000"]
