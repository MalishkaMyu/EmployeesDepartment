
ALTER TABLE IF EXISTS "tblDepartments"
  RENAME TO "departments";
ALTER TABLE IF EXISTS "tblRoles"
  RENAME TO "roles";
ALTER TABLE IF EXISTS "tblEmployees"
  RENAME TO "employees";
ALTER TABLE IF EXISTS "tblEmployeeRoles"
  RENAME TO "employees_roles";
