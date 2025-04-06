ALTER TABLE grade
DROP CONSTRAINT fk_recovery_assessment,
ADD CONSTRAINT fk_recovery_assessment
FOREIGN KEY (recovery_assessment_id)
REFERENCES recovery_assessment(id)
ON DELETE CASCADE;