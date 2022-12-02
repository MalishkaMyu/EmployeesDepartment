ALTER TABLE IF EXISTS "employees"
  ADD COLUMN employment_date date;
ALTER TABLE IF EXISTS "employees"
  ADD COLUMN created_at timestamp NOT NULL;
ALTER TABLE IF EXISTS "employees"
  ADD COLUMN updated_at timestamp NOT NULL;

ALTER TABLE IF EXISTS "employees"
  ADD CONSTRAINT "employees_unique_name_surname" UNIQUE (name,surname);
ALTER TABLE IF EXISTS "employees"
  RENAME CONSTRAINT "tblEmployees_pkey" TO "employees_pkey";

ALTER TABLE IF EXISTS "departments"
  ADD CONSTRAINT "departments_unique_depart_name" UNIQUE (depart_name);
ALTER TABLE IF EXISTS "departments"
  RENAME CONSTRAINT "tblDepartments_pkey" TO "departments_pkey";

ALTER TABLE IF EXISTS "roles"
  ADD CONSTRAINT "roles_unique_role_name" UNIQUE (role);
ALTER TABLE IF EXISTS "roles"
  RENAME CONSTRAINT "tblRoles_pkey" TO "roles_pkey";

ALTER TABLE IF EXISTS "employees_roles"
  RENAME CONSTRAINT "tblEmployeeRoles_pkey" TO "employees_roles_pkey";