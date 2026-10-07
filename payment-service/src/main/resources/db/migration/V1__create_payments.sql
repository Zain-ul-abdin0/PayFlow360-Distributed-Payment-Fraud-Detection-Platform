CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,

    transaction_id VARCHAR(50) NOT NULL UNIQUE,

    source_account_id VARCHAR(50) NOT NULL,

    destination_account_id VARCHAR(50) NOT NULL,

    amount NUMERIC(19, 4) NOT NULL,

    currency VARCHAR(3) NOT NULL,

    status VARCHAR(30) NOT NULL,

    description VARCHAR(255),

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL
);