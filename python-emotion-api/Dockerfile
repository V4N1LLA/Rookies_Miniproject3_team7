# =========================
# Stage 1: Build
# =========================
FROM python:3.11-slim AS builder

WORKDIR /app

# 1. Install build tools
RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential curl && \
    rm -rf /var/lib/apt/lists/*

# 2. Upgrade pip
RUN pip install --upgrade pip

# 3. Copy only requirements
COPY requirements.txt .

# 4. Install dependencies with CPU-optimized PyTorch
RUN pip install --user --no-cache-dir --extra-index-url https://download.pytorch.org/whl/cpu \
    -r requirements.txt \
    && pip install --user --no-cache-dir protobuf \
    && rm -rf /root/.cache

# =========================
# Stage 2: Runtime
# =========================
FROM python:3.11-slim

WORKDIR /app

# 1. Install minimal dependencies
RUN apt-get update && apt-get install -y --no-install-recommends \
    curl && rm -rf /var/lib/apt/lists/*

# 2. Copy dependencies from builder
COPY --from=builder /root/.local /root/.local

# 3. Copy application source code
COPY . .

# 4. Set ENV
ENV PATH="/root/.local/bin:$PATH" \
    PYTHONPATH="/root/.local/lib/python3.11/site-packages"

# 5. Clean up cache
RUN find /root/.local -type d -name "__pycache__" -exec rm -rf {} + && \
    find /root/.local -name "*.pyc" -delete && \
    rm -rf /root/.cache

# 6. Expose & Start
EXPOSE 8000
CMD ["uvicorn", "app:app", "--host", "0.0.0.0", "--port", "8000"]
