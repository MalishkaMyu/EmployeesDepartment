BEGIN;

ALTER TABLE IF EXISTS public."tblDepartments"
  RENAME TO "departments";
ALTER TABLE IF EXISTS public."tblRoles"
  RENAME TO "roles";
ALTER TABLE IF EXISTS public."tblEmployees"
  RENAME TO "employees";
ALTER TABLE IF EXISTS public."tblEmployeeRoles"
  RENAME TO "employee_roles";

ALTER TABLE IF EXISTS public."employees"
  ALTER COLUMN "depart_id" TYPE bigint;

END;
