CREATE TABLE recovery_assessment(
    id UUID PRIMARY KEY NOT NULL,
    name VARCHAR(20) NOT NULL,
    grading_period_id UUID,
    CONSTRAINT fk_grading_period FOREIGN KEY (grading_period_id) REFERENCES grading_period(id),
    created_at TIMESTAMP,
    assessment_id UUID,
    CONSTRAINT fk_assessment FOREIGN KEY (assessment_id) REFERENCES assessment(id),
    value NUMERIC(4,2)
)