-- deleting of columns 'created_at', 'updated_at' is necessary, because
-- in postgres the operation 'add column after' is impossible
ALTER TABLE IF EXISTS "users"
    DROP COLUMN created_at;
ALTER TABLE IF EXISTS "users"
    DROP COLUMN updated_at;
ALTER TABLE IF EXISTS "users"
    ADD COLUMN keycloak_id character varying(36) DEFAULT NULL;
ALTER TABLE IF EXISTS "users"
    ADD COLUMN created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE IF EXISTS "users"
    ADD COLUMN updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP;