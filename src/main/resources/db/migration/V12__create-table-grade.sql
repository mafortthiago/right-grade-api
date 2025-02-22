CREATE TABLE grade(
    id UUID PRIMARY KEY NOT NULL,
    value NUMERIC(4,2),
    student_id UUID NOT NULL,
    assessment_id UUID NOT NULL,
    is_recovery BOOLEAN,
    CONSTRAINT fk_student FOREIGN KEY (student_id) REFERENCES student(id),
    CONSTRAINT fk_assessment FOREIGN KEY (assessment_id) REFERENCES assessment(id)
)