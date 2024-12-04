--DROP TABLE credit

CREATE TABLE client (
    client_id UUID PRIMARY KEY,
    last_name VARCHAR NOT NULL,
    first_name VARCHAR NOT NULL,
    middle_name VARCHAR,
    birth_date DATE NOT NULL,
    email VARCHAR NOT NULL,
    gender VARCHAR,
    marital_status VARCHAR,
    dependent_amount INT,
    passport_id JSONB,
    employment_id JSONB,
    account_number VARCHAR
);

CREATE TABLE credit (
    credit_id UUID PRIMARY KEY,
    amount DECIMAL(22, 2) NOT NULL,
    term INT NOT NULL,
    monthly_payment DECIMAL(22, 2) NOT NULL,
    rate DECIMAL(22, 2) NOT NULL,
    psk DECIMAL(22, 2) NOT NULL,
    payment_schedule JSONB,
    insurance_enabled BOOLEAN NOT NULL,
    salary_client BOOLEAN NOT NULL,
    credit_status VARCHAR NOT NULL
);

CREATE TABLE statements (
    statement_id UUID PRIMARY KEY,
    client_id UUID REFERENCES client(client_id),
    credit_id UUID REFERENCES credit(credit_id),
    status VARCHAR NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    applied_offer JSONB,
    sign_date TIMESTAMP,
    ses_code VARCHAR,
    status_history JSONB
);
