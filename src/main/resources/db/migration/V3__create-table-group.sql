CREATE TABLE class (
    id UUID NOT NULL PRIMARY KEY,
    name VARCHAR(100),
    teacher_id UUID,
    created_at TIMESTAMP,
    CONSTRAINT fk_teacher FOREIGN KEY(teacher_id) REFERENCES teachers(id)
);
