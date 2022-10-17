BEGIN;

ALTER TABLE IF EXISTS public."employee_roles"
  RENAME TO "employees_roles";

END;