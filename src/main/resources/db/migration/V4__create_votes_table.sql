-- Create vote_type enum
CREATE TYPE vote_type AS ENUM ('UP', 'DOWN');

-- Create votes table
CREATE TABLE votes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    fraud_id UUID NOT NULL,
    user_id UUID NOT NULL,
    type vote_type NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_votes_fraud FOREIGN KEY (fraud_id) REFERENCES frauds(id) ON DELETE CASCADE,
    CONSTRAINT fk_votes_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uk_votes_fraud_user UNIQUE (fraud_id, user_id)
);

-- Create indexes
CREATE INDEX idx_votes_fraud_id ON votes(fraud_id);
CREATE INDEX idx_votes_user_id ON votes(user_id);
