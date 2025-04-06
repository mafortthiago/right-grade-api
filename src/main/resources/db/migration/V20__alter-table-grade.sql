ALTER TABLE grade add recovery_assessment_id UUID;
ALTER TABLE grade add CONSTRAINT fk_recovery_assessment FOREIGN KEY (recovery_assessment_id) REFERENCES recovery_assessment(id)
