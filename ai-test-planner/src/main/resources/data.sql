CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE nl_testcase_training (
    id BIGSERIAL PRIMARY KEY,
    nl TEXT NOT NULL,
    json TEXT NOT NULL,
    embedding VECTOR(1024),
    created_at TIMESTAMP DEFAULT now()
);

CREATE INDEX nl_embedding_idx
ON nl_testcase_training
USING ivfflat (embedding vector_cosine_ops)
WITH (lists = 100);

CREATE UNIQUE INDEX nl_unique_idx
ON nl_testcase_training(nl);