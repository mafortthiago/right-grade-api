CREATE TABLE account_confirmation_token(
    id UUID NOT NULL PRIMARY KEY,
    token CHAR(6) NOT NULL,
    email VARCHAR(120) NOT NULL,
    expiration TIMESTAMP NOT NULL
);