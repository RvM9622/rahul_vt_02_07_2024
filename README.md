Please find postgres query to create table urlmapping

CREATE TABLE url_mapping (
    id SERIAL PRIMARY KEY,
    short_url VARCHAR(30) UNIQUE NOT NULL,
    destination_url TEXT NOT NULL,
    expiration_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
