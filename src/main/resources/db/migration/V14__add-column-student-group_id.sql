ALTER TABLE class ADD COLUMN group_id UUID;
ALTER TABLE class ADD CONSTRAINT fk_group FOREIGN KEY (group_id) REFERENCES class(id);