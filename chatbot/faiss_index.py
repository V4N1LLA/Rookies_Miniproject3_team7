import faiss
import numpy as np
import os

# FAISS Index 설정
DIMENSION = 1536  # OpenAI embedding dimension
index_dir = "./faiss_data"
os.makedirs(index_dir, exist_ok=True)
index_file = os.path.join(index_dir, "faiss.index")

# 인덱스 로딩 또는 생성
if os.path.exists(index_file):
    index = faiss.read_index(index_file)
    print("✅ FAISS index loaded from file.")
else:
    index = faiss.IndexFlatL2(DIMENSION)
    print("✅ New FAISS index created.")

# 벡터 삽입 함수
def insert_vector(analysis_id, vector_list):
    if len(vector_list) != DIMENSION:
        raise ValueError(f"❌ 잘못된 벡터 차원: {len(vector_list)}. 필요: {DIMENSION}")

    np_vector = np.array(vector_list, dtype=np.float32).reshape(1, -1)
    index.add(np_vector)
    faiss.write_index(index, index_file)
    print(f"✅ Vector inserted (analysis_id: {analysis_id})")

# 벡터 검색 함수
def search_vector(query_vector, top_k=5):
    if len(query_vector) != DIMENSION:
        raise ValueError(f"❌ 잘못된 벡터 차원: {len(query_vector)}. 필요: {DIMENSION}")

    np_vector = np.array(query_vector, dtype=np.float32).reshape(1, -1)
    distances, indices = index.search(np_vector, top_k)
    return {
        "indices": indices.tolist(),
        "distances": distances.tolist()
    }
