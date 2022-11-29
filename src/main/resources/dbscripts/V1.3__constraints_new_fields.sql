ALTER TABLE IF EXISTS "employees"
  ADD COLUMN employment_date date;
ALTER TABLE IF EXISTS "employees"
  ADD COLUMN created_at timestamp NOT NULL;
ALTER TABLE IF EXISTS "employees"
  ADD COLUMN updated_at timestamp NOT NULL;

ALTER TABLE IF EXISTS "employees"
  ADD CONSTRAINT "tblEmployees_unique_name_surname" UNIQUE (name,surname);

ALTER TABLE IF EXISTS "departments"
  ADD CONSTRAINT "tblDepartments_unique_depart_name" UNIQUE (depart_name);