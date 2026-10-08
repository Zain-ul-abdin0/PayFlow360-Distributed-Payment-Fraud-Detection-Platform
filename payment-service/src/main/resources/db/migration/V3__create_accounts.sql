CREATE TABLE accounts (
    id BIGSERIAL PRIMARY KEY,

    account_number VARCHAR(50) NOT NULL UNIQUE,

    owner_name VARCHAR(100) NOT NULL,

    balance NUMERIC(19, 4) NOT NULL,

    currency VARCHAR(3) NOT NULL,

    version BIGINT NOT NULL DEFAULT 0
);