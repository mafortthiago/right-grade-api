ALTER TABLE assessment RENAME COLUMN class_id TO grading_period_id;
ALTER TABLE assessment DROP CONSTRAINT fk_class;
ALTER TABLE assessment ADD CONSTRAINT fk_grading_period FOREIGN KEY (grading_period_id) REFERENCES grading_period(id)