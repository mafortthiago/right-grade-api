CREATE TABLE code (
    id UUID PRIMARY KEY NOT NULL,
    email VARCHAR(255) NOT NULL,
    code VARCHAR(6) NOT NULL,
    expiration TIMESTAMP WITHOUT TIME ZONE NOT NULL
);
