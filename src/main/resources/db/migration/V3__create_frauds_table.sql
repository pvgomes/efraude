-- Create fraud_status enum
CREATE TYPE fraud_status AS ENUM ('ACTIVE', 'ARCHIVED');

-- Create frauds table
CREATE TABLE frauds (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    slug VARCHAR(300) NOT NULL,
    title VARCHAR(300) NOT NULL,
    description TEXT NOT NULL,
    caution TEXT,
    url VARCHAR(2048),
    country VARCHAR(64) NOT NULL DEFAULT 'GLOBAL',
    status fraud_status NOT NULL DEFAULT 'ACTIVE',
    created_by UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_frauds_created_by FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX idx_frauds_created_at ON frauds(created_at DESC);
CREATE INDEX idx_frauds_status_created_at ON frauds(status, created_at DESC);
CREATE INDEX idx_frauds_slug ON frauds(slug);
CREATE INDEX idx_frauds_country ON frauds(country);
