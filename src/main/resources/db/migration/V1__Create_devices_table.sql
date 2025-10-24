CREATE TABLE devices (
    id BIGSERIAL PRIMARY KEY,
    device_id VARCHAR(255) NOT NULL UNIQUE,
    user_id VARCHAR(255) NOT NULL,
    device_name VARCHAR(255) NOT NULL,
    public_key TEXT NOT NULL,
    fcm_token VARCHAR(500),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_seen_at TIMESTAMP
);

CREATE INDEX idx_devices_user_id ON devices(user_id);
CREATE INDEX idx_devices_device_id ON devices(device_id);
CREATE INDEX idx_devices_active ON devices(is_active);
