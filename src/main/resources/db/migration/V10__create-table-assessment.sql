CREATE TABLE assessment(
    id UUID PRIMARY KEY NOT NULL,
    name VARCHAR(20) NOT NULL,
    class_id UUID,
    CONSTRAINT fk_class FOREIGN KEY (class_id) REFERENCES class(id),
    created_at TIMESTAMP
)