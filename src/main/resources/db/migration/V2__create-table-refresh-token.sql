CREATE TABLE refresh_token(
    id UUID NOT NULL,
    teacher_id UUID UNIQUE,
    token VARCHAR(255) NOT NULL,
    CONSTRAINT fk_teacher FOREIGN KEY (teacher_id) REFERENCES teachers(id)
);