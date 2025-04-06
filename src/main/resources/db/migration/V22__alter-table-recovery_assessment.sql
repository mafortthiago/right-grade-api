ALTER TABLE recovery_assessment
DROP CONSTRAINT fk_assessment,
ADD CONSTRAINT fk_assessment
FOREIGN KEY (assessment_id)
REFERENCES assessment(id)
ON DELETE CASCADE;