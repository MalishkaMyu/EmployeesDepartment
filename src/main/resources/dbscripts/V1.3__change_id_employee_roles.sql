BEGIN;

ALTER TABLE IF EXISTS public."employee_roles"
  ALTER COLUMN "emp_id" TYPE bigint,
  ALTER COLUMN "role_id" TYPE bigint;

END;