ALTER TABLE users
    ADD COLUMN auth_version INT NOT NULL DEFAULT 0;