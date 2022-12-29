CREATE SEQUENCE seq_user_id;

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

INSERT INTO "users"(
	login,password,email)
	VALUES ('$$$admin_login$$$','','$$$admin_email$$$');