# faiss_index.py

import faiss
import numpy as np
import os

# FAISS Index 초기화
DIMENSION = 1536  # OpenAI embedding dimension
index_dir = "./faiss_data"  # ✅ volume mount path
os.makedirs(index_dir, exist_ok=True)
index_file = os.path.join(index_dir, "faiss.index")

# Index 로드 혹은 새로 생성
if os.path.exists(index_file):
    index = faiss.read_index(index_file)
    print("FAISS index loaded from file.")
else:
    index = faiss.IndexFlatL2(DIMENSION)  # L2 거리 기반
    print("New FAISS index created.")

# 벡터 추가 함수
def insert_vector(analysis_id, vector_list):
    np_vector = np.array(vector_list, dtype=np.float32).reshape(1, -1)
    index.add(np_vector)
    faiss.write_index(index, index_file)
    print(f"Vector inserted. analysis_id: {analysis_id}")

def search_vector(query_vector, top_k=5):
    np_vector = np.array(query_vector, dtype=np.float32).reshape(1, -1)
    distances, indices = index.search(np_vector, top_k)
    return {
        "indices": indices.tolist(),
        "distances": distances.tolist()
    }