-- insert user for admin
INSERT INTO "users"(
	login,password,email)
	VALUES ('$$$admin_login$$$','','$$$admin_email$$$');

-- insert default department
INSERT INTO "departments"(
    depart_name)
    VALUES ('$$$default_department$$$');

-- insert roles for spring security
INSERT INTO "roles"(
    role)
    VALUES ('admin');
INSERT INTO "roles"(
    role)
    VALUES ('user');

-- insert employee for admin
INSERT INTO "employees"(
    name,surname,sex,birth_date,depart_id,employment_date,user_id)
    VALUES ('Admin','Admin','M',CURRENT_DATE,CURRVAL('seq_depart_id'),CURRENT_DATE,CURRVAL('seq_user_id'));

-- roles for admin
INSERT INTO "employees_roles"(
    emp_id,role_id)
    VALUES (CURRVAL('seq_emp_id'),CURRVAL('seq_role_id')-1);
INSERT INTO "employees_roles"(
    emp_id,role_id)
    VALUES (CURRVAL('seq_emp_id'),CURRVAL('seq_role_id'));