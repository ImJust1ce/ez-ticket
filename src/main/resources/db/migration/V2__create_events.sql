CREATE TABLE events (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    venue      VARCHAR(255) NOT NULL,
    starts_at  TIMESTAMPTZ NOT NULL,
    status     VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_events_status CHECK (status IN ('DRAFT', 'PUBLISHED'))
);

CREATE INDEX idx_events_status ON events (status);
CREATE INDEX idx_events_starts_at ON events (starts_at);
