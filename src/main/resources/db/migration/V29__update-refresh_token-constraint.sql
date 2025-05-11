ALTER TABLE refresh_token
DROP CONSTRAINT fk_teacher,
ADD CONSTRAINT fk_teacher
FOREIGN KEY (teacher_id)
REFERENCES teachers(id)
ON DELETE CASCADE;
