ALTER TABLE IF EXISTS "employees"
  ADD COLUMN employment_date date;
ALTER TABLE IF EXISTS "employees"
  ADD COLUMN created_at timestamp NOT NULL;
ALTER TABLE IF EXISTS "employees"
  ADD COLUMN updated_at timestamp NOT NULL;

ALTER TABLE IF EXISTS "employees"
  ADD CONSTRAINT "employees_unique_name_surname" UNIQUE (name,surname);

ALTER TABLE IF EXISTS "departments"
  ADD CONSTRAINT "departments_unique_depart_name" UNIQUE (depart_name);

ALTER TABLE IF EXISTS "roles"
  ADD CONSTRAINT "roles_unique_role_name" UNIQUE (role);