CREATE SEQUENCE seq_user_id
START WITH 2
INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS "users"
(
    user_id bigint DEFAULT nextval('seq_user_id') NOT NULL,
    login character varying(20) NOT NULL,
    password character varying(100) NOT NULL,
    email character varying(70) NOT NULL,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT "users_pkey" PRIMARY KEY (user_id),
    CONSTRAINT "users_unique_login" UNIQUE (login),
    CONSTRAINT "users_unique_email" UNIQUE (email)
);
-- deleting of columns 'created_at', 'updated_at' is necessary, because
-- in postgres the operation 'add column after' is impossible
ALTER TABLE IF EXISTS "employees"
    DROP COLUMN created_at;
ALTER TABLE IF EXISTS "employees"
    DROP COLUMN updated_at;
ALTER TABLE IF EXISTS "employees"
    ADD COLUMN user_id bigint NOT NULL;
ALTER TABLE IF EXISTS "employees"
    ADD COLUMN created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE IF EXISTS "employees"
    ADD COLUMN updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE IF EXISTS "employees"
    ADD CONSTRAINT "employees_fkey_user" FOREIGN KEY (user_id)
    REFERENCES "users" (user_id)
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;
CREATE INDEX IF NOT EXISTS "fki_employees_fkey_user"
    ON "employees"(user_id);

-- insert user for admin
INSERT INTO "users"(
	user_id,login,password,email)
	VALUES (1,'$$$admin_login$$$','','$$$admin_email$$$');

-- insert default department
INSERT INTO "departments"(
    depart_id,depart_name)
    VALUES (1,'$$$default_department$$$');

-- insert roles for spring security
INSERT INTO "roles"(
    role_id,role)
    VALUES (1,'ROLE_ADMIN');
INSERT INTO "roles"(
    role_id,role)
    VALUES (2,'ROLE_USER');

-- insert employee for admin
INSERT INTO "employees"(
    emp_id,name,surname,sex,birth_date,depart_id,employment_date,user_id)
    VALUES (1,'Admin','Admin','M',CURRENT_DATE,1,CURRENT_DATE,1);

-- roles for admin
INSERT INTO "employees_roles"(
    emp_id,role_id)
    VALUES (1,1);
INSERT INTO "employees_roles"(
    emp_id,role_id)
    VALUES (1,2);