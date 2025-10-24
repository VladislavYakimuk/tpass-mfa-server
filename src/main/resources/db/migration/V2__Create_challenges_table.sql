CREATE TABLE challenges (
    id BIGSERIAL PRIMARY KEY,
    challenge_id VARCHAR(255) NOT NULL UNIQUE,
    user_id VARCHAR(255) NOT NULL,
    device_id VARCHAR(255) NOT NULL,
    challenge_data TEXT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    resolved_at TIMESTAMP,
    nas_ip VARCHAR(45),
    caller_id VARCHAR(255)
);

CREATE INDEX idx_challenges_challenge_id ON challenges(challenge_id);
CREATE INDEX idx_challenges_user_id ON challenges(user_id);
CREATE INDEX idx_challenges_device_id ON challenges(device_id);
CREATE INDEX idx_challenges_status ON challenges(status);
CREATE INDEX idx_challenges_expires_at ON challenges(expires_at);
