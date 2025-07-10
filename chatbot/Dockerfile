# Base image
FROM python:3.10

# Set working directory
WORKDIR /app

# Copy files
COPY requirements.txt .
COPY app.py .
COPY faiss_index.py ./


# Install dependencies
RUN pip install --upgrade pip
RUN pip install -r requirements.txt

# Expose port
EXPOSE 8000

# Command
CMD ["uvicorn", "app:app", "--host", "0.0.0.0", "--port", "8000"]
