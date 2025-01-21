CREATE TABLE grading_period (
    id UUID NOT NULL PRIMARY KEY,
    name VARCHAR(20),
    class_id UUID NOT NULL,
    CONSTRAINT fk_class FOREIGN KEY (class_id) REFERENCES class(id),
    created_at TIMESTAMP
)