-- E1-S1: Flyway baseline — domain tables added in E1-S2+
CREATE TABLE application_baseline (
    id             SMALLINT PRIMARY KEY DEFAULT 1 CHECK (id = 1),
    initialized_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

INSERT INTO application_baseline (id) VALUES (1);
