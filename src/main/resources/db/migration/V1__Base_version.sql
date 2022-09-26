-- This script was generated by a beta version of the ERD tool in pgAdmin 4.
-- Please log an issue at https://redmine.postgresql.org/projects/pgadmin4/issues/new if you find any bugs, including reproduction steps.
BEGIN;


CREATE TABLE IF NOT EXISTS public."tblDepartments"
(
    depart_id integer NOT NULL,
    depart_name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "tblDepartments_pkey" PRIMARY KEY (depart_id)
);

CREATE TABLE IF NOT EXISTS public."tblEmployeeRoles"
(
    emp_id integer NOT NULL,
    role_id integer NOT NULL,
    CONSTRAINT "tblEmployeeRoles_pkey" PRIMARY KEY (emp_id, role_id)
);

CREATE TABLE IF NOT EXISTS public."tblEmployees"
(
    emp_id integer NOT NULL,
    name character varying(30) COLLATE pg_catalog."default" NOT NULL,
    surname character varying(30) COLLATE pg_catalog."default" NOT NULL,
    sex character(1) COLLATE pg_catalog."default" NOT NULL,
    birth_date date NOT NULL,
    pass_no character varying(12) COLLATE pg_catalog."default",
    pass_valid date,
    depart_id integer NOT NULL,
    CONSTRAINT "tblEmployees_pkey" PRIMARY KEY (emp_id)
);

CREATE TABLE IF NOT EXISTS public."tblRoles"
(
    role_id integer NOT NULL,
    role character varying(30) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "tblRoles_pkey" PRIMARY KEY (role_id)
);

ALTER TABLE IF EXISTS public."tblEmployeeRoles"
    ADD CONSTRAINT "tblEmployeeRoles_fkey_emp" FOREIGN KEY (emp_id)
    REFERENCES public."tblEmployees" (emp_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;
CREATE INDEX IF NOT EXISTS "fki_tblEmployeeRoles_fkey_emp"
    ON public."tblEmployeeRoles"(emp_id);


ALTER TABLE IF EXISTS public."tblEmployeeRoles"
    ADD CONSTRAINT "tblEmployeeRoles_fkey_role" FOREIGN KEY (role_id)
    REFERENCES public."tblRoles" (role_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;
CREATE INDEX IF NOT EXISTS "fki_tblEmployeeRoles_fkey_role"
    ON public."tblEmployeeRoles"(role_id);


ALTER TABLE IF EXISTS public."tblEmployees"
    ADD CONSTRAINT "tblEmployees_fkey_depart" FOREIGN KEY (depart_id)
    REFERENCES public."tblDepartments" (depart_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;

COMMENT ON CONSTRAINT "tblEmployees_fkey_depart" ON public."tblEmployees"
    IS 'Fereign Key department';

CREATE INDEX IF NOT EXISTS "fki_tblEmployees_fkey_depart"
    ON public."tblEmployees"(depart_id);

END;